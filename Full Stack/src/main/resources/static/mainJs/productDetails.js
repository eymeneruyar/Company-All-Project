function mostPopularProduct(){

    $.ajax({
        url:"./mostPopularProducts",
        type: "get",
        dataType: "json",
        contentType : 'application/json; charset=utf-8',
        success: function (data){
            if(data.result.length > 0){
                createCard(data.result)
            }
        },
        error: function (err){
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the most popular five product listing operation!",
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
mostPopularProduct()

function createCard(data){
    console.log(data)
    let html = ``
    for (let i = 0; i <data.length ; i++) {
        const itm = data[i]
        let price = priceFormatter(itm.price)
        html += `<div class="swiper-slide">
                         <a href="/productDetail/${itm.id}">
                             <div class="item-heading">
                                 <h5 class="text-truncate mb-0">${itm.name} ${itm.description}</h5>
                                 <small class="text-body">by ${itm.name}</small>
                             </div>
                             <div class="img-container w-50 mx-auto py-75">
                                 <img src="/uploadImages/_products/${itm.id}/${itm.fileName[0]}" class="img-fluid" alt="image" />
                             </div>
                             <div class="item-meta">
                                <div class="row">
                                     <div class="read-only-ratings-${itm.id} mr-4" data-rateyo-read-only="true"></div>
                                     <div></div>
                                     <p class="card-text text-primary mb-0 ml-5">${price} TL</p>
                                </div>
                                 
                             </div>
                         </a>
                     </div>
                 </div>`
    }
    $("#mostPopularFiveProduct").html(html)

    // Ratings To CustomerByProduct
    data.forEach(itm => {
        var isRtl = $('html').attr("data-textdirection") === 'rtl',
            readOnlyRatings = $(`.read-only-ratings-${itm.id}`)

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