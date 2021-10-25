$('#pagination_gallery').twbsPagination('destroy');
getAllGalleryByPage(1, $("#showGalleryTableRow").val());

$('#pagination_category').twbsPagination('destroy');
getAllGalleryCategoryByPage(1, $("#showCategoryTableRow").val());


function resetFormCategory() {
    // select_id = 0
    $('#categoryName').val("")
    $('#categoryStatus').val("")
    getAllGalleryCategoryByPage(1, 10);
}

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

function resetFormGallery() {
    select_id_gallery = 0
    select_id_category = 0;

    $('#title').val("")
    $('#detail').val("")
    $('#status').val("1")
    $('#galleryCategories').empty()
    getAllGalleryByPage(1, 10);
    allGalleryCategoryList()
}

// <------------------------------------------------GALLERY CATEGORY ADD - START------------------------------------------>
let select_id_category = 0;
$('#galleryCategoryAdd_modalForm').submit((event) => {

    event.preventDefault()

    const categoryName = $('#categoryName').val()
    const categoryStatus = $('#categoryStatus').val()

    const obj = {
        no: noGenerator(),
        name: categoryName,
        status: categoryStatus == 1 ? "Active" : "Passive",
        date: fncDate()
    }

    if (select_id_category != 0) {
        // update
        obj["id"] = select_id_category;
    }

    $.ajax({
        url: './gallery/categoryAdd',
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
                resetFormGallery()
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
                resetFormGallery()
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
                    resetFormGallery()
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
                    resetFormGallery()
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
            resetFormGallery()
        }
    })
})
// <------------------------------------------------GALLERY CATEGORY ADD - START------------------------------------------>


// <------------------------------------------------GALLERY CATEGORY DELETE - START--------------------------------------->
function galleryCategoryDelete(id) {
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
                url: './gallery/categoryDelete/' + id,
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
                        resetFormGallery()
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: "Error",
                            text: data.message,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        });
                        resetFormGallery()
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
                    resetFormGallery()
                }
            })
        }
    });
}

// <------------------------------------------------GALLERY CATEGORY DELETE - END----------------------------------------->





// <------------------------------------------------GALLERY CATEGORY LIST - START----------------------------------------->

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
                getAllGalleryCategoryByPage(page, size);
            } else {
                searchGalleryCategory(page, size, $('#searchCategory').val())
            }

            //$('#firstLast1-content').text('You are on Page ' + page);
            $('.pagination').find('li').addClass('page-item');
            $('.pagination').find('a').addClass('page-link');
        }
    });
}

function getAllGalleryCategoryByPage(page, showNumber) {
    $.ajax({
        url: "./gallery/categoryList/" + showNumber + "/" + (page - 1),
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
                     <td>${itm.status}</td>
                     
                     <td>
                         <div class="dropdown">
                             <button type="button" class="btn btn-sm dropdown-toggle hide-arrow"
                                 data-toggle="dropdown">
                                 <i class="fas fa-ellipsis-v"></i>
                             </button>
                             <div class="dropdown-menu">
                                 <a class="dropdown-item" href="javascript:galleryCategoryUpdate(${i});">
                                     <i class="mr-50 fas fa-pen"></i>
                                     <span>Edit</span>
                                 </a>
                                 <a class="dropdown-item" href="javascript:galleryCategoryDelete(${itm.categoryId});">
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
    $('#pagination_category').twbsPagination('destroy');
    getAllGalleryCategoryByPage(1, parseInt($(this).val()));
});
getAllGalleryCategoryByPage(1, 10);

// <------------------------------------------------GALLERY CATEGORY LIST - END------------------------------------------->

// <------------------------------------------------GALLERY CATEGORY UPDATE - START--------------------------------------->

function galleryCategoryUpdate(i) {
    $('#categoryAdd_modal').modal('toggle');
    const itm = globalArr[i]
    select_id_category = itm.id
    $('#categoryName').val(itm.name)
    $('#categoryStatus').val(itm.status == "Active" ? 1 : 2)
    resetFormGallery()
    console.log(select_id)
}

// <------------------------------------------------GALLERY CATEGORY UPDATE - END----------------------------------------->


// <------------------------------------------------GALLERY CATEGORY SEARCH - START--------------------------------------->

function searchGalleryCategory(page, showPageSize, searchData) {

    $.ajax({
        url: './gallery/searchGalleryCategory/' + searchData + "/" + (page - 1) + "/" + showPageSize,
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
        searchGalleryCategory(1, $("#showCategorySize").val(), searchData)
    } else {
        $('#pagination_category').twbsPagination('destroy');
        getAllGalleryCategoryByPage(1, $("#showCategorySize").val());
    }
})

// <------------------------------------------------GALLERY CATEGORY SEARCH - END----------------------------------------->


// <------------------------------------------------GALLERY CATEGORY DATA  - START---------------------------------------->

function allGalleryCategoryList() {
    $.ajax({
        url: './gallery/categoryList',
        type: 'get',
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            category = data.result;
            //console.log("Gallery Categories : ", categories)
            galleryCategoryOption(category)
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

allGalleryCategoryList()

function galleryCategoryOption(data) {
    data.forEach(item => {
        $('#galleryCategories').append('<option value="' + item.id + '">' + item.name + '</option>');
    })
}

// <------------------------------------------------GALLERY CATEGORY DATA  - END------------------------------------------>

// <------------------------------------------------GALLERY ADD - START--------------------------------------------------->

let select_id_gallery = 0;
$('#galleryAdd_modalForm').submit((event) => {

    event.preventDefault()

    const name = $('#name').val()
    const detail = $('#detail').val()
    const status = $('#status').val()
    const galleryCategory = $('#galleryCategories').val()


    const obj = {
        no: noGenerator(),
        title: name,
        description: detail,
        status: status == 1 ? "Active" : "Passive",
        galleryCategory: {
            id: galleryCategory
        },
        date: fncDate()
    }

    if (select_id_gallery != 0) {
        // update
        obj["id"] = select_id_gallery;
    }
    console.log(obj)
    $.ajax({
        url: './gallery/add',
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
                    $("#galleryAdd_modal").modal('hide');
                }, 2000);
                resetFormGallery()
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
                resetFormGallery()
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

// <------------------------------------------------GALLERY ADD - END----------------------------------------------------->

// <------------------------------------------------GALLERY LIST - START-------------------------------------------------->

let globalGalleryArr = []

function dynamicPaginationGallery(totalPage, size) {
    $('#pagination_gallery').twbsPagination({
        totalPages: totalPage,
        visiblePages: 5,
        prev: 'Prev',
        first: 'First',
        last: 'Last',
        startPage: 1,
        onPageClick: function (event, page) {
            if ($('#searchGalleryData').val() === "") {
                getAllGalleryByPage(page, size);
            } else {
                searchGallery(page, size, $('#searchGalleryData').val())
            }
            $('.pagination').find('li').addClass('page-item');
            $('.pagination').find('a').addClass('page-link');
        }
    });
}

function getAllGalleryByPage(page, showNumber) {
    $.ajax({
        url: "./gallery/gallerylist/" + showNumber + "/" + (page - 1),
        type: "get",
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            createRowDataGallery(data)
            dynamicPaginationGallery(data.totalPage, showNumber)
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

function createRowDataGallery(data) {
    let html = ``;
    for (let i = 0; i < data.result.length; i++) {
        console.log("createRowGallery")
        globalGalleryArr = data.result
        const itm = data.result[i]
        console.log(globalGalleryArr)
        html += `<tr>
                     <td>${itm.no}</td>
                     <td>${itm.title}</td>
                     <td>${itm.description}</td>
                     <td>${itm.status}</td>
                     <td>${itm.date}</td>
                     <td>
                         <div class="dropdown">
                             <button type="button" class="btn btn-sm dropdown-toggle hide-arrow"
                                 data-toggle="dropdown">
                                 <i class="fas fa-ellipsis-v"></i>
                             </button>
                             <div class="dropdown-menu">
                                <a class="dropdown-item" href="javascript:galleryAddImages(${i});">
                                     <i class="mr-50 far fa-images"></i>
                                     <span>Add Images</span>
                                 </a>
                                 <a class="dropdown-item" type="button" href="javascript:galleryDetail(${i});" >
                                      <i class="mr-50 fas fa-info-circle"></i>
                                      <span>Detail</span>
                                 </a>
                                 <a class="dropdown-item" href="javascript:galleryUpdate(${i});">
                                     <i class="mr-50 fas fa-pen"></i>
                                     <span>Edit</span>
                                 </a>
                                 <a class="dropdown-item" href="javascript:galleryDelete(${itm.id});">
                                     <i class="mr-50 far fa-trash-alt"></i>
                                     <span>Delete</span>
                                 </a>
                             </div>
                         </div>
                     </td>
                 </tr>`
    }
    $("#galleryTable").html(html)
}

$('#showGalleryTableRow').change(function () {
    $('#pagination_gallery').twbsPagination('destroy');
    getAllGalleryByPage(1, parseInt($(this).val()));
});
getAllGalleryByPage(1, 10);

// <------------------------------------------------GALLERY LIST - END---------------------------------------------------->


// <------------------------------------------------GALLERY DELETE - START------------------------------------------------>

function galleryDelete(id) {
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
                url: './gallery/delete/' + id,
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
                        resetFormGallery()
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

// <------------------------------------------------GALLERY DELETE - END-------------------------------------------------->


function galleryAddImages(i) {
    $('#galleryImage').modal('toggle');
    const itm = globalGalleryArr[i]
    $("#modalTitle").text(itm.title + " - " + itm.detail)
    $.ajax({
        url: './gallery/chosenId/' + itm.id,
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

// <------------------------------------------------GALLERY DETAIL - START------------------------------------------------>

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

function galleryDetail(i) {
    $('#galleryDetail').modal('toggle')
    $('#imagesChose').empty()
    const itm = globalGalleryArr[i]
    let html = ``
    var fileName
    $.ajax({
        url: './gallery/chosenId/' + itm.id,
        type: 'get',
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            console.log(data)
            $.ajax({
                url: "./gallery/detail/" + itm.id,
                type: "get",
                dataType: "json",
                contentType: 'application/json; charset=utf-8',
                success: function (data) {
                    console.log(data.result)
                    for (let j = 0; j < data.result.fileName.length; j++) {
                        fileName = data.result.fileName[j]
                        html += `<div class="swiper-slide">
                             <img src="/gallery/galleryDetail/get_image/id=${itm.id}name=${fileName}" class="img-fluid" alt="banner" />

                         </div>`

                        $('#imagesChose').append('<option value="' + fileName + '">Image -  ' + j + '</option>');

                    }
                    $("#imageSlide").html(html)
                    $("#title").text(itm.no + " - " + itm.title + "  " + itm.description)
                    $("#titleDetail").text(itm.title)
                    $("#dateDetail").text(itm.date)
                    $("#statusDetail").text(itm.status)
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

// <------------------------------------------------GALLERY DETAIL - END-------------------------------------------------->

// <------------------------------------------------GALLERY SEARCH - START------------------------------------------------>

function searchGallery(page, showPageSize, searchData) {

    $.ajax({
        url: './gallery/search/' + searchData + "/" + (page - 1) + "/" + showPageSize,
        type: 'get',
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            createRowDataGallery(data)
            dynamicPaginationGallery(data.totalPage, showPageSize)
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

$('#searchGalleryData').keyup(function (event) {
    console.log("değişti")
    event.preventDefault();
    const searchDataGallery = $(this).val()

    if (searchDataGallery !== "") {
        $("#galleryTable > tr").remove()
        $('#pagination_gallery').twbsPagination('destroy');
        searchGallery(1, $("#showGalleryTableRow").val(), searchDataGallery)
    } else {
        $('#pagination_gallery').twbsPagination('destroy');
        getAllGalleryByPage(1, $("#showGalleryTableRow").val());
    }
})

// <------------------------------------------------GALLERY SEARCH - END-------------------------------------------------->

// <------------------------------------------------GALLERY UPDATE - START------------------------------------------------>

function galleryUpdate(i) {
    $('#galleryAdd_modal').modal('toggle');
    const itm = globalGalleryArr[i]
    select_id_gallery = itm.id
    console.log("select_id : " + select_id_gallery)
    $.ajax({
        url: "./gallery/detail/" + itm.id,
        type: "get",
        dataType: "json",
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            console.log(data.result)

            $('#title').val(data.result.title)
            $('#detail').val(data.result.description)
            $("#status").val(data.result.status == "Active" ? 1 : 2)
            $('#galleryCategories').val(data.result.galleryCategory.id).trigger('change');
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

// <------------------------------------------------GALLERY UPDATE - END-------------------------------------------------->

// <------------------------------------------------GALLERY IMAGE DELETE - START------------------------------------------->

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
                url: './gallery/chosenImages/delete/' + chosenImages,
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
                            $("#galleryDetail").modal('hide');
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

// <------------------------------------------------GALLERY IMAGE DELETE - END-------------------------------------------->

