$('#forgot_password_form').submit((event) => {
    event.preventDefault();
    const email = $('#email').val();
    $.ajax({
        url: '/forgotPassword/send_email/email=' + email,
        type: 'GET',
        dataType: 'JSON',
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
});