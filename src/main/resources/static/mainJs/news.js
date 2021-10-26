// ================================================CATEGORY ACTIONS - START=============================================
$('#pagination_news').twbsPagination('destroy');
getAllNewsByPage(1, $("#showNewsSize").val());

$('#pagination_category').twbsPagination('destroy');
getAllNewsCategoryByPage(1, $("#showCategorySize").val());

$('#pagination_news').change(function () {
    $('#pagination_news').twbsPagination('destroy');
    getAllNewsByPage(1, $("#showNewsSize").val());
});
$('#pagination_category').change(function () {
    console.log("değişti")
    $('#pagination_category').twbsPagination('destroy');
    getAllNewsCategoryByPage(1, $("#showCategorySize").val());
});

// <------------------------------------------------FUNCTIONS - START-------------------------------------------------->

function noGenerator() {
    const date = new Date();
    const time = date.getTime();
    return time.toString().substring(4);
}

function fncDate() {
    const d = new Date();
    const ye = new Intl.DateTimeFormat('tr', {year: 'numeric'}).format(d)
    const mo = new Intl.DateTimeFormat('tr', {month: '2-digit'}).format(d)
    const da = new Intl.DateTimeFormat('tr', {day: '2-digit'}).format(d)
    return `${da}-${mo}-${ye}`;
}

function resetFormCategory() {
    select_id = 0
    $('#categoryName').val("")
    $('#categoryDetails').val("")
    $('#categoryStatus').val("")
    getAllNewsCategoryByPage(1, 10);
}

function resetFormNews() {
    select_id_news = 0
    select_id_category = 0;

    $('#title').val("")
    $('#summary').val("")
    $('#description').val("")
    $('#status').val("1")
    $('#newsCategories').empty()
    getAllNewsByPage(1, 10);
    allNewsCategoryList()
}

// <------------------------------------------------FUNCTIONS - END---------------------------------------------------->

// <------------------------------------------------NEWS CATEGORY ADD - START------------------------------------------>

let select_id_category = 0;
$('#newsCategoryAdd_modalForm').submit((event) => {

    event.preventDefault()

    const categoryName = $('#categoryName').val()
    const categoryDetails = $('#categoryDetails').val()
    const categoryStatus = $('#categoryStatus').val()

    const obj = {
        no: noGenerator(),
        name: categoryName,
        detail: categoryDetails,
        status: categoryStatus == 1 ? "Active" : "Passive",
        date: fncDate()
    }

    if (select_id_category != 0) {
        // update
        obj["id"] = select_id_category;
    }

    $.ajax({
        url: './news/categoryAdd',
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
                setTimeout(function () {
                    $("#categoryAdd_modal").modal('hide');
                }, 2000);
                resetFormCategory()
                resetFormNews()
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
                resetFormNews()
            } else {
                if (!jQuery.isEmptyObject(data.errors)) {
                    console.log("burada")
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
                    resetFormNews()
                } else {
                    console.log("burada");
                    Swal.fire({
                        title: 'Error!',
                        text: data.message,
                        icon: "error",
                        customClass: {
                            confirmButton: 'btn btn-primary'
                        },
                        buttonsStyling: false
                    });
                    resetFormNews()
                }

            }
            console.log(data)
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
            resetFormNews()
        }
    })
})

// <------------------------------------------------NEWS CATEGORY ADD - END-------------------------------------------->


// <------------------------------------------------NEWS CATEGORY DELETE - START--------------------------------------->

function newsCategoryDelete(id) {
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
                url: './news/categoryDelete/' + id,
                type: 'DELETE',
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                success: function (data) {
                    if (id != null) {
                        Swal.fire({
                            icon: 'success',
                            title: "Deleted!",
                            text: data.message,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        });
                        resetFormCategory()
                        resetFormNews()
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: "Error",
                            text: data.message,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        });
                        resetFormNews()
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
                    resetFormNews()
                }
            })
        }
    });
}

// <------------------------------------------------NEWS CATEGORY DELETE - END----------------------------------------->

// <------------------------------------------------NEWS CATEGORY UPDATE - START--------------------------------------->

function newsCategoryUpdate(i) {
    $('#categoryAdd_modal').modal('toggle');
    const itm = globalArr[i]
    select_id_category = itm.id
    $('#categoryName').val(itm.name)
    $('#categoryDetails').val(itm.detail)
    $('#categoryStatus').val(itm.status == "Active" ? 1 : 2)
    resetFormNews()
    //console.log(select_id)
}

// <------------------------------------------------NEWS CATEGORY UPDATE - END----------------------------------------->

// <------------------------------------------------NEWS CATEGORY LIST - START----------------------------------------->

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
            if ($('#searchCategory').val() === "") {
                getAllNewsCategoryByPage(page, size);
            } else {
                searchNewsCategory(page, size, $('#searchCategory').val())
            }

            //$('#firstLast1-content').text('You are on Page ' + page);
            $('.pagination').find('li').addClass('page-item');
            $('.pagination').find('a').addClass('page-link');
        }
    });
}

function getAllNewsCategoryByPage(page, showNumber) {
    $.ajax({
        url: "./news/categoryList/" + showNumber + "/" + (page - 1),
        type: "get",
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            //console.log(data)
            createRowData(data)
            dynamicPagination(data.totalPage, showNumber)
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

function createRowData(data) {
    console.log(data)

    let html = ``
    for (let i = 0; i < data.result.length; i++) {
        globalArr = data.result
        const itm = data.result[i]
        console.log(globalArr)
        html += `<tr>
                     <td>${itm.no}</td>
                     <td>${itm.name}</td>
                     <td>${itm.detail}</td>
                     <td>${itm.status}</td>
                     
                     <td>
                         <div class="dropdown">
                             <button type="button" class="btn btn-sm dropdown-toggle hide-arrow"
                                 data-toggle="dropdown">
                                 <i class="fas fa-ellipsis-v"></i>
                             </button>
                             <div class="dropdown-menu">
                                 <a class="dropdown-item" href="javascript:newsCategoryUpdate(${i});">
                                     <i class="mr-50 fas fa-pen"></i>
                                     <span>Edit</span>
                                 </a>
                                 <a class="dropdown-item" href="javascript:newsCategoryDelete(${itm.categoryId});">
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

$('#showCategorySize').change(function () {
    if($('#searchCategory').val() === ""){
        $('#pagination_category').twbsPagination('destroy');
        getAllNewsCategoryByPage(1, parseInt($(this).val()));
    }else{
        searchNewsCategory( page, $('#showCategorySize').val(),$('#searchCategory').val());
    }
});
getAllNewsCategoryByPage(1, 10);

// <------------------------------------------------NEWS CATEGORY LIST - END------------------------------------------->

// <------------------------------------------------NEWS CATEGORY SEARCH - START--------------------------------------->

function searchNewsCategory(page, showPageSize, searchData) {

    $.ajax({
        url: './news/searchNewsCategory/' + searchData + "/" + (page - 1) + "/" + showPageSize,
        type: 'get',
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            //console.log("Data result " + JSON.stringify(data))
            createRowData(data)
            dynamicPagination(data.totalPage, showPageSize)
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

$('#searchCategory').keyup(function (event) {
    console.log("harf girildi")
    event.preventDefault();
    const searchData = $(this).val()
    if (searchData !== "") {
        $("#categoryTable > tr").remove()
        searchNewsCategory(1, $("#showCategorySize").val(), searchData)
    } else {
        $('#pagination_category').twbsPagination('destroy');
        getAllNewsCategoryByPage(1, $("#showCategorySize").val());
    }
})

// <------------------------------------------------NEWS CATEGORY SEARCH - END----------------------------------------->

// ================================================CATEGORY ACTIONS - END===============================================


// ================================================NEWS ACTIONS - START=================================================


// <------------------------------------------------NEWS CATEGORY DATA  - START---------------------------------------->

function allNewsCategoryList() {
    $.ajax({
        url: './news/categoryList',
        type: 'get',
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            category = data.result;
            //console.log("News Categories : ", categories)
            newsCategoryOption(category)
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

allNewsCategoryList()

function newsCategoryOption(data) {
    data.forEach(item => {
        $('#newsCategories').append('<option value="' + item.id + '">' + item.name + '</option>');
    })
}

// <------------------------------------------------NEWS CATEGORY DATA  - END------------------------------------------>

// <------------------------------------------------NEWS ADD - START--------------------------------------------------->

let select_id_news = 0;
$('#newsAdd_modalForm').submit((event) => {

    event.preventDefault()

    const title = $('#title').val()
    const summary = $('#summary').val()
    const description = $('#description').val()
    const status = $('#status').val()
    const newsCategory = $('#newsCategories').val()


    const obj = {
        no: noGenerator(),
        title: title,
        summary: summary,
        description: description,
        status: status == 1 ? "Active" : "Passive",
        newsCategory: {
            id: newsCategory
        },
        date: fncDate()
    }

    if (select_id_news != 0) {
        // update
        obj["id"] = select_id_news;
    }
    console.log(obj)
    $.ajax({
        url: './news/add',
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
                setTimeout(function () {
                    $("#newsAdd_modal").modal('hide');
                }, 2000);
                resetFormNews()
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
                resetFormNews()
            } else {
                if (!jQuery.isEmptyObject(data.errors)) {
                    console.log("burada")
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
                    console.log("burada");
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

// <------------------------------------------------NEWS ADD - END----------------------------------------------------->


// <------------------------------------------------NEWS LIST - START-------------------------------------------------->

let globalNewsArr = []

function dynamicPaginationNews(totalPage, size) {
    $('#pagination_news').twbsPagination({
        totalPages: totalPage,
        visiblePages: 5,
        prev: 'Prev',
        first: 'First',
        last: 'Last',
        startPage: 1,
        onPageClick: function (event, page) {
            if ($('#searchNewsData').val() === "") {
                getAllNewsByPage(page, size);
            } else {
                searchNews(page, size, $('#searchNewsData').val())
            }
            $('.pagination').find('li').addClass('page-item');
            $('.pagination').find('a').addClass('page-link');
        }
    });
}

function getAllNewsByPage(page, showNumber) {
    $.ajax({
        url: "./news/newslist/" + showNumber + "/" + (page - 1),
        type: "get",
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            createRowDataNews(data)
            dynamicPaginationNews(data.totalPage, showNumber)
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

function createRowDataNews(data) {
    let html = ``;
    for (let i = 0; i < data.result.length; i++) {
        globalNewsArr = data.result
        const itm = data.result[i]
        console.log(globalNewsArr)
        html += `<tr>
                     <td>${itm.no}</td>
                     <td>${itm.title}</td>
                     <td>${itm.categoryName}</td>
                     <td>${itm.status}</td>
                     <td>${itm.summary}</td>
                     <td>${itm.date}</td>
                     <td>
                         <div class="dropdown">
                             <button type="button" class="btn btn-sm dropdown-toggle hide-arrow"
                                 data-toggle="dropdown">
                                 <i class="fas fa-ellipsis-v"></i>
                             </button>
                             <div class="dropdown-menu">
                                <a class="dropdown-item" href="javascript:newsAddImages(${i});">
                                     <i class="mr-50 far fa-images"></i>
                                     <span>Add Images</span>
                                 </a>
                                 <a class="dropdown-item" type="button" href="javascript:newsDetail(${i});" >
                                      <i class="mr-50 fas fa-info-circle"></i>
                                      <span>Detail</span>
                                 </a>
                                 <a class="dropdown-item" href="javascript:newsUpdate(${i});">
                                     <i class="mr-50 fas fa-pen"></i>
                                     <span>Edit</span>
                                 </a>
                                 <a class="dropdown-item" href="javascript:newsDelete(${itm.id});">
                                     <i class="mr-50 far fa-trash-alt"></i>
                                     <span>Delete</span>
                                 </a>
                             </div>
                         </div>
                     </td>
                 </tr>`
    }
    $("#newsTable").html(html)
}

$('#showNewsTableRow').change(function () {
    if($('#searchNewsData').val() === ""){
        $('#pagination_news').twbsPagination('destroy');
        getAllNewsByPage(1, parseInt($(this).val()));
    }else{
        searchNews( page, $('#showNewsTableRow').val(),$('#searchNewsData').val());
    }
});
getAllNewsByPage(1, 10);

// <------------------------------------------------NEWS LIST - END---------------------------------------------------->


// <------------------------------------------------NEWS DELETE - START------------------------------------------------>

function newsDelete(id) {
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
                url: './news/delete/' + id,
                type: 'DELETE',
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                success: function (data) {
                    if (id != null) {
                        Swal.fire({
                            icon: 'success',
                            title: "Deleted!",
                            text: data.message,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        });
                        resetFormNews()
                    } else {
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

// <------------------------------------------------NEWS DELETE - END-------------------------------------------------->


function newsAddImages(i) {
    $('#newsImage').modal('toggle');
    const itm = globalNewsArr[i]
    $("#modalTitle").text(itm.title + " - " + itm.summary)
    $.ajax({
        url: './news/chosenId/' + itm.id,
        type: 'get',
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
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

// <------------------------------------------------NEWS DETAIL - START------------------------------------------------>

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

function newsDetail(i) {
    $('#newsDetail').modal('toggle')
    $('#imagesChose').empty()
    const itm = globalNewsArr[i]
    let html = ``
    var fileName
    $.ajax({
        url: './news/chosenId/' + itm.id,
        type: 'get',
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            console.log(data)
            $.ajax({
                url: "./news/detail/" + itm.id,
                type: "get",
                dataType: "json",
                contentType: 'application/json; charset=utf-8',
                success: function (data) {
                    console.log(data.result)
                    for (let j = 0; j < data.result.fileName.length; j++) {
                        fileName = data.result.fileName[j]
                        html += `<div class="swiper-slide">
<!--                             <img class="img-fluid" src="/uploadImages/_news/${itm.id}/${fileName}" alt="banner"/>-->
                             <img src="/news/newsDetail/get_image/id=${itm.id}name=${fileName}" class="img-fluid" alt="banner" />

                         </div>`

                        $('#imagesChose').append('<option value="' + fileName + '">Image -  ' + j + '</option>');

                    }
                    $("#imageSlide").html(html)
                    $("#title").text(itm.no + " - " + itm.name + "  " + itm.description)
                    $("#titleDetail").text(itm.title)
                    $("#dateDetail").text(itm.date)
                    $("#statusDetail").text(itm.status)
                    $("#descriptionDetails").text(data.result.description)
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

// <------------------------------------------------NEWS DETAIL - END-------------------------------------------------->

// <------------------------------------------------NEWS SEARCH - START------------------------------------------------>

function searchNews(page, showPageSize, searchData) {

    $.ajax({
        url: './news/search/' + searchData + "/" + (page - 1) + "/" + showPageSize,
        type: 'get',
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            createRowDataNews(data)
            dynamicPaginationNews(data.totalPage, showPageSize)
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

$('#searchNewsData').keyup(function (event) {
    console.log("değişti")
    event.preventDefault();
    const searchDataNews = $(this).val()

    if (searchDataNews !== "") {
        $("#newsTable > tr").remove()
        $('#pagination_news').twbsPagination('destroy');
        searchNews(1, $("#showNewsTableRow").val(), searchDataNews)
    } else {
        $('#pagination_news').twbsPagination('destroy');
        getAllNewsByPage(1, $("#showNewsTableRow").val());
    }
})

// <------------------------------------------------NEWS SEARCH - END-------------------------------------------------->

// <------------------------------------------------NEWS UPDATE - START------------------------------------------------>

function newsUpdate(i) {
    $('#newsAdd_modal').modal('toggle');
    const itm = globalNewsArr[i]
    select_id_news = itm.id
    console.log("select_id : " + select_id_news)
    $.ajax({
        url: "./news/detail/" + itm.id,
        type: "get",
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            console.log(data.result)
            // for (let i = 0; i < data.result.newsCategory.length; i++) {
            //     $('#newsCategories').val(data.result.newsCategory[i].id).trigger('change')
            // }
            // allNewsCategoryList()

            $('#title').val(data.result.title)
            $('#summary').val(data.result.summary)
            $('#description').val(data.result.description)
            $("#status").val(data.result.status == "Active" ? 1 : 2)
            $('#newsCategories').val(data.result.newsCategory.id).trigger('change');
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

// <------------------------------------------------NEWS UPDATE - END-------------------------------------------------->

// <------------------------------------------------NEWS IMAGE DELETE - START------------------------------------------->

function deleteImage() {

    const chosenImages = $("#imagesChose").val()
    console.log(chosenImages)

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
                url: './news/chosenImages/delete/' + chosenImages,
                type: 'DELETE',
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                success: function (data) {
                    if (chosenImages.length > 0) {
                        Swal.fire({
                            icon: 'success',
                            title: "Deleted!",
                            text: data.message,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        });
                        $('#imagesChose').empty()
                        setTimeout(function () {
                            $("#newsDetail").modal('hide');
                        }, 2000);
                    } else {
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

// <------------------------------------------------NEWS IMAGE DELETE - END-------------------------------------------->


// ================================================NEWS ACTIONS - END===================================================



