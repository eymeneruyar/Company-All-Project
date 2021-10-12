//-------------------------------------- Quill Editor - Start --------------------------------------//
var options = {
    placeholder: 'Enter the contents details...',
    theme: 'snow'
};
let contentDetails
var editor = new Quill('#quillEditor', options);

editor.on('text-change', function() {
    var text = editor.getText();
    contentDetails = text;
    //console.log(text)
    //console.log(contentDetails)
});

//-------------------------------------- Quill Editor - End ----------------------------------------//

let select_id = 0;
var success = $('#type-success');

//-------------------------------------- Contents Add - Start --------------------------------------//
if (success.length) {
    success.on('click', function () {

        const contentTitle = $('#title').val()
        const contentDescription = $('#description').val()
        const contentStatus = $('#status').val()
        var editorText = ""

        if(contentDetails != null && contentDetails != " "){
            editorText = contentDetails.split("\n")[0] //Quill editör'den gelen yazı sonunda \n ifadesini ayırmak için kullanıldı.
        }

        const obj = {
            title: contentTitle,
            description: contentDescription,
            status: contentStatus,
            details: editorText
        }
        console.log("obj -> " + JSON.stringify(obj))

        if ( select_id != 0 ) {
            // update
            obj["id"] = select_id;
        }

        $.ajax({
            url: './contents/add',
            type: 'POST',
            data: JSON.stringify(obj),
            dataType: 'json',
            contentType : 'application/json; charset=utf-8',
            success: function (data) {
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
                 console.log(data)
                 //formReset()
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
                console.log(err)
            }
        })

    });
}
//-------------------------------------- Contents Add - End ----------------------------------------//

//-------------------------------------- Contents List - Start ----------------------------------------//

(function (window, document, $) {
    'use strict';

    if ($(window).width() < 768) {
        dynamicPagination(2);
    } else {
        dynamicPagination(5);
    }

    function dynamicPagination(visiblePages) {

        let totalPage = 0

        var showNumber = $('#showTableRow').val()

        $('#showTableRow').change(function(){
            console.log("Show number New " + $(this).val())
            showNumber = $(this).val()
        })
        console.log("Show number " + showNumber)

        $.ajax({
            url:"./contents/totalPageValue/" + showNumber,
            type: "get",
            contentType : 'application/json; charset=utf-8',
            success: function (data){

                totalPage = data
                console.log("Total page -> " + totalPage)

                $('.firstLast1-links').twbsPagination({
                    totalPages: totalPage,
                    visiblePages: visiblePages,
                    prev: 'Prev',
                    first: 'First',
                    last: 'Last',
                    startPage: 1,
                    onPageClick: function (event, page) {

                        function listOfContents(){
                            $.ajax({
                                url:"./contents/list/" + showNumber + "/" + (page-1),
                                type: "get",
                                dataType: "json",
                                contentType : 'application/json; charset=utf-8',
                                success: function (data){
                                    //createRowData(data)
                                    console.log("Pagination Data -> " + JSON.stringify(data.result))
                                },
                                error: function (err){
                                    console.log(err)
                                }
                            })
                        }
                        listOfContents()
                        //$('#firstLast1-content').text('You are on Page ' + page);
                        $('.pagination').find('li').addClass('page-item');
                        $('.pagination').find('a').addClass('page-link');
                    }
                });
                //createRow(data)
            },
            error: function (err){
                console.log(err)
            }
        })


    }
})(window, document, jQuery);

//-------------------------------------- Contents List - End ------------------------------------------//

//-------------------------------------- Contents Create Row - Start ------------------------------------------//
let globalArr = []
function createRowData(data){
    let html = ``
    for (let i = 0; i < data.length; i++) {
        globalArr = data
        const itm = data[i]
        html += ``
    }
    $("#").html(html)
}
//-------------------------------------- Contents Create Row - End --------------------------------------------//

function fncShow(){
    var tableRowNumber = $('#showTableRow').val()
    console.log("Show number " + tableRowNumber)
    return tableRowNumber
    var x = $('#showTableRow').val()
    console.log("Show number X " + x)
}





