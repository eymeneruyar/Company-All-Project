//--------------------------------------------- Register Form - Start ---------------------------------------------//
$("#registerForm").submit( (event) => {

    event.preventDefault()

    const companyName = $("#companyName").val()
    const companyAddress = $("#companyAddress").val()
    const companySector = $("#companySector").val()
    const companyPhone = $("#companyPhone").val()
    const adminName = $("#adminName").val()
    const adminSurname = $("#adminSurname").val()
    const adminEmail = $("#adminEmail").val()
    const adminPassword = $("#adminPassword").val()

    const obj = {
        companyName:companyName,
        companyAddress:companyAddress,
        companySector:companySector,
        companyPhone:companyPhone,
        name:adminName,
        surname:adminSurname,
        email:adminEmail,
        password:adminPassword,
        no:codeGenerator()
    }

    $.ajax({
        url: './register/save',
        type: 'POST',
        data: JSON.stringify(obj),
        dataType: 'json',
        contentType : 'application/json; charset=utf-8',
        success: function (data){
            console.log(data)
            if(data.status == true && data.result != null){
                Swal.fire({
                    title: 'Success!',
                    text: data.message,
                    icon: "success",
                    customClass: {
                        confirmButton: 'btn btn-primary'
                    },
                    buttonsStyling: false
                });
                resetForm()
            }else if(data.status == true && data.result == null){
                Swal.fire({
                    title: "Warning!",
                    text: "Returned Data is Empty!",
                    icon: "warning",
                    customClass: {
                        confirmButton: 'btn btn-primary'
                    },
                    buttonsStyling: false
                });
                resetForm()
            }else{
                if (!jQuery.isEmptyObject(data.errors)) {
                    console.log(data.errors)
                    Swal.fire({
                        title: 'Error!',
                        text: data.errors[0].fieldMessage,
                        icon: "error",
                        customClass: {
                            confirmButton: 'btn btn-primary'
                        },
                        buttonsStyling: false
                    });
                }else{
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
            }
        },
        error: function (err){
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the operation!",
                icon: "error",
                customClass: {
                    confirmButton: 'btn btn-primary'
                },
                buttonsStyling: false
            });
            console.log(err)
        }
    })

})
//--------------------------------------------- Register Form - End -----------------------------------------------//
function codeGenerator() {
    const date = new Date();
    const time = date.getTime();
    return time.toString().substring(3);
}
function resetForm(){
    $("#registerForm").trigger('reset')
}







