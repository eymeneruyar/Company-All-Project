function getAllAdvertisementsByPage(page, size) {
    $.ajax({
        url: '/advertisement/all' + '/status=' + selectedAdvertisementStatus + '/page=' + page + 'size=' + size,
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            console.log(data.result)
            createAdvertisementTable(data.result);
            dynamicPagination(data.totalPage, size);
        },
        error: function (err) {
            console.log(err)
        }
    })

}

function saveAdvertisement(advertisementObj) {

    let url;
    let state;
    if(advertisementObj["id"] === undefined){
        url = '/advertisement/add';
        state = 1;
    }else{
        url = '/advertisement/update';
        state = 2;
    }

    $.ajax({
        url: url,
        type: 'POST',
        data: JSON.stringify(advertisementObj),
        dataType: 'JSON',
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            if (data.status === true) {
                let formData = new FormData();
                let image = document.getElementById("image");
                if(image.files[0] !== undefined && state === 2){
                    formData.append("image", image.files[0]);
                    imageUpload(formData, data.result, state);
                }
                if(state === 1){ // add
                    formData.append("image", image.files[0]);
                    imageUpload(formData, data.result, state);
                }

            } else {
                if (!jQuery.isEmptyObject(data.errors)) {
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
            console.log("Error" + err);
        }
    })
}

function imageUpload(formData, aid, state) {

    let url;
    if(state === 1){
        url = '/advertisement/image_upload/advertisement_id=';
    }else{
        url = '/advertisement/image_update/advertisement_id=';
    }

    $.ajax({
        url: url + aid,
        type: "POST",
        headers: {'IsAjax': 'true'},
        dataType: "json",
        processData: false,
        contentType: false,
        data: formData,
        success: function (data) {
            if (data.status === true) {
                Swal.fire({
                    title: 'Success!',
                    text: data.message,
                    icon: "success",
                    customClass: {
                        confirmButton: 'btn btn-primary'
                    },
                    buttonsStyling: false
                });
                getAllAdvertisementsByPage(0, $('#advertisement_pagesize').val());
                $('#advertisement_add_form').trigger("reset");
                setTimeout(function () {
                    $("#advertisement_form_modal").modal('hide');
                }, 0);
                selectedAdvertisement = {};
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
        },
        error: function () {
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the operation!",
                icon: "error",
                customClass: {
                    confirmButton: 'btn btn-primary'
                },
                buttonsStyling: false
            });
        }
    });
}

function getAdvertisement(aid, index) {

    if(index === 3 && !jQuery.isEmptyObject(selectedAdvertisement)){
        getImage(selectedAdvertisement.image);
        return;
    }

    $.ajax({
        url: '/advertisement/get' + '/id=' + aid,
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            if(data.status === true){
                selectedAdvertisement = data.result;
                if(index === 0){ //detail
                    createAdvertisementDetailsModal();
                }
                if(index === 1){ //update
                    createAdvertisementEditModal();
                }
                if(index === 3){ // show image
                    getImage(selectedAdvertisement.image);
                }
            }else{
                console.log(data.message);
            }
        },
        error: function (err) {
            console.log(err)
        }
    })
    return {};
}

function getImage(image) {
    $.ajax({
        url: '/advertisement/get/image/name=' + image,
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            createImageModal(data.result);
        },
        error: function (err) {
            console.log(err)
        }
    })
}

function deleteAdvertisement(aid) {
    Swal.fire({
        title: 'Are you sure?',
        text: "This advertisement will be permanently deleted!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Delete',
        customClass: {
            confirmButton: 'btn btn-primary',
            cancelButton: 'btn btn-outline-danger ml-1'
        },
        buttonsStyling: false
    }).then(function (result) {
        if (result.value) {
            $.ajax({
                url: '/advertisement/delete/id=' + aid,
                type: 'DELETE',
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                success: function (data) {
                    if( data.status === true){
                        Swal.fire({
                            icon: 'success',
                            title: "Deleted!",
                            text: data.message,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        });
                        getAllAdvertisementsByPage(0, $('#advertisement_pagesize').val());
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

function searchAdvertisementByKey(key, page, size) {

    $.ajax({
        url: '/advertisement/search' + '/key=' + key + '/status=' + selectedAdvertisementStatus + '/page=' + page + 'size=' + size,
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            createAdvertisementTable(data.result);
            dynamicPagination(data.totalPage, size);
        },
        error: function (err) {
            console.log(err)
        }
    })
}

function codeGenerator() {
    const date = new Date();
    const time = date.getTime();
    return time.toString().substring(3);
}

function dynamicPagination(totalPage, size) {
    $('#advertisement_pagination').twbsPagination({
        totalPages: totalPage,
        visiblePages: 5,
        prev: 'Prev',
        first: 'First',
        last: 'Last',
        startPage: 1,
        onPageClick: function (event, page) {
            if($('#advertisement_search').val() === ""){
                getAllAdvertisementsByPage(page-1, size);
            }else{
                searchAdvertisementByKey($('#advertisement_search').val(), page-1, $('#advertisement_pagesize').val());
            }

            //$('#firstLast1-content').text('You are on Page ' + page);
            $('.pagination').find('li').addClass('page-item');
            $('.pagination').find('a').addClass('page-link');
        }
    });

}

function createAdvertisementTable(advertisements) {
    let html = ``;
    for (let i = 0; i < advertisements.length; i++) {
        const advertisement = advertisements[i];
        html += `                <tr>
                                    <td>` + advertisement.no + `</td>
                                    <td>` + advertisement.name + `</td>
                                    <td>` + advertisement.view + `</td>
                                    <td>` + advertisement.click + `</td>
                                    <td>
                                        <a href="javascript:getAdvertisement(`+advertisement.aid+`, `+3+`);">
                                            <i class="far fa-eye" style="font-size: 20px;" ></i>
                                        </a>
                                    </td>
                                    <td>` + advertisement.width + `</td>
                                    <td>` + advertisement.height + `</td>
                                    <td>` + advertisement.status + `</td>
                                    <td>
                                        <div class="dropdown">
                                            <button type="button" class="btn btn-sm dropdown-toggle hide-arrow"
                                                data-toggle="dropdown">
                                                <i class="fas fa-ellipsis-v"></i>
                                            </button>
                                            <div class="dropdown-menu">
                                                <a class="dropdown-item" href="javascript:getAdvertisement(`+advertisement.aid+`, `+0+`);">
                                                    <i class="mr-50 fas fa-info-circle"></i>
                                                     <span>Info</span>
                                                </a>
                                                <a class="dropdown-item" href="javascript:getAdvertisement(`+advertisement.aid+`, `+1+`);">
                                                    <i class="mr-50 fas fa-pen"></i>
                                                    <span>Edit</span>
                                                </a>
                                                <a class="dropdown-item" href="javascript:deleteAdvertisement(`+advertisement.aid+`);">
                                                    <i class="mr-50 far fa-trash-alt"></i>
                                                    <span>Delete</span>
                                                </a>
                                            </div>
                                        </div>
                                    </td>
                                </tr>`;
    }
    $('#tBodyAdvertisement').html(html);
}

function createAdvertisementDetailsModal() {
    $("#detail_name").text(selectedAdvertisement.name);
    $("#detail_view").text( selectedAdvertisement.view);
    $("#detail_startdate").text( selectedAdvertisement.startDate);
    $("#detail_finishdate").text( selectedAdvertisement.finishDate);
    $("#detail_width").text( selectedAdvertisement.width);
    $("#detail_height").text( selectedAdvertisement.height);
    $("#detail_link").text( selectedAdvertisement.link);
    $("#detail_click").text( selectedAdvertisement.click);
    $("#detail_status").text( selectedAdvertisement.status);
    $("#advertisement_detail_modal_title").text( "Advertisement No : " + selectedAdvertisement.no);
    $('#advertisement_detail_modal').modal('toggle');
}

function createAdvertisementEditModal() {
    $("#name").val(selectedAdvertisement.name);
    $("#view").val( selectedAdvertisement.view);
    $("#startDate").val( selectedAdvertisement.startDate);
    $("#finishDate").val( selectedAdvertisement.finishDate);
    $("#width").val( selectedAdvertisement.width);
    $("#height").val( selectedAdvertisement.height);
    $("#link").val( selectedAdvertisement.link);
    $("#advertisement_form_modal_title").text("Edit Advertisement - No : " + selectedAdvertisement.no);
    $('#advertisement_form_modal').modal('toggle');
}

function createImageModal(bytes) {
    $("#advertisement_image").attr("src", "data:image/jpg;base64," + bytes);
    $("#advertisement_image_modal_title").text(selectedAdvertisement.name);
    $('#advertisement_image_modal').modal('toggle');
}

$('#advertisement_add_form').submit((event) => {
    event.preventDefault();
    const name = $("#name").val();
    const view = $("#view").val();
    const startDate = $("#startDate").val();
    const finishDate = $("#finishDate").val();
    const width = $("#width").val();
    const height = $("#height").val();
    const link = $("#link").val();

    const advertisementObj = {
        name: name,
        view: view,
        startDate: startDate,
        finishDate: finishDate,
        width: width,
        height: height,
        link: link,
        click: 0,
        status: "Active",
    }
    if(jQuery.isEmptyObject(selectedAdvertisement)){
        advertisementObj["no"] = codeGenerator();
        advertisementObj["image"] = "";
    }else{
        advertisementObj["id"] = selectedAdvertisement.id;
        advertisementObj["no"] = selectedAdvertisement.no;
        advertisementObj["image"] = selectedAdvertisement.image;
    }
    saveAdvertisement(advertisementObj);
});

$('#advertisement_pagesize').change(function () {
    $('#advertisement_pagination').twbsPagination('destroy');
    getAllAdvertisementsByPage(0, parseInt($(this).val()));
});

$('#advertisement_status').change(function(){
    selectedAdvertisementStatus = $(this).val();
    $("#tBodyAdvertisement > tr").remove();
    getAllAdvertisementsByPage(0, $('#advertisement_pagesize').val());
});
$('#advertisement_search').keyup( function (event) {
    event.preventDefault();
    const key = $(this).val();
    if(key !== ""){
        $("#tBodyAdvertisement > tr").remove();
        $('#advertisement_pagination').twbsPagination('destroy');
        searchAdvertisementByKey(key, 0, $('#advertisement_pagesize').val());
    }else{
        $('#advertisement_pagination').twbsPagination('destroy');
        getAllAdvertisementsByPage(0, $('#advertisement_pagesize').val());
    }
});

$( "#advertisement_form_modal_button" ).click(function() {
    $('#advertisement_add_form').trigger("reset");
    $("#advertisement_form_modal_title").text("New Advertisement");
    $('#advertisement_form_modal').modal('toggle');
});

let selectedAdvertisement = {};
let selectedAdvertisementStatus = "Active";
getAllAdvertisementsByPage(0, 10);