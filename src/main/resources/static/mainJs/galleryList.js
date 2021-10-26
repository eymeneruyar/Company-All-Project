$('#pagination_galleryList').twbsPagination('destroy');
getAllGalleryByCategoryAndPage(1, 9, 1);
//------------------------------------ Gallery Category List - Start ------------------------------------//
function categoryList() {

    let category

    $.ajax({
        url: "./galleryList/categories/",
        type: "get",
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            console.log(data.result)
            for (let i = 0; i < data.result.length; i++) {
                category = data.result[i]
                console.log(category)
                $("#categoryFilter").append('<option value="' + category.id + '">' + category.name + '</option>')
            }

        },
        error: function (err) {
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the gallery category list operation!",
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
//------------------------------------ Gallery Category List - End ------------------------------------//

//------------------------------------ Gallery List Pagination - Start --------------------------------------//
let globalGalleryArr = []

function dynamicPaginationGalleryList(totalPage, size) {
    $('#pagination_galleryList').twbsPagination({
        totalPages: totalPage,
        visiblePages: 5,
        prev: 'Prev',
        first: 'First',
        last: 'Last',
        startPage: 1,
        onPageClick: function (event, page) {
            if ($('#searchGalleryList').val() === "") {
                getAllGalleryByCategoryAndPage(page, size, $('#categoryFilter').val());
            } else {
                searchGallery(page, size, $('#searchGallery').val())
            }
            //$('#firstLast1-content').text('You are on Page ' + page);
            $('.pagination').find('li').addClass('page-item');
            $('.pagination').find('a').addClass('page-link');
        }
    });
}

function getAllGalleryByCategoryAndPage(page, showNumber, categoryId) {


    $.ajax({
        url: "./galleryList/listByCategoryIdElasticsearch/" + categoryId + "/" + showNumber + "/" + (page - 1),
        type: "get",
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            console.log("categoryandpage: ")
            createGalleryCard(data)
            console.log("createnewcard")
            dynamicPaginationGalleryList(data.totalPage, showNumber)
            console.log("dynamic")

        },
        error: function (err) {
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the gallery list operation!",
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

function createGalleryCard(data) {
    //console.log(data.result.length)

    let html = ``;
    for (let i = 0; i < data.result.length; i++) {
        globalGalleryArr = data.result
        //console.log(globalGalleryArr)
        const itm = data.result[i]
        //console.log("itemId: " + itm.id)
        console.log(itm)
        html += `<div class="card ecommerce-card">
                     <div class="item-img text-center">
                         <a href="/galleryDetail/${itm.id}">
                            <img class="img-fluid card-img-top" src="/gallery/galleryDetail/get_image/id=${itm.id}name=${itm.fileName[0]}" alt="img-placeholder" />
                        </a>
                     </div>
                     <div class="card-body">
                         
                     <h6 class="item-name">
                             <a class="text-body" href="/galleryDetails/${itm.id}">${itm.title}  ${itm.description}</a>
                             <span class="card-text item-company">By <a href="/galleryDetails/${itm.id}" class="company-name">${itm.title}</a></span>
                         </h6>
                         <p class="card-text item-description">
                             ${itm.description}
                         </p> 
                     </div>
                 </div>`
    }
    $("#gallery").html(html)
}

$('#categoryFilter').change(function () {
    $('#pagination_galleryList').twbsPagination('destroy');
    getAllGalleryByCategoryAndPage(1, 9, parseInt($(this).val()));
    console.log("Category change Id " + parseInt($(this).val()));
});
getAllGalleryByCategoryAndPage(1, 9, 1)
//------------------------------------ Gallery List Pagination - End ----------------------------------------//

//------------------------------------ Gallery List Search - Start ----------------------------------------//
function searchGallery(page, showPageSize, searchData) {

    $.ajax({
        url: './galleryList/search/' + searchData + "/" + (page - 1) + "/" + showPageSize,
        type: 'get',
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            createGalleryCard(data)
            dynamicPaginationGalleryList(data.totalPage, showPageSize)
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

$('#searchGalleryList').keyup(function (event) {
    event.preventDefault();
    const searchData = $(this).val()
    if (searchData !== "") {
        searchGallery(1, 9, searchData)
    } else {
        $('#pagination_galleryList').twbsPagination('destroy');
        getAllGalleryByCategoryAndPage(1, 9, 1);
    }
})
//------------------------------------ Gallery List Search - End ------------------------------------------//