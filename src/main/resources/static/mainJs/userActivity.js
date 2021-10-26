function fncAccountActivity (){

    $.ajax({
        url:"http://localhost:8080/api/userActivity",
        success: function (data){
            //console.log(data)

            let role = data.result.role

            $("#nameAndSurname").text(data.result.name + ' ' + data.result.surname)

            //Rol ifadeleri düzenlendi.
            if(role === "ROLE_ADMIN"){
                $("#role").text(role.replace("ROLE_ADMIN","Admin"))
            }

            //template 'de bulunan avatar resim kısmı
            $.ajax({
                url: '/settings/get_profile_image',
                type: 'GET',
                dataType: 'Json',
                success: function (data) {
                    $("#imageProfile").attr("src", "data:image/*;base64," + data.result);
                },
                error: function (err) {
                    console.log(err)
                }
            })

        },
        error: function (err){
            console.log(err)
        }
    })

}
fncAccountActivity ()


