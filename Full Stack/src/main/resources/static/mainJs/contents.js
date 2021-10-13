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
var confirmText = $('#confirm-text');

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

let globalArr = []

var showNumber = $('#showTableRow').val()

dynamicPagination(5,showNumber);

function dynamicPagination(visiblePages,showData) {

    var totalPage = 0

    console.log("Show number  " + showData)

    $.ajax({
        url:"./contents/totalPageValue/" + showData,
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

                        $.ajax({
                            url:"./contents/list/" + showData + "/" + (page-1),
                            type: "get",
                            dataType: "json",
                            contentType : 'application/json; charset=utf-8',
                            success: function (data){
                                createRowData(data)
                            },
                            error: function (err){
                                console.log(err)
                            }
                        })

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
$('#showTableRow').change(function(){
    showNumber = $(this).val()
    dynamicPagination(5,showNumber);
    console.log("Show number Change " + $(this).val())
})

//-------------------------------------- Contents List - End ------------------------------------------//

//-------------------------------------- Contents Create Row - Start ------------------------------------------//
function createRowData(data){
    let html = ``
    for (let i = 0; i < data.result.length; i++) {
        globalArr = data.result
        const itm = data.result[i]
        html += `<tr>
                      <td>${itm.no}</td>
                      <td>${itm.title}</td>
                      <td>${itm.description}</td>
                      <td>${itm.status}</td>
                      <td>${itm.date}</td>
                      <td>
                          <div class="dropdown">
                              <button type="button" class="btn btn-sm dropdown-toggle hide-arrow" data-toggle="dropdown">
                                  <i data-feather="more-vertical"></i>
                              </button>
                              <div class="dropdown-menu">
                                  <a class="dropdown-item" href="javascript:void(0);">
                                      <i class="mr-50 fas fa-pen"></i>
                                      <span>Edit</span>
                                  </a>
                                  <a class="dropdown-item" href="javascript:void(0);">
                                      <i class="mr-50 fas fa-ban"></i>
                                      <span>Ban</span>
                                  </a>
                                  <a class="dropdown-item" type="button" id="confirm-text" href="javascript:contentDelete(${itm.contentsId})">
                                      <i class="mr-50 far fa-trash-alt"></i>
                                      <span>Delete</span>
                                  </a>
                              </div>
                          </div>
                      </td>
               </tr>`
    }
    $("#contentTable").html(html)
}
//-------------------------------------- Contents Create Row - End --------------------------------------------//

//-------------------------------------- Contents Delete - Start --------------------------------------------//
function contentDelete(id){
    Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes, delete it!',
        customClass: {
            confirmButton: 'btn btn-primary',
            cancelButton: 'btn btn-outline-danger ml-1'
        },
        buttonsStyling: false
    }).then(function (result) {
        if (result.value) {
            $.ajax({
                url: './contents/delete/' + id,
                type: 'DELETE',
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                success: function (data) {
                    if( id != null){
                        Swal.fire({
                            icon: 'success',
                            title: "Deleted!",
                            text: data.message,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        });
                    }else{
                        Swal.fire({
                            icon: 'error',
                            title: "Error",
                            text: data.message,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        });
                    }
                },
                error: function (err) {
                    Swal.fire({
                        icon: 'error',
                        title: "Error",
                        text: "An error occurred during the delete operation",
                        customClass: {
                            confirmButton: 'btn btn-success'
                        }
                    });
                    console.log(err)
                }
            })
        }
    });
}
//-------------------------------------- Contents Delete - End ----------------------------------------------//

//-------------------------------------- Contents Search - Start --------------------------------------------//
$('#searchContent').keyup( function (event) {

    event.preventDefault();

    var totalPage = 0
    let searchData = $("#searchContent").val()

    $.ajax({
        url:"./contents/totalPageValue/" + showNumber,
        type: "get",
        contentType : 'application/json; charset=utf-8',
        success: function (data){

            totalPage = data
            console.log("Total page search section -> " + totalPage)

            $('.firstLast1-links').twbsPagination({
                totalPages: totalPage,
                visiblePages: 3,
                prev: 'Prev',
                first: 'First',
                last: 'Last',
                startPage: 1,
                onPageClick: function (event, page) {

                    $.ajax({
                        url: './contents/search/' + searchData + "/" + page + "/" + showNumber,
                        type: 'get',
                        dataType: "json",
                        contentType : 'application/json; charset=utf-8',
                        success: function (data) {

                            createRowData(data)
                            console.log("Search data " + JSON.stringify(data))

                        },
                        error: function (err) {
                            console.log(err)
                            alert("İşlem sırısında bir hata oluştu!");
                        }
                    })

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



})
//-------------------------------------- Contents Search - End ----------------------------------------------//

function fncShow(){
    var tableRowNumber = $('#showTableRow').val()
    console.log("Show number " + tableRowNumber)
    return tableRowNumber
    var x = $('#showTableRow').val()
    console.log("Show number X " + x)
}





