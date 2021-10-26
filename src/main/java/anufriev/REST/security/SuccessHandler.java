package anufriev.REST.security;

import anufriev.REST.OAuth2.CustomOAuth2User;
import anufriev.REST.model.Provider;
import anufriev.REST.model.Role;
import anufriev.REST.model.User;
import anufriev.REST.repository.RoleRepo;
import anufriev.REST.repository.UserRepo;
import anufriev.REST.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Component
public class SuccessHandler implements AuthenticationSuccessHandler {
    private final Logger LOGGER = Logger.getLogger("<-------- LOG: SuccessHandler -------->");

    private final UserService userService;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SuccessHandler(UserService userService, RoleRepo roleRepo, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
            // проверяем откуда пришел пользователь
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            LOGGER.info("GOOGLE authority: " + oAuth2User.getAuthorities());
            if(!userService.existsByEmail(oAuth2User.getEmail())) {
                addNewGoogleUserToDB(oAuth2User);
            } else {
                LOGGER.info("Welcome back " + oAuth2User.getName());
            }
        } catch (ClassCastException e) {
            LOGGER.warning(e.getMessage());
            LOGGER.info("LOCAL authority: " + authentication.getAuthorities());
        }

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin");
        } else if (roles.contains("ROLE_USER")) {
            response.sendRedirect("/user");
        } else
            response.sendRedirect("/login");
    }
    private void addNewGoogleUserToDB(CustomOAuth2User oAuth2User) {
        LOGGER.info("Creating new GOOGLE User...");
        User user = new User();
        user.setProvider(Provider.GOOGLE);
        user.setUsername(oAuth2User.getAttribute("given_name"));
        user.setLastname(oAuth2User.getAttribute("family_name"));
        user.setEmail(oAuth2User.getEmail());
        user.setPassword(passwordEncoder.encode("user"));

        Set<String> authList = AuthorityUtils.authorityListToSet(oAuth2User.getAuthorities());
        Set<Role> roleSet = new HashSet<>();
        if (authList.contains("ROLE_ADMIN")) {
            roleSet.add(roleRepo.findByName("ROLE_ADMIN"));
        }
        if (authList.contains("ROLE_USER")) {
            roleSet.add(roleRepo.findByName("ROLE_USER"));
        }

        user.setRoles(roleSet);
        LOGGER.info("Adding new GOOGLE User into DB...");
        userService.addUser(user);
        LOGGER.info("User " + user.getUsername() + " has been successfully added into DB!");
        LOGGER.info("For entering as registered user from Google use EMAIL and default PASSWORD \"user\"");
    }
}

