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

function addUser() {
    event.preventDefault();
    let select = $("#roles").val();
    let roles = [];
    for (let i = 0; i < select.length; i++) {
        roles.push({
            id: select[i] === 'ADMIN' ? 1 : 2,
            name: select[i],
        });
    }
    let user = {
        username: $('#firstName').val(),
        lastname: $('#lastName').val(),
        age: $('#age').val(),
        email: $('#email').val(),
        password: $('#password').val(),
        roles: roles,
    };
    console.log(JSON.stringify(user));
    fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user),
    })
        .then((response) => console.log(response.status))
        .then(tableResult)
        .then(clearForm)
        .catch(e => console.error(e))

    $(".adm-btn").click();
}

function clearForm() {
    $('#firstName').empty().val('')
    $('#lastName').empty().val('')
    $('#age').empty().val('')
    $('#email').empty().val('')
    $('#password').empty().val('')
}

// new user
// const newUserForm = document.getElementById('newUserForm');
// newUserForm.addEventListener('submit', data => {
//     data.preventDefault();
//     const select = data.target.roles;
//     let len = select.options.length;
//     let roles = [];
//     for (let i = 0; i < len; i++) {
//         if (select.options[i].selected === true) {
//             roles.push({
//                 id: select.options[i].value,
//                 name: select.options[i].text,
//             });
//         }
//     }
//     let user = {
//         username: `${data.target.firstName.value}`,
//         lastname: `${data.target.lastName.value}`,
//         age: `${data.target.age.value}`,
//         email: `${data.target.email.value}`,
//         password: `${data.target.password.value}`,
//         roles: roles
//     };
//     console.log(JSON.stringify(user));
//     fetch(url, {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json; charset=UTF-8'
//         },
//         body: JSON.stringify(user)
//     })
//         .then(response => console.log(response.status))
//         .then(() => {
//             $('#table-all-users').empty();
//             tableResult()
//         })
//         .catch(e => console.error(e));
//     $(".adm-btn").click();
// })

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
        event.preventDefault();
        let select = $("#editRole").val();
        console.log(select)
        let roles = [];
        for (let i = 0; i < select.length; i++) {
                roles.push({
                    id: select[i] === 'ADMIN' ? 1 : 2,
                    name: select[i],
                });
        }
        let user = {
            id: $('#editId').val(),
            username: $('#editFirstName').val(),
            lastname: $('#editLastName').val(),
            age: $('#editAge').val(),
            email: $('#editEmail').val(),
            password: $('#editPassword').val(),
            roles: roles,
        };
        console.log(JSON.stringify(user));
        fetch(url, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(user),
        })
            .then((response) => console.log(response.status))
            .then(tableResult)
            .catch(e => console.error(e))
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
        event.preventDefault();
        let id = $("#deleteId").val()
        console.log(`delete:  ${id}`)
        fetch(url + '/' + id, {method: 'DELETE'})
            .then((response) => console.log(response.status))
            .then(tableResult)
            .catch(e => console.error(e))
        $(".btn-close").click();
}
