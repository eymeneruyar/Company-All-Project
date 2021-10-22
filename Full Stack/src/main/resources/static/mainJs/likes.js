// Listing the number of likes by products
function list_likesByProduct() {
    $.ajax({
        url:"./likes/list/likesByProduct",
        type: "get",
        dataType:"json",
        contentType:'application/json; charset=utf-8',
        success:function (data) {
            createRowDataProductLikes(data);
        }
    })
}
list_likesByProduct();

// total likes of the products are printed on the screen
let globalProductLikesArray = []
function createRowDataProductLikes(data) {

    let html = ``
    for (let i = 0; i < data.length; i++) {
        globalProductLikesArray = data
        const itm = data[i]
        console.log("productLikes : " + itm.name + "--" + itm.totalLike);
        html += `<tr>
                     <td>${itm.id}</td>
                     <td>${itm.name}</td>
                     <td>
                        <div id="${itm.id}"  class="read-only-ratings-${itm.id}" data-rateyo-read-only="true"> </div>
                     </td>
                 </tr>`
    }
    $("#productLikeTableBody").html(html)

    // Ratings To ProductId
    data.forEach(itm => {
        var isRtl = $('html').attr("data-textdirection") === 'rtl',
            readOnlyRatings = $(`.read-only-ratings-${itm.id}`)

        if (readOnlyRatings.length) {
            readOnlyRatings.rateYo({
                rating: itm.totalLike,
                rtl: isRtl
            });
        }
    })
}

//-------------------------------------- LikesCustomer List - Start ----------------------------------------//
let globalArr2 = []
function dynamicPagination(totalPage, size) {
    $('#pagination_likesCustomer').twbsPagination({
        totalPages: totalPage,
        visiblePages: 5,
        prev: 'Prev',
        first: 'First',
        last: 'Last',
        startPage: 1,
        onPageClick: function (event, page) {
            if($('#searchLikesCustomer').val() === ""){
                getLikesListByCustomer(page, size);
            }else{
                searchLikesCustomerCategory(page,size,$('#searchLikesCustomer').val())
            }

            //$('#firstLast1-content').text('You are on Page ' + page);
            $('.pagination').find('li').addClass('page-item');
            $('.pagination').find('a').addClass('page-link');
        }
    });
}

function getLikesListByCustomer(page,showNumber){
    $.ajax({
        url:"./likes/likesListByCustomer/" + showNumber + "/" + (page-1),
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
                text: "An error occurred during the news category list operation!",
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

function createRowData(data) {
    console.log(data)

    let html = ``
    for (let i = 0; i < data.result.length; i++) {
        globalArr = data.result
        const itm = data.result[i]
        console.log(globalArr)
        html += `<tr>
                     <td>${itm.id}</td>
                     <td>${itm.customerName}</td>
                     <td>${itm.productName}</td>
                     <td>
                        <div id="${itm.id}"  class="read-only-ratings-${itm.id}" data-rateyo-read-only="true"> </div>
                     </td>

                     <td>
                         <a  class="btn btn-outline-danger"  href="javascript:likesCustomerDelete(${itm.id});">
                             <i class="far fa-trash-alt"></i>
                         </a>
                     </td>
                 </tr>`
    }
    $("#totalLikeTableBody").html(html)

    // Ratings To CustomerByProduct
    data.result.forEach(itm => {
        var isRtl = $('html').attr("data-textdirection") === 'rtl',
            readOnlyRatings = $(`.read-only-ratings-${itm.id}`)

        if (readOnlyRatings.length) {
            readOnlyRatings.rateYo({
                rating: itm.productRating,
                rtl: isRtl
            });
        }
    })
}

$('#showLikesCustomerSize').change(function(){
    $('#pagination_likesCustomer').twbsPagination('destroy');
    getLikesListByCustomer(1, parseInt($(this).val()));
});
getLikesListByCustomer(1, 5);
//-------------------------------------- LikesCustomer List - End ------------------------------------------//

//-------------------------------------- LikesCustomer Search - Start ------------------------------------------------//
function searchLikesCustomerCategory(page,showPageSize,searchData){

    $.ajax({
        url: './likes/searchLikesCustomer/' + searchData + "/" + (page-1) + "/" + showPageSize,
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

$('#searchLikesCustomer').keyup( function (event) {
    event.preventDefault();
    const searchData = $(this).val()
    if(searchData !== ""){
        $("#totalLikeTableBody > tr" ).remove()
        searchLikesCustomerCategory(1,$("#showLikesCustomerSize").val(),searchData)
    }else{
        $('#pagination_likesCustomer').twbsPagination('destroy');
        getLikesListByCustomer(1, $("#showLikesCustomerSize").val());
    }
})
//-------------------------------------- LikesCustomer Search - End --------------------------------------------------//

//-------------------------------------- LikesCustomer  Delete - Start --------------------------------------------//
function likesCustomerDelete(id){
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
                url: './likes/likesCustomer/' + id,
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
                        fncReset();
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
//-------------------------------------- LikesCustomer Delete - End ----------------------------------------------//

function fncReset() {
    getLikesListByCustomer(1,5);
}