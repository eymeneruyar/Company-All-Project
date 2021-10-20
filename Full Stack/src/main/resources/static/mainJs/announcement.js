function getAllAnnouncementsByPage(page, size) {
    $.ajax({
        url: '/announcements/all' + '/status=' + selectedAnnouncementStatus + '/page=' + page + 'size=' + size,
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            console.log(data.result)
            createAnnouncementTable(data.result);
            dynamicPagination(data.totalPage, size);
        },
        error: function (err) {
            console.log(err)
        }
    })
}

function searchAnnouncementByKey(key, page, size) {

    $.ajax({
        url: '/announcements/search' + '/key=' + key + '/status=' + selectedAnnouncementStatus + '/page=' + page + 'size=' + size,
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            createAnnouncementTable(data.result);
            dynamicPagination(data.totalPage, size);
        },
        error: function (err) {
            console.log(err)
        }
    })
}

function saveAnnouncement(announcementObj) {

    let url;
    if(announcementObj["id"] === undefined){
        url = '/announcements/add';
    }else{
        url = '/announcements/update';
    }

    $.ajax({
        url: url,
        type: 'POST',
        data: JSON.stringify(announcementObj),
        dataType: 'JSON',
        contentType: 'application/json; charset=utf-8',
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
                getAllAnnouncementsByPage(0, $('#announcement_pagesize').val());
                $('#announcement_add_form').trigger("reset");
                editor.setText('');

                setTimeout(function () {
                    $("#announcement_form_modal").modal('hide');
                }, 0);
                selectedAnnouncement = {};

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
            console.log(err);
        }
    })
}

function deleteAnnouncement(aid) {
    Swal.fire({
        title: 'Are you sure?',
        text: "This announcement will be permanently deleted!",
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
                url: '/announcements/delete/id=' + aid,
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
                        getAllAnnouncementsByPage(0, $('#announcement_pagesize').val());
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

function getAnnouncement(aid, index) {

    $.ajax({
        url: '/announcements/get' + '/id=' + aid,
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            if(data.status === true){
                selectedAnnouncement = data.result;
                if(index === 0){ //detail
                    createAnnouncementDetailsModal();
                }
                if(index === 1){ //update
                    createAnnouncementEditModal();
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

function codeGenerator() {
    const date = new Date();
    const time = date.getTime();
    return time.toString().substring(3);
}

function dynamicPagination(totalPage, size) {
    $('#announcement_pagination').twbsPagination({
        totalPages: totalPage,
        visiblePages: 5,
        prev: 'Prev',
        first: 'First',
        last: 'Last',
        startPage: 1,
        onPageClick: function (event, page) {
            if($('#announcement_search').val() === ""){
                getAllAnnouncementsByPage(page-1, size);
            }else{
                searchAnnouncementByKey($('#advertisement_search').val(), page-1, $('#advertisement_pagesize').val());
            }

            //$('#firstLast1-content').text('You are on Page ' + page);
            $('.pagination').find('li').addClass('page-item');
            $('.pagination').find('a').addClass('page-link');
        }
    });
}

function createAnnouncementTable(announcements) {
    let html = ``;
    for(let i = 0; i < announcements.length; i++){
        const announcement = announcements[i];
        html += `                <tr>
                                    <td>` + announcement.no + `</td>
                                    <td>` + announcement.title + `</td>
                                    <td>` + announcement.date + `</td>
                                    <td>` + announcement.status + `</td>
                                    <td>
                                        <a href="javascript:getAnnouncement(`+announcement.aid+`, `+0+`);">
                                            <i class="far fa-file-alt" style="font-size: 20px;"></i>
                                        </a>
                                    </td>
                                    <td>
                                        <div class="dropdown">
                                            <button type="button" class="btn btn-sm dropdown-toggle hide-arrow"
                                                data-toggle="dropdown">
                                                <i class="fas fa-ellipsis-v"></i>
                                            </button>
                                            <div class="dropdown-menu">
                                                <a class="dropdown-item" href="javascript:getAnnouncement(`+announcement.aid+`, `+1+`);">
                                                    <i class="mr-50 fas fa-pen"></i>
                                                    <span>Edit</span>
                                                </a>                                                
                                                <a class="dropdown-item" href="javascript:deleteAnnouncement(`+announcement.aid+`);">
                                                    <i class="mr-50 far fa-trash-alt"></i>
                                                    <span>Delete</span>
                                                </a>
                                            </div>
                                        </div>
                                    </td>
                                </tr>`;
    }
    $('#tBodyAnnouncements').html(html);
}

function createAnnouncementDetailsModal() {
    $("#detail_detail").text(selectedAnnouncement.detail);
    $("#announcement_detail_modal_title").text( "Announcement No : " + selectedAnnouncement.no);
    $('#announcement_detail_modal').modal('toggle');
}

function createAnnouncementEditModal() {
    $("#title").val(selectedAnnouncement.title);
    editor.setText(selectedAnnouncement.detail);
    $("#announcement_form_modal_title").text("Edit Announcement - No : " + selectedAnnouncement.no);
    $('#announcement_form_modal').modal('toggle');
}

$('#announcement_add_form').submit((event) => {
    event.preventDefault();
    const title = $("#title").val();


    const announcementObj = {
        title: title,
        detail: editorText,
        status: "Active"
    }

    if(jQuery.isEmptyObject(selectedAnnouncement)){
        announcementObj["no"] = codeGenerator();
    }else{
        announcementObj["id"] = selectedAnnouncement.id;
        announcementObj["no"] = selectedAnnouncement.no;
        announcementObj["date"] = selectedAnnouncement.date;
    }
    saveAnnouncement(announcementObj);
});

$('#announcement_pagesize').change(function () {
    $('#announcement_pagination').twbsPagination('destroy');
    getAllAnnouncementsByPage(0, parseInt($(this).val()));
});

$('#announcement_status').change(function(){
    selectedAnnouncementStatus = $(this).val();
    $("#tBodyAnnouncements > tr").remove();
    getAllAnnouncementsByPage(0, $('#announcement_pagesize').val());
});

$('#announcement_search').keyup( function (event) {
    event.preventDefault();
    const key = $(this).val();
    if(key !== ""){
        $("#tBodyAnnouncements > tr").remove();
        $('#announcement_pagination').twbsPagination('destroy');
        searchAnnouncementByKey(key, 0, $('#announcement_pagesize').val());
    }else{
        $('#announcement_pagination').twbsPagination('destroy');
        getAllAnnouncementsByPage(0, $('#announcement_pagesize').val());
    }
});

$( "#announcement_form_modal_button" ).click(function() {
    $('#announcement_add_form').trigger("reset");
    $("#announcement_form_modal_title").text("New Announcement");
    $('#announcement_form_modal').modal('toggle');
});

let options = {
    placeholder: 'Enter the announcement details...',
    theme: 'snow'
};
const editor = new Quill('#detail', options);
editor.on('text-change', function() {
    editorText = editor.getText();
});

let selectedAnnouncement = {};
let selectedAnnouncementStatus = "Active";
let editorText = "";
getAllAnnouncementsByPage(0, 10);

