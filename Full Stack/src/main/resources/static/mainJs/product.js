
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

    if (select_id !== 0) {
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
            if (data.status === true && data.result != null) {
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
            } else if (data.status === true && data.result == null) {
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
    let html = ``;
    let statusHtml = ``;
    let statusDropdownHtml = ``;
    let statusInt;
    //console.log(data)
    for (let i = 0; i < data.result.length; i++) {
        globalArr = data.result
        const itm = data.result[i]
        let status = itm.status;
        if(status === "Active"){
            statusInt = 1;
            statusHtml = `<i class="fas fa-check-circle"></i>`;
            statusDropdownHtml = `<i class="mr-50 fas fa-ban"></i>
                                  <span>Passive</span>`;
        }
        if(status === "Passive"){
            statusInt = 0;
            statusHtml = `<i class="fas fa-ban"></i>`;
            statusDropdownHtml = `<i class="fas fa-check-circle"></i>
                                  <span>Active</span>`;
        }
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
                                 <a class="dropdown-item" href="javascript:changeProductCategoryStatus(${itm.categoryId}, ${statusInt});">
                                    `+statusDropdownHtml+`
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

//-------------------------------------- Product Category Status - Start ----------------------------------------------//
function changeProductCategoryStatus(id, statusInt) {

    let url;
    let text;
    let confirmButtonText;
    let successTitle;
    if(statusInt === 1){ //Passive
        url = '/product/changeCategoryStatus/' + id + "/passive";
        text = "This product category will be passive and won't be able to do any operations!";
        confirmButtonText = "Ok";
        successTitle = "Passive!"
    }else if(statusInt === 0){ //Active
        url = '/product/changeCategoryStatus/' + id + "/active";
        text = "This product category will be active and will be able to do any operations!";
        confirmButtonText = "Ok";
        successTitle = "Active!"
    }else{
        return;
    }

    Swal.fire({
        title: 'Are you sure?',
        text: text,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: confirmButtonText,
        customClass: {
            confirmButton: 'btn btn-primary',
            cancelButton: 'btn btn-outline-danger ml-1'
        },
        buttonsStyling: false
    }).then(function (result) {
        console.log(url)
        if (result.value) {
            $.ajax({
                url: url,
                type: 'GET',
                dataType: 'json',
                success: function (data) {
                    if( data.status === true){
                        Swal.fire({
                            icon: 'success',
                            title: successTitle,
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
                        text: "An error occurred during the operation",
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
//-------------------------------------- Product Category Status - End ------------------------------------------------//

function resetFormCategory(){
    select_id = 0
    $('#categoryName').val("")
    $('#categoryDetails').val("")
    $('#categoryStatus').val("")
    getAllProductCategoryByPage(1, $('#showCategoryTableRow').val());
}

function allProductCategoryList(){
    $.ajax({
        url: './product/categoryList',
        type: 'get',
        dataType: "json",
        contentType : 'application/json; charset=utf-8',
        success: function (data) {
            category = data.result;
            //console.log("Product Categories : ", category)
            productCategoryOption(category)
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
allProductCategoryList()

function productCategoryOption(data) {
    data.forEach(item => {
        $('#productCategories').append('<option value="' + item.id + '">' + item.name + '</option>');
    })
}
//====================================== Category Section - End ========================================//

//====================================== Product Section - Start ========================================//

//-------------------------------------- Product Add - Start --------------------------------------//
$("#productAdd_modalForm").submit( (event) => {

    event.preventDefault()

    const categories = $('#productCategories').val()
    const productName = $('#productName').val()
    const productDescription = $('#productDescription').val()
    const productPrice = $('#productPrice').val()
    const productDetails = $('#productDetails').val()
    const campaStatus = $('#campaignStatus').val()
    const campaName = $('#campaignName').val()
    const campaDetails = $('#campaignDetails').val()

    let categoryList = []

    for(let i= 0 ; i<categories.length; i++){
        const categoryObj = {
            id: categories[i]
        }
        categoryList.push(categoryObj)
    }


    const obj = {
        productCategories: categoryList,
        name: productName,
        description: productDescription,
        price: productPrice,
        details: productDetails,
        status: "Available",
        no: codeGenerator(),
        campaignName: campaName,
        campaignDetails: campaDetails,
        campaignStatus: campaStatus

    }

    console.log(obj)

    if ( select_id != 0 ) {
        // update
        obj["id"] = select_id;
    }

    $.ajax({
        url: './product/add',
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
                setTimeout(function(){
                    $("#productAdd_modal").modal('hide');
                }, 2000);
                resetFormProduct()
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
                resetFormProduct()
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
            console.log(data)
            resetFormProduct()
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
//-------------------------------------- Product Add - End ----------------------------------------//

//-------------------------------------- Product List - Start ----------------------------------------//
let globalProductArr = []
function dynamicPaginationProduct(totalPage, size) {
    $('#pagination_product').twbsPagination({
        totalPages: totalPage,
        visiblePages: 5,
        prev: 'Prev',
        first: 'First',
        last: 'Last',
        startPage: 1,
        onPageClick: function (event, page) {
            if($('#searchProduct').val() === ""){
                getAllProductsByPage(page, size);
            }else{
                searchProduct(page,size,$('#searchProduct').val())
            }
            //$('#firstLast1-content').text('You are on Page ' + page);
            $('.pagination').find('li').addClass('page-item');
            $('.pagination').find('a').addClass('page-link');
        }
    });
}

function getAllProductsByPage(page,showNumber){
    $.ajax({
        url:"./product/list/" + showNumber + "/" + (page-1),
        type: "get",
        dataType: "json",
        contentType : 'application/json; charset=utf-8',
        success: function (data){
            createRowDataProduct(data)
            dynamicPaginationProduct(data.totalPage,showNumber)
        },
        error: function (err){
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the product list operation!",
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

function createRowDataProduct(data){
    let html = ``;
    let statusHtml = ``;
    let statusDropdownHtml = ``;
    let statusInt;
    for (let i = 0; i < data.result.length; i++) {
        globalProductArr = data.result
        const itm = data.result[i]
        let status = itm.status;
        let price = priceFormatter(itm.price)
        if(status === "Available"){
            statusInt = 1;
            statusHtml = `<i class="fas fa-check-circle"></i>`;
            statusDropdownHtml = `<i class="mr-50 fas fa-ban"></i>
                                  <span>Ban</span>`;
        }
        if(status === "Unavailable"){
            statusInt = 0;
            statusHtml = `<i class="fas fa-ban"></i>`;
            statusDropdownHtml = `<i class="fas fa-check-circle"></i>
                                  <span>Unban</span>`;
        }
        html += `<tr>
                     <td>${itm.no}</td>
                     <td>${itm.name}</td>
                     <td>${itm.description}</td>
                     <td>${itm.no}</td>
                     <td>${price}</td>
                     <td>${itm.date}</td>
                     <td>${itm.status}</td>
                     <td>
                         <div class="dropdown">
                             <button type="button" class="btn btn-sm dropdown-toggle hide-arrow"
                                 data-toggle="dropdown">
                                 <i class="fas fa-ellipsis-v"></i>
                             </button>
                             <div class="dropdown-menu">
                                <a class="dropdown-item" href="javascript:productAddImages(${i});">
                                     <i class="mr-50 far fa-images"></i>
                                     <span>Add Images</span>
                                 </a>
                                 <a class="dropdown-item" type="button" href="javascript:productDetail(${i});" >
                                      <i class="mr-50 fas fa-info-circle"></i>
                                      <span>Detail</span>
                                 </a>
                                 <a class="dropdown-item" href="javascript:productUpdate(${i});">
                                     <i class="mr-50 fas fa-pen"></i>
                                     <span>Edit</span>
                                 </a>
                                 <a class="dropdown-item" href="javascript:changeProductStatus(${itm.productId}, ${statusInt});">
                                    `+statusDropdownHtml+`
                                </a>
                                 <a class="dropdown-item" href="javascript:productDelete(${itm.productId});">
                                     <i class="mr-50 far fa-trash-alt"></i>
                                     <span>Delete</span>
                                 </a>
                             </div>
                         </div>
                     </td>
                 </tr>`
    }
    $("#productTable").html(html)
}

$('#showProductTableRow').change(function(){
    $('#pagination_product').twbsPagination('destroy');
    getAllProductsByPage(1, parseInt($(this).val()));
    //console.log("Show number Change " + parseInt($(this).val()));
});
getAllProductsByPage(1, 10);
//-------------------------------------- Product List - End ------------------------------------------//

//-------------------------------------- Product Delete - Start ------------------------------------------//
function productDelete(id){
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
                url: './product/delete/' + id,
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
                        resetFormProduct()
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
//-------------------------------------- Product Delete - End --------------------------------------------//

//-------------------------------------- Product Update - End --------------------------------------------//
function productUpdate(i){
    $('#productAdd_modal').modal('toggle');
    const itm = globalProductArr[i]
    select_id = itm.productId
    $.ajax({
        url:"./product/detail/" + itm.productId,
        type: "get",
        dataType: "json",
        contentType : 'application/json; charset=utf-8',
        success: function (data){
            console.log(data.result)
            for(let i = 0; i < data.result.productCategories.length; i++){
                $('#productCategories').val(data.result.productCategories[i].id).trigger('change')
            }
            $('#productName').val(data.result.name)
            $('#productDescription').val(data.result.description)
            $('#productPrice').val(data.result.price)
            $('#productDetails').val(data.result.details)
            if(data.result.campaignStatus === "Yes"){
                $('#campaignStatus').val(data.result.campaignStatus).trigger('change')
                $('#campaignName').val(data.result.campaignName)
                $('#campaignDetails').val(data.result.campaignDetails)
            }else{
                $('#campaignStatus').val(data.result.campaignStatus).trigger('change')
            }

        },
        error: function (err){
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the product list operation!",
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

function productAddImages(i){
    $('#productImage').modal('toggle');
    const itm = globalProductArr[i]
    $("#modalTitle").text(itm.name + "  " + itm.description + " - " + itm.productId)
    $.ajax({
        url: './product/chosenId/' + itm.productId,
        type: 'get',
        dataType: "json",
        contentType : 'application/json; charset=utf-8',
        success: function (data) {
            console.log(data)
        },
        error: function (err) {
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the chosen id operation!",
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

//-------------------------------------- Product Update - End --------------------------------------------//

//-------------------------------------- Product Details - Start --------------------------------------------//
// progress
var mySwiper3 = new Swiper('.swiper-progress', {
    pagination: {
        el: '.swiper-pagination',
        type: 'progressbar'
    },
    navigation: {
        nextEl: '.swiper-button-next',
        prevEl: '.swiper-button-prev'
    }
});
// navigation
var mySwiper1 = new Swiper('.swiper-navigations', {
    navigation: {
        nextEl: '.swiper-button-next',
        prevEl: '.swiper-button-prev'
    }
});
function productDetail(i){
    $('#productDetail').modal('toggle')
    $('#imagesChose').empty()
    const itm = globalProductArr[i]
    let html = ``
    var fileName
    $.ajax({
        url: './product/chosenId/' + itm.productId,
        type: 'get',
        dataType: "json",
        contentType : 'application/json; charset=utf-8',
        success: function (data) {
            console.log(data)
            $.ajax({
                url:"./product/detail/" + itm.productId,
                type: "get",
                dataType: "json",
                contentType : 'application/json; charset=utf-8',
                success: function (data){
                    for (let j = 0; j < data.result.fileName.length; j++) {
                        fileName = data.result.fileName[j]
                        html += `<div class="swiper-slide">
                             <img src="/productDetail/get_image/id=${itm.productId}name=${fileName}" class="img-fluid" alt="banner" />

                         </div>`

                        $('#imagesChose').append('<option value="' + fileName + '">Image -  ' + j + '</option>'); //Ürüne ait image'leri silme aksiyonu select2 bölümüne eklenmesi

                    }$("#imageSlide").html(html)
                    $("#title").text(itm.no + " - " + itm.name + "  " + itm.description)
                    $("#priceDetail").text(priceFormatter(itm.price)+" TL")
                    $("#dateDetail").text(itm.date)
                    $("#statusDetail").text(itm.status)
                    $("#details").text(data.result.details)
                    if(data.result.campaignStatus === "Yes"){
                        let campaign1 = `<label style="color: black" class="form-label">Campaign Status</label>
                                <div   id="campaignStatusDetail" class="form-text"></div>`
                        let campaign2 = `<label style="color: black" class="form-label">Campaign Name</label>
                            <div   id="campaignNameDetail" class="form-text"></div>`
                        let campaign3 = `<label style="color: black" class="form-label mt-1" >Campaign Details</label>
                                <div   id="campaignDetailsInfo" class="form-text"></div>`
                        $('#campaignStatusDetail').text(data.result.campaignStatus)
                        $('#campaignNameDetail').text(data.result.campaignName)
                        $('#campaignDetailsInfo').text(data.result.campaignDetails)

                        $("#campaign-1").html(campaign1)
                        $("#campaign-2").html(campaign2)
                        $("#campaign-3").html(campaign3)
                    }

                },
                error: function (err){
                    Swal.fire({
                        title: "Error!",
                        text: "An error occurred during the product list operation!",
                        icon: "error",
                        customClass: {
                            confirmButton: 'btn btn-primary'
                        },
                        buttonsStyling: false
                    });
                    console.log(err)
                }
            })
        },
        error: function (err) {
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the chosen id operation!",
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
//-------------------------------------- Product Details - End ----------------------------------------------//

//-------------------------------------- Product Image Delete - Start ----------------------------------------------//
function deleteImage(){

    const chosenImages = $("#imagesChose").val()

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
                url: '/product/chosenImages/delete/images=' + chosenImages,
                type: 'DELETE',
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                success: function (data) {
                    if( chosenImages.length > 0){
                        Swal.fire({
                            icon: 'success',
                            title: "Deleted!",
                            text: data.message,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        });
                        $('#imagesChose').empty()
                        setTimeout(function(){
                            $("#productDetail").modal('hide');
                        }, 2000);
                    }else{
                        Swal.fire({
                            icon: 'warning',
                            title: "Warning",
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
//-------------------------------------- Product Image Delete - End ------------------------------------------------//

//-------------------------------------- Product Search - Start --------------------------------------------//
function searchProduct(page,showPageSize,searchData){

    $.ajax({
        url: './product/search/' + searchData + "/" + (page-1) + "/" + showPageSize,
        type: 'get',
        dataType: "json",
        contentType : 'application/json; charset=utf-8',
        success: function (data) {
            createRowDataProduct(data)
            dynamicPaginationProduct(data.totalPage,showPageSize)
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

$('#searchProduct').keyup( function (event) {
    event.preventDefault();
    const searchData = $(this).val()
    if(searchData !== ""){
        $("#productTable > tr" ).remove()
        //$('#content_pagination').twbsPagination('destroy');
        searchProduct(1,$("#showProductTableRow").val(),searchData)
    }else{
        $('#pagination_product').twbsPagination('destroy');
        getAllProductsByPage(1, $("#showProductTableRow").val());
    }
})
//-------------------------------------- Product Search - End ----------------------------------------------//

//-------------------------------------- Product Status - Start ----------------------------------------------//
function changeProductStatus(id, statusInt) {

    let url;
    let text;
    let confirmButtonText;
    let successTitle;
    if(statusInt === 1){ //Unavailable
        url = '/product/changeStatus/' + id + "/passive";
        text = "This product will be banned and won't be able to do any operations!";
        confirmButtonText = "Ban";
        successTitle = "Banned!"
    }else if(statusInt === 0){ //Available
        url = '/product/changeStatus/' + id + "/active";
        text = "This product will be unbanned and will be able to do any operations!";
        confirmButtonText = "Unban";
        successTitle = "Unbanned!"
    }else{
        return;
    }

    Swal.fire({
        title: 'Are you sure?',
        text: text,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: confirmButtonText,
        customClass: {
            confirmButton: 'btn btn-primary',
            cancelButton: 'btn btn-outline-danger ml-1'
        },
        buttonsStyling: false
    }).then(function (result) {
        console.log(url)
        if (result.value) {
            $.ajax({
                url: url,
                type: 'GET',
                dataType: 'json',
                success: function (data) {
                    if( data.status === true){
                        Swal.fire({
                            icon: 'success',
                            title: successTitle,
                            text: data.message,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        });
                        getAllProductsByPage(1, $('#showProductTableRow').val());
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
                        text: "An error occurred during the operation",
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
//-------------------------------------- Product Status - End ------------------------------------------------//

//====================================== Product Section - End ==========================================//

$('#campaignStatus').change(function () {
    //console.log($(this).val())
    if ($(this).val() !== "Yes"){
        $('#campaignName').attr("disabled", true);
        $('#campaignDetails').attr("disabled", true);
    }else{
        $('#campaignName').attr("disabled", false);
        $('#campaignDetails').attr("disabled", false);
    }
});

function resetFormProduct(){
    select_id = 0
    $('#productCategories').empty()
    $('#productName').val("")
    $('#productDescription').val("")
    $('#productPrice').val("")
    $('#productDetails').val("")
    $('#campaignName').val("")
    $('#campaignDetails').val("")
    getAllProductsByPage(1, 10);
    allProductCategoryList()
}

function priceFormatter(price){
    var formattedPrice = (price).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,')
    return formattedPrice
}

function codeGenerator() {
    const date = new Date();
    const time = date.getTime();
    return time.toString().substring(3);
}
