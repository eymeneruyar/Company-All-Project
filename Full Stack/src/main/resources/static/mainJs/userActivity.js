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
            htmlNavbarImage = `<img class="round" src="/uploadImages/_profileImages/${data.result.image}" alt="avatar" height="40" width="40">`
            $("#imageProfile").html(htmlNavbarImage)


        },
        error: function (err){
            console.log(err)
        }
    })

}
fncAccountActivity ()