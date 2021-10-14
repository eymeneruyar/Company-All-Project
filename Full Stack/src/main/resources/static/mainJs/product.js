
let select_id = 0;
var success = $('#type-success');
var confirmText = $('#confirm-text');

//====================================== Category Section - Start ======================================//

//-------------------------------------- Product Category Add - Start --------------------------------------//
$('#productCategoryAdd_modalForm').submit( ( event ) => {

    event.preventDefault()

    const categoryName = $('#categoryName').val()
    const categoryDetail = $('#categoryDetails').val()
    const categoryStatus = $('#categoryStatus').val()

    const obj = {
        name: categoryName,
        status: categoryStatus,
        details: categoryDetail,
        no: codeGenerator()
    }
    //console.log("obj -> " + JSON.stringify(obj))

    if (select_id != 0) {
        // update
        obj["id"] = select_id;
    }

    $.ajax({
        url: './product/categoryAdd',
        type: 'POST',
        data: JSON.stringify(obj),
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            if (data.status == true && data.result != null) {
                Swal.fire({
                    title: 'Success!',
                    text: data.message,
                    icon: "success",
                    customClass: {
                        confirmButton: 'btn btn-primary'
                    },
                    buttonsStyling: false
                });
                setTimeout(function(){
                    $("#categoryAdd_modal").modal('hide');
                }, 2000);
                resetFormCategory()
            } else if (data.status == true && data.result == null) {
                Swal.fire({
                    title: "Warning!",
                    text: "Returned Data is Empty!",
                    icon: "warning",
                    customClass: {
                        confirmButton: 'btn btn-primary'
                    },
                    buttonsStyling: false
                });
                resetFormCategory()
            } else {
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
})
//-------------------------------------- Product Category Add - End ----------------------------------------//

//-------------------------------------- Product Category List - Start ----------------------------------------//
let globalArr = []
function dynamicPagination(totalPage, size) {
    $('#pagination_category').twbsPagination({
        totalPages: totalPage,
        visiblePages: 5,
        prev: 'Prev',
        first: 'First',
        last: 'Last',
        startPage: 1,
        onPageClick: function (event, page) {
            if($('#searchCategory').val() === ""){
                getAllProductCategoryByPage(page, size);
            }else{
                searchProductCategory(page,size,$('#searchCategory').val())
            }

            //$('#firstLast1-content').text('You are on Page ' + page);
            $('.pagination').find('li').addClass('page-item');
            $('.pagination').find('a').addClass('page-link');
        }
    });
}

function getAllProductCategoryByPage(page,showNumber){
    $.ajax({
        url:"./product/categoryList/" + showNumber + "/" + (page-1),
        type: "get",
        dataType: "json",
        contentType : 'application/json; charset=utf-8',
        success: function (data){
            //console.log(data)
            createRowData(data)
            dynamicPagination(data.totalPage,showNumber)
        },
        error: function (err){
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the product category list operation!",
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

function createRowData(data){
    let html = ``
    //console.log(data)
    for (let i = 0; i < data.result.length; i++) {
        globalArr = data.result
        const itm = data.result[i]
        html += `<tr>
                     <td>${itm.no}</td>
                     <td>${itm.name}</td>
                     <td>${itm.details}</td>
                     <td>${itm.date}</td>
                     <td>${itm.status}</td>
                     <td>
                         <div class="dropdown">
                             <button type="button" class="btn btn-sm dropdown-toggle hide-arrow"
                                 data-toggle="dropdown">
                                 <i class="fas fa-ellipsis-v"></i>
                             </button>
                             <div class="dropdown-menu">
                                 <a class="dropdown-item" href="javascript:productCategoryUpdate(${i});">
                                     <i class="mr-50 fas fa-pen"></i>
                                     <span>Edit</span>
                                 </a>
                                 <a class="dropdown-item" href="javascript:void(0);">
                                     <i class="mr-50 fas fa-ban"></i>
                                     <span>Ban</span>
                                 </a>
                                 <a class="dropdown-item" href="javascript:productCategoryDelete(${itm.categoryId});">
                                     <i class="mr-50 far fa-trash-alt"></i>
                                     <span>Delete</span>
                                 </a>
                             </div>
                         </div>
                     </td>
                 </tr>`
    }
    $("#categoryTable").html(html)
}

$('#showCategoryTableRow').change(function(){
    $('#pagination_category').twbsPagination('destroy');
    getAllProductCategoryByPage(1, parseInt($(this).val()));
    //console.log("Show number Change " + parseInt($(this).val()));
});
getAllProductCategoryByPage(1, 10);
//-------------------------------------- Product Category List - End ------------------------------------------//

//-------------------------------------- Product Category Delete - Start --------------------------------------------//
function productCategoryDelete(id){
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
                url: './product/categoryDelete/' + id,
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
                        resetFormCategory()
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
//-------------------------------------- Product Category Delete - End ----------------------------------------------//

//-------------------------------------- Product Category Update - Start ----------------------------------------------//
function productCategoryUpdate(i){
    $('#categoryAdd_modal').modal('toggle');
    const itm = globalArr[i]
    select_id  = itm.categoryId
    $('#categoryName').val(itm.name)
    $('#categoryDetails').val(itm.details)
    $('#categoryStatus').append(itm.status)
    //console.log(select_id)
}
//-------------------------------------- Product Category Update - End ------------------------------------------------//

//-------------------------------------- Product Category Search - Start ------------------------------------------------//
function searchProductCategory(page,showPageSize,searchData){

    $.ajax({
        url: './product/searchProductCategory/' + searchData + "/" + (page-1) + "/" + showPageSize,
        type: 'get',
        dataType: "json",
        contentType : 'application/json; charset=utf-8',
        success: function (data) {
            //console.log("Data result " + JSON.stringify(data))
            createRowData(data)
            dynamicPagination(data.totalPage,showPageSize)
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
}

$('#searchCategory').keyup( function (event) {
    event.preventDefault();
    const searchData = $(this).val()
    //console.log("Key " + searchData)
    if(searchData !== ""){
        $("#categoryTable > tr" ).remove()
        //$('#pagination_category').twbsPagination('destroy');
        searchProductCategory(1,$("#showCategoryTableRow").val(),searchData)
    }else{
        $('#pagination_category').twbsPagination('destroy');
        getAllProductCategoryByPage(1, $("#showCategoryTableRow").val());
    }
})
//-------------------------------------- Product Category Search - End --------------------------------------------------//

function resetFormCategory(){
    select_id = 0
    $('#categoryName').val("")
    $('#categoryDetails').val("")
    $('#categoryStatus').val("")
    getAllProductCategoryByPage(1, 10);
}

//====================================== Category Section - End ========================================//


function codeGenerator() {
    const date = new Date();
    const time = date.getTime();
    return time.toString().substring(3);
}
