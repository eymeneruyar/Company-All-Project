$('#pagination_newsList').twbsPagination('destroy');
getAllNewsByCategoryAndPage(1, 9, 1);
//------------------------------------ News Category List - Start ------------------------------------//
function categoryList() {

    let category

    $.ajax({
        url: "./newsList/categories/",
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

categoryList()
//------------------------------------ News Category List - End ------------------------------------//

//------------------------------------ News List Pagination - Start --------------------------------------//
let globalNewsArr = []

function dynamicPaginationNewsList(totalPage, size) {
    $('#pagination_newsList').twbsPagination({
        totalPages: totalPage,
        visiblePages: 5,
        prev: 'Prev',
        first: 'First',
        last: 'Last',
        startPage: 1,
        onPageClick: function (event, page) {
            if ($('#searchNewsList').val() === "") {
                getAllNewsByCategoryAndPage(page, size, $('#categoryFilter').val());
            } else {
                searchNews(page, size, $('#searchNews').val())
            }
            //$('#firstLast1-content').text('You are on Page ' + page);
            $('.pagination').find('li').addClass('page-item');
            $('.pagination').find('a').addClass('page-link');
        }
    });
}

function getAllNewsByCategoryAndPage(page, showNumber, categoryId) {


    $.ajax({
        url: "./newsList/listByCategoryIdElasticsearch/" + categoryId + "/" + showNumber + "/" + (page - 1),
        type: "get",
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            console.log("categoryandpage: ")
            createNewsCard(data)
            console.log("createnewcard")
            dynamicPaginationNewsList(data.totalPage, showNumber)
            console.log("dynamic")

        },
        error: function (err) {
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the news list operation!",
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

function createNewsCard(data) {
    console.log("newsCard")
    console.log(data.result.length)

    let html = ``;
    for (let i = 0; i < data.result.length; i++) {
        globalNewsArr = data.result
        console.log("globalNewsArr")
        console.log(globalNewsArr)
        const itm = data.result[i]
        console.log("itemId: " + itm.id)
        console.log(itm)
        html += `<div class="card ecommerce-card">
                     <div class="item-img text-center">
                         <a href="/newsDetail/${itm.id}">
                            <img class="img-fluid card-img-top" src="/news/newsDetail/get_image/id=${itm.id}name=${itm.fileName[0]}" alt="img-placeholder" />
                        </a>
                     </div>
                     <div class="card-body">
                         
                     <h6 class="item-name">
                             <a class="text-body" href="/newsDetail/${itm.id}">${itm.title} - ${itm.summary}</a>
                             <span class="card-text item-company">By <a href="javascript:void(0)" class="company-name">${itm.title}</a></span>
                         </h6>
                         <p class="card-text item-description">
                             ${itm.description}
                         </p> 
                     </div>
                 </div>`
    }
    $("#news").html(html)
}

$('#categoryFilter').change(function () {
    $('#pagination_newsList').twbsPagination('destroy');
    getAllNewsByCategoryAndPage(1, 9, parseInt($(this).val()));
    console.log("Category change Id " + parseInt($(this).val()));
});
getAllNewsByCategoryAndPage(1, 9, 1)
//------------------------------------ News List Pagination - End ----------------------------------------//

//------------------------------------ News List Search - Start ----------------------------------------//
function searchNews(page, showPageSize, searchData) {

    $.ajax({
        url: './newsList/search/' + searchData + "/" + (page - 1) + "/" + showPageSize,
        type: 'get',
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            createNewsCard(data)
            dynamicPaginationNewsList(data.totalPage, showPageSize)
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

$('#searchNewsList').keyup(function (event) {
    event.preventDefault();
    const searchData = $(this).val()
    if (searchData !== "") {
        //$("#productTable > tr" ).remove()
        //$('#content_pagination').twbsPagination('destroy');
        searchNews(1, 9, searchData)
    } else {
        $('#pagination_newsList').twbsPagination('destroy');
        getAllNewsByCategoryAndPage(1, 9, 1);
    }
})
//------------------------------------ News List Search - End ------------------------------------------//