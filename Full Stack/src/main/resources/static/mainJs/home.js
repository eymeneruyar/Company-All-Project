function sendContactMail(contactMailObj) {

    $.ajax({
        url: '/home/contact_email',
        type: 'POST',
        data: JSON.stringify(contactMailObj),
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
                });
                $('#contact_email_form').trigger("reset");
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

$('#contact_email_form').submit((event) => {
    event.preventDefault();
    const name = $("#name").val();
    const subject = $("#subject").val();
    const message = $("#message").val();

    const contactMailObj = {
        name: name,
        subject: subject,
        message: message
    }
    sendContactMail(contactMailObj);
});