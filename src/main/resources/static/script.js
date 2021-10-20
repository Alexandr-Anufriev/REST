const url = 'http://localhost:8080/admin'

// email и роли авторизованного ADMIN в навигационной панеле
fetch("http://localhost:8080/currentUser")
    .then(response => response.json())
    .then(data => {
        document.getElementById('userId').innerText = data.id;
        document.getElementById('userFirstName').innerText = data.username;
        document.getElementById('userLastName').innerText = data.lastname;
        document.getElementById('userAge').innerText = data.age;
        document.getElementById('userEmail').innerText = data.email;
        document.getElementById("email-in-nav").innerText = data.email;
        let roles = '';
        data.roles.forEach(role => {
            roles += ' ' + role.name;
        })
        roles = roles.replaceAll("ROLE_", "")
        document.getElementById("role-in-nav").innerText = roles;
        document.getElementById("userRoles").innerText = roles;
    })
// email и роли авторизованного USER в навигационной панеле
// fetch("http://localhost:8080/user/currentUser")
//     .then(response => response.json())
//     .then(data => {
//         document.getElementById('userId').innerText = data.id;
//         document.getElementById('userFirstName').innerText = data.username;
//         document.getElementById('userLastName').innerText = data.lastname;
//         document.getElementById('userAge').innerText = data.age;
//         document.getElementById('userEmail').innerText = data.email;
//         document.getElementById("email-in-nav").innerText = data.email;
//         let roles = '';
//         data.roles.forEach(role => {
//             roles += ' ' + role.name;
//         })
//         roles = roles.replaceAll("ROLE_", "")
//         document.getElementById("role-in-nav").innerText = roles;
//         document.getElementById("userRoles").innerText = roles;
//     })

// все юзеры
const table = document.getElementById('table-all-users')
let out = '';
tableResult()
function tableResult() {
    out = ""
    table.innerText = ""
    out = `<tr class="text-center">
                <th>ID</th>
                <th>First name</th>
                <th>Last name</th>
                <th>Age</th>
                <th>Email</th>
                <th>Role</th>
                <th>Edit</th>
                <th>Delete</th>
                </tr>`
    fetch(url + "/all")
        .then(response => response.json())
        .then(data => {
            data.forEach(user => {
                let userRole = '';
                user.roles.forEach(role => {
                    userRole += ' ' + role.name;
                })
                userRole = userRole.replaceAll("ROLE_", "")
                let editButton = '<button type="button" class="btn btn-info text-light"' +
                    ' data-bs-toggle="modal" data-bs-target="#editModal" onclick="getProfileForEdit(' + user.id + ')">' +
                    'Edit' +
                    '</button>'
                let deleteButton = '<button type="button" class="btn btn-danger text-light"' +
                    ' data-bs-toggle="modal" data-bs-target="#deleteModal" onclick="getProfileForDelete(' + user.id + ')">' +
                    'Delete' +
                    '</button>'

                out += `<tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.lastname}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${userRole}</td>
                    <td>${editButton}</td>
                    <td>${deleteButton}</td>
                    </tr>`
            })
            table.innerHTML += out + '</table>';
        });
}

function clearTable() {
    table.innerText = "";
}

// new user
const newUserForm = document.getElementById('newUserForm');
newUserForm.addEventListener('submit', data => {
    // $('#firstName').empty().val('')
    // $('#lastName').empty().val('')
    // $('#age').empty().val('')
    // $('#email').empty().val('')
    // $('#password').empty().val('')
    // $('#roles').empty().val('')


    data.preventDefault();
    console.log('кнопка нажата')
    const select = data.target.roles;
    let len = select.options.length;
    let roles = [];
    for (let i = 0; i < len; i++) {
        if (select.options[i].selected === true) {
            roles.push({
                id: select.options[i].value,
                name: select.options[i].text,
            });
        }
    }
    let user = {
        username: `${data.target.firstName.value}`,
        lastname: `${data.target.lastName.value}`,
        age: `${data.target.age.value}`,
        email: `${data.target.email.value}`,
        password: `${data.target.password.value}`,
        roles: roles
    };
    console.log(JSON.stringify(user));
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json; charset=UTF-8'
        },
        body: JSON.stringify(user)
    })
        .then(response => console.log(response.status))
        .then(()=>{
            $('#table-all-users').empty(); //                                     тут
            tableResult()
        })
        .catch(e => console.error(e));
    $(".adm-btn").click();
})

// заполнение модального окна edit
function getProfileForEdit(id) {
    fetch(url + '/' + id)
        .then(response => response.json())
        .then(user => {
            document.getElementById('editId').value = user.id;
            document.getElementById('editFirstName').value = user.username;
            document.getElementById('editLastName').value = user.lastname;
            document.getElementById('editAge').value = user.age;
            document.getElementById('editEmail').value = user.email;
            document.getElementById('editPassword').value = null;
            document.getElementById('editRole').value = null;
        })
}
// отправка editForm
function sendProfileForEdit() {
    const editForm = document.getElementById('editForm');
    editForm.addEventListener('submit', (data) => {
        data.preventDefault();
        const select = data.target.editRole;
        let len = select.options.length;
        let roles = [];
        for (let i = 0; i < len; i++) {
            if (select.options[i].selected === true) {
                roles.push({
                    id: select.options[i].value,
                    name: select.options[i].text,
                });
            }
        }
        let user = {
            id: `${data.target.editId.value}`,
            username: `${data.target.editFirstName.value}`,
            lastname: `${data.target.editLastName.value}`,
            age: `${data.target.editAge.value}`,
            email: `${data.target.editEmail.value}`,
            password: `${data.target.editPassword.value}`,
            roles: roles,
        };
        console.log(JSON.stringify(user));
        fetch(url, {
            method: 'PUT',
            headers: {
                'Content-type': 'application/json',
            },
            body: JSON.stringify(user),
        })
            .then((response) => console.log(response.status))
            .then(()=> {
                clearTable();
                tableResult();
            })
            .catch(e => console.error(e))
    });
    // location.reload()
    $(".btn-close").click();
}

// заполнение модального окна delete
function getProfileForDelete(id) {
    fetch(url + '/' + id)
        .then(response => response.json())
        .then(user => {
            document.getElementById('deleteId').value = user.id;
            document.getElementById('deleteFirstName').value = user.username;
            document.getElementById('deleteLastName').value = user.lastname;
            document.getElementById('deleteAge').value = user.age;
            document.getElementById('deleteEmail').value = user.email;
        })
}
// отправка deleteForm
function sendFormDeleteProfile() {
    const deleteForm = document.getElementById('deleteForm');
    deleteForm.addEventListener('submit', data => {
        data.preventDefault();
        let id = `${data.target.deleteId.value}`
        console.log(`delete:  ${id}`)
        fetch(url + '/' + id, {method: 'DELETE'})
            .then((response) => console.log(response.status))
            .then(tableResult)
            .catch(e => console.error(e))
        $(".btn-close").click();
    })
}
