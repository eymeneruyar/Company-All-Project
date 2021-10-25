//------------------------------------ Product Category List - Start ------------------------------------//
function categoryList(){

    let category

    $.ajax({
        url:"./productList/categories/",
        type: "get",
        dataType: "json",
        contentType : 'application/json; charset=utf-8',
        success: function (data){
            //console.log(data.result)
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
            //getAllProductsByCategoryAndPage(page,size,$('#categoryFilter').val())
            if($('#searchProductList').val() === ""){
                getAllProductsByCategoryAndPage(page, size,$('#categoryFilter').val());
            }else{
                searchProduct(page,size,$('#searchProduct').val())
            }
            //$('#firstLast1-content').text('You are on Page ' + page);
            $('.pagination').find('li').addClass('page-item');
            $('.pagination').find('a').addClass('page-link');
        }
    });
}
function getAllProductsByCategoryAndPage(page,showNumber,categoryId){

    //var categoryId = $("#categoryFilter").val()
    //console.log("Category " + categoryId)

    $.ajax({
        url:"./productList/listByCategoryIdElasticsearch/" + categoryId + "/" + showNumber + "/" + (page-1),
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
    let html = ``;
    for (let i = 0; i < data.result.length; i++){
        globalProductArr = data.result
        const itm = data.result[i]
        let price = priceFormatter(itm.price)
        //console.log(price)
        html += `<div class="card ecommerce-card">
                     <div class="item-img text-center">
                         <a href="/productDetail/${itm.productId}">
                             <img class="img-fluid card-img-top" src="productDetail/get_image/id=${itm.productId}name=${itm.fileName[0]}" alt="img-placeholder" />
                         </a>
                     </div>
                     <div class="card-body">
                         <div class="item-wrapper">
                                 
                                 <div id="${itm.productId}" class="read-only-ratings-${itm.productId}" data-rateyo-read-only="true"></div>
                                
                             <div>
                                 <h6 class="item-price">${price} TL</h6>
                             </div>
                         </div>
                         <h6 class="item-name">
                             <a class="text-body" href="/productDetail/${itm.productId}">${itm.name} ${itm.description}</a>
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
                         <a href="/productDetail/${itm.productId}" class="btn btn-primary btn-cart">
                             <i data-feather='info'></i>
                             <span class="add-to-cart">More Details</span>
                         </a>
                     </div>
                 </div>`
    }
    $("#products").html(html)

    // Ratings To CustomerByProduct
    data.result.forEach(itm => {
        var isRtl = $('html').attr("data-textdirection") === 'rtl',
            readOnlyRatings = $(`.read-only-ratings-${itm.productId}`)

        if (readOnlyRatings.length) {
            readOnlyRatings.rateYo({
                rating: itm.totalLike,
                rtl: isRtl,
                starWidth: "15px"
            });
        }
    })

}

function priceFormatter(price){
    var formattedPrice = (price).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,')
    return formattedPrice
}

$('#categoryFilter').change(function(){
    $('#pagination_productList').twbsPagination('destroy');
    getAllProductsByCategoryAndPage(1, 9,parseInt($(this).val()));
    //console.log("Category change Id " + parseInt($(this).val()));
});
getAllProductsByCategoryAndPage(1,9, 1)
//------------------------------------ Product List Pagination - End ----------------------------------------//

//------------------------------------ Product List Search - Start ----------------------------------------//
function searchProduct(page,showPageSize,searchData){

    $.ajax({
        url: './productList/search/' + searchData + "/" + (page-1) + "/" + showPageSize,
        type: 'get',
        dataType: "json",
        contentType : 'application/json; charset=utf-8',
        success: function (data) {
            //console.log(data)
            createProductCard(data)
            dynamicPaginationProductList(data.totalPage,showPageSize)
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

$('#searchProductList').keyup( function (event) {
    event.preventDefault();
    const searchData = $(this).val()
    if(searchData !== ""){
        //$('#pagination_productList').twbsPagination('destroy');
        searchProduct(1,9,searchData)
    }else{
        $('#pagination_productList').twbsPagination('destroy');
        getAllProductsByCategoryAndPage(1, 9,1);
    }
})
//------------------------------------ Product List Search - End ------------------------------------------//
