function save(passwordObj) {
    $.ajax({
        url: '/newPassword/setPassword',
        type: 'POST',
        data: JSON.stringify(passwordObj),
        dataType: 'JSON',
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            if (data.status === true) {
                Swal.fire({
                    title: 'Success!',
                    text: data.message,
                    icon: "success",
                    customClass: {
                        confirmButton: 'btn btn-primary'
                    },
                    buttonsStyling: false
                }).then(function (result) {
                    if (result.value) {
                        Swal.fire({
                            title: 'Redirecting...!',
                            text: "You will be redirecting to the login page in 3 seconds.",
                            icon: "success",
                            buttonsStyling: false
                        })
                        setTimeout(function () {
                            window.location.href = '/login';
                        }, 3000);
                    }
                    });
            } else {
                Swal.fire({
                    title: 'Error!',
                    text: data.message,
                    icon: "error",
                    customClass: {
                        confirmButton: 'btn btn-primary'
                    },
                    buttonsStyling: false
                });
            }
        },
        error: function (err) {
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the operation!",
                icon: "error",
                customClass: {
                    confirmButton: 'btn btn-primary'
                },
                buttonsStyling: false
            });
            console.log(err);
        }
    })
}

$('#new_password_form').submit((event) => {
    event.preventDefault();
    const passwordObj = {
        oldPassword: "",
        newPassword: $('#new_password').val(),
        reNewPassword: $('#new_password_check').val()
    }
    console.log(passwordObj);
    save(passwordObj);

});
