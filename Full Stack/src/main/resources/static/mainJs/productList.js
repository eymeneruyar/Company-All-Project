//------------------------------------ Product Category List - Start ------------------------------------//
function categoryList(){

    let category

    $.ajax({
        url:"./productList/categories/",
        type: "get",
        dataType: "json",
        contentType : 'application/json; charset=utf-8',
        success: function (data){
            console.log(data.result)
            for (let i = 0; i < data.result.length; i++) {
                category = data.result[i]
                $("#categoryFilter").append('<option value="' + category.id + '">' + category.name + '</option>')
            }

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
categoryList()
//------------------------------------ Product Category List - End --------------------------------------//

//------------------------------------ Product List Pagination - Start --------------------------------------//
let globalProductArr = []
function dynamicPaginationProductList(totalPage, size) {
    $('#pagination_productList').twbsPagination({
        totalPages: totalPage,
        visiblePages: 5,
        prev: 'Prev',
        first: 'First',
        last: 'Last',
        startPage: 1,
        onPageClick: function (event, page) {
            getAllProductsByCategoryAndPage(page,size,$('#categoryFilter').val())
            /*if($('#searchProduct').val() === ""){
                getAllProductsByPage(page, size);
            }else{
                searchProduct(page,size,$('#searchProduct').val())
            }*/
            //$('#firstLast1-content').text('You are on Page ' + page);
            $('.pagination').find('li').addClass('page-item');
            $('.pagination').find('a').addClass('page-link');
        }
    });
}
function getAllProductsByCategoryAndPage(page,showNumber,categoryId){

    //var categoryId = $("#categoryFilter").val()
    console.log("Category " + categoryId)

    $.ajax({
        url:"./productList/listByCategoryId/" + categoryId + "/" + showNumber + "/" + (page-1),
        type: "get",
        dataType: "json",
        contentType : 'application/json; charset=utf-8',
        success: function (data){
            createProductCard(data)
            dynamicPaginationProductList(data.totalPage,showNumber)
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

function createProductCard(data){
    console.log(data)
    let html = ``;
    for (let i = 0; i < data.result.length; i++){
        globalProductArr = data.result
        const itm = data.result[i]
        let price = priceFormatter(itm.price)
        console.log(price)
        html += `<div class="card ecommerce-card">
                     <div class="item-img text-center">
                         <a href="/productDetail/${itm.id}">
                             <img class="img-fluid card-img-top" src="/uploadImages/_products/${itm.id}/${itm.fileName[0]}" alt="img-placeholder" /></a>
                     </div>
                     <div class="card-body">
                         <div class="item-wrapper">
                             <div class="item-rating">
                                 <ul class="unstyled-list list-inline">
                                     <li class="ratings-list-item"><i data-feather="star" class="filled-star"></i></li>
                                     <li class="ratings-list-item"><i data-feather="star" class="filled-star"></i></li>
                                     <li class="ratings-list-item"><i data-feather="star" class="filled-star"></i></li>
                                     <li class="ratings-list-item"><i data-feather="star" class="filled-star"></i></li>
                                     <li class="ratings-list-item"><i data-feather="star" class="unfilled-star"></i></li>
                                 </ul>
                             </div>
                             <div>
                                 <h6 class="item-price">${price} TL</h6>
                             </div>
                         </div>
                         <h6 class="item-name">
                             <a class="text-body" href="/productDetail/${itm.id}">${itm.name} ${itm.description}</a>
                             <span class="card-text item-company">By <a href="javascript:void(0)" class="company-name">${itm.name}</a></span>
                         </h6>
                         <p class="card-text item-description">
                             ${itm.details}
                         </p>
                     </div>
                     <div class="item-options text-center">
                         <div class="item-wrapper">
                             <div class="item-cost">
                                 <h4 class="item-price">$339.99</h4>
                             </div>
                         </div>
                         <a href="/productDetail/${itm.id}" class="btn btn-primary btn-cart">
                             <i data-feather='info'></i>
                             <span class="add-to-cart">More Details</span>
                         </a>
                     </div>
                 </div>`
    }
    $("#products").html(html)

}

function priceFormatter(price){
    var formattedPrice = (price).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,')
    return formattedPrice
}

$('#categoryFilter').change(function(){
    $('#pagination_productList').twbsPagination('destroy');
    getAllProductsByCategoryAndPage(1, 9,parseInt($(this).val()));
    console.log("Category change Id " + parseInt($(this).val()));
});
getAllProductsByCategoryAndPage(1,9, 3)
//------------------------------------ Product List Pagination - End ----------------------------------------//
