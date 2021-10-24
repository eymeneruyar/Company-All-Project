//------------------------------ Company Form - Start ------------------------------//
$("#companyInfo_form").submit( (event) => {

    event.preventDefault()

    const companyName = $("#settings-companyName").val()
    const companyAddress = $("#settings-companyAddress").val()
    const companySector = $("#settings-companySector").val()
    const companyPhone = $("#settings-companyPhone").val()

    const obj = {
        companyName:companyName,
        companyAddress:companyAddress,
        companySector:companySector,
        companyPhone:companyPhone
    }

    Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes, changes saved!',
        customClass: {
            confirmButton: 'btn btn-primary',
            cancelButton: 'btn btn-outline-danger ml-1'
        },
        buttonsStyling: false
    }).then(function (result) {
        if (result.value) {
            $.ajax({
                url: './settings/updateCompany',
                type: 'POST',
                data: JSON.stringify(obj),
                dataType: 'json',
                contentType : 'application/json; charset=utf-8',
                success: function (data){
                    //console.log(data)
                    if(data.status === true && data.result != null){
                        Swal.fire({
                            title: 'Success!',
                            text: data.message,
                            icon: "success",
                            customClass: {
                                confirmButton: 'btn btn-primary'
                            },
                            buttonsStyling: false
                        });
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
        }
    });



})
//------------------------------ Company Form - End --------------------------------//

//------------------------------ Company Change Password Form - Start --------------------------------//
$("#changePassword_form").submit( (event) => {

    event.preventDefault()

    const oldPassword = $("#account-old-password").val()
    const newPassword = $("#account-new-password").val()
    const reNewPassword = $("#account-retype-new-password").val()

    const obj = {
        oldPassword:oldPassword,
        newPassword:newPassword,
        reNewPassword:reNewPassword
    }

    Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes, changes saved!',
        customClass: {
            confirmButton: 'btn btn-primary',
            cancelButton: 'btn btn-outline-danger ml-1'
        },
        buttonsStyling: false
    }).then(function (result) {
        if (result.value) {
            $.ajax({
                url: './settings/changePassword',
                type: 'POST',
                data: JSON.stringify(obj),
                dataType: 'json',
                contentType : 'application/json; charset=utf-8',
                success: function (data){
                    //console.log(data)
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
                        resetPasswordForm()
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
                        resetPasswordForm()
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
        }
    });

})

function resetPasswordForm(){
    $("#changePassword_form").trigger('reset')
}
//------------------------------ Company Change Password Form - End ----------------------------------//

//------------------------------ User Information Form - Start ----------------------------------//
$("#userInfo_form").submit( (event) => {

    event.preventDefault()

    const name = $("#settings-name").val()
    const surname = $("#settings-surname").val()
    const email = $("#settings-Email").val()
    const birthdate = $("#settings-birtDate").val()
    const bio = $("#settings-bio").val()

    const obj = {
        name:name,
        surname:surname,
        email:email,
        bio:bio,
        birthday:birthdate
    }

    Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes, changes saved!',
        customClass: {
            confirmButton: 'btn btn-primary',
            cancelButton: 'btn btn-outline-danger ml-1'
        },
        buttonsStyling: false
    }).then(function (result) {
        if (result.value) {
            $.ajax({
                url: './settings/userInfo',
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
        }
    });

})
//------------------------------ User Information Form - End ------------------------------------//

function getProfileImage() {
    $.ajax({
        url: '/settings/get_profile_image',
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            setProfileImage(data.result);
        },
        error: function (err) {
            console.log(err)
        }
    })
}

function imageUpload(formData) {
    $.ajax({
        url: '/settings/profile_image_upload',
        type: "POST",
        headers: {'IsAjax': 'true'},
        dataType: "json",
        processData: false,
        contentType: false,
        data: formData,
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
                $('#image_upload_form').trigger("reset");
                getProfileImage(data.result);
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
        error: function () {
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the operation!",
                icon: "error",
                customClass: {
                    confirmButton: 'btn btn-primary'
                },
                buttonsStyling: false
            });
        }
    });
}

File.prototype.convertToBase64 = function(callback){

    var FR= new FileReader();
    FR.onload = function(e) {
        callback(e.target.result)
    };
    FR.readAsDataURL(this);
}

function setProfileImage(bytes) {
    $("#profile_image").attr("src", "data:image/*;base64," + bytes);
}

$('#image_upload_form').submit((event) => {
    event.preventDefault();
    let formData = new FormData();
    let image = document.getElementById("profile_image_input");
    if(image.files[0] !== undefined){
        formData.append("image", image.files[0]);
        imageUpload(formData);
        $("#profile_image_warning").text("");
    }
});

$('#profile_image_input').change((event) => {
    event.preventDefault();
    let image_input = document.getElementById("profile_image_input");
    const image = image_input.files[0];
    if(image !== undefined){
        image.convertToBase64(function (base64){
            $("#profile_image").attr("src", base64);
            $("#profile_image_warning").text("*");

        })
    }
});

getProfileImage();














