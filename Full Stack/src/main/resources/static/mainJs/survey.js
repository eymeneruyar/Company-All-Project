$('#survey_pagination').twbsPagination('destroy');
getAllSurveysByPage(1, $("#survey_pagesize").val());
// Functions - Start
function noGenerator() {
    const date = new Date();
    const time = date.getTime();
    const key = time.toString().substring(4);
    return key;
}

function fncDate() {
    const d = new Date();
    const ye = new Intl.DateTimeFormat('tr', {year: 'numeric'}).format(d)
    const mo = new Intl.DateTimeFormat('tr', {month: '2-digit'}).format(d)
    const da = new Intl.DateTimeFormat('tr', {day: '2-digit'}).format(d)
    const date = `${da}-${mo}-${ye}`
    return date;
}

function fncReset() {
    $("#surveyForm").trigger("reset")
    select_id = 0
}

// Functions - End

/*------------------------------ Survey Add - Start ------------------------------*/
let select_id = 0
let globalArrSurvey = []
var success = $('#type-success');
if (success.length) {
    $('#surveyForm').submit((event) => {
        event.preventDefault()
        console.log("tıklandı");

        const title = $("#title").val()
        const detail = $("#detail").val()
        const status = $("#status").val()

        const obj = {
            title: title,
            detail: detail,
            status: status == 1 ? "Active" : "Passive",
            no: noGenerator(),
            date: fncDate(),
        }
        if (select_id != 0) {
            // update
            obj["id"] = select_id;
        }
        $.ajax({
            url: './surveys/add',
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
                        buttonStyling: false

                    });
                    getAllSurveysByPage(0, 10);
                } else if (data.status == true && data.result == null) {
                    Swal.fire({
                        title: "Warning!",
                        text: "Returned Data is Empty!",
                        icon: "Warning",
                        customClass: {
                            confirmButton: 'btn btn-primary'
                        },
                        buttonStyling: false
                    });
                } else {
                    Swal.fire({
                        title: 'Error!',
                        text: data.message,
                        icon: "error",
                        customClass: {
                            confirmButton: 'btn btn-primary'
                        },
                        buttonStyling: false
                    });
                }

                console.log(data);
                fncReset();
            },
            error: function (err) {
                Swal.fire({
                    title: "Error!",
                    text: "An Error occurred during the operation!",
                    icon: "error",
                    customClass: {
                        confirmButton: 'btn btn-primary'
                    },

                    buttonStyling: false
                });
                console.log(err)
            }

        })

    });



    /*------------------------------ Survey Data To Table - Start ------------------------------*/
    function createRowToTable(data) {
        let html = ``
        console.log("createRowToTable")
        console.log(data)
        data.forEach((row) => {
            html += `<tr role="row" class="odd">
                <td>` + row.no + `</td>
                <td>` + row.title + `</td>
                <td>` + row.detail + `</td>
                <td>` + row.status + `</td>
                <td>` + row.date + `</td>

                <td class="text-left">
                    <div class="dropdown">
                        <button type="button" class="btn btn-sm dropdown-toggle hide-arrow" data-toggle="dropdown">
                           <i class="fas fa-ellipsis-v"></i>
                        </button>
                        <div class="dropdown-menu">
                           <a class="dropdown-item" type="button" href="javascript:surveyUpdate(${row.id})   ">
                               <i class="mr-50 fas fa-pen"></i>
                               <span>Edit</span>
                           </a>
                           <a class="dropdown-item" href="javascript:surveyOption(${row.id})">
                               <i class="mr-50 fas fa-plus"></i>
                               <span>Add Option</span>
                           </a>
                           <a class="dropdown-item"  type="button"  href="javascript:surveyDelete(${row.id})" >
                               <i class="mr-50 far fa-trash-alt"></i>
                               <span>Delete</span>
                           </a>
                           </div>
                        </div>
                </td>
                </tr>`;


        })
        $('#surveyTableBody').html(html);
    }

    /*------------------------------ Survey Data To Table - End ------------------------------*/

}
/*------------------------------ Survey Add - Start ------------------------------*/

/*------------------------------ Survey List Pageble - Start ------------------------------*/
function getAllSurveysByPage(page, size) {

    $.ajax({
        url: '/surveys/all/' + page + '/' + size,
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            createRowData(data)

            //createRowToTable(data.result);
            dynamicPagination(data.totalPage, size);
            globalArrSurvey = data.result;
        },
        error: function (err) {
            console.log(err)
        }
    })
}

/*------------------------------ Survey List Pageble - End ------------------------------*/

/*------------------------------ Survey Delete - Start ------------------------------*/
function surveyDelete(id) {
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
                url: './surveys/delete/' + id,
                type: 'delete',
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
                        getAllSurveysByPage(0, 10);
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

/*------------------------------ Survey Delete - End ------------------------------*/


/*------------------------------ Survey Update - Start ------------------------------*/
function surveyUpdate(id) {
    $('#surveyAdd_modal').modal("toggle");

    const itm = globalArrSurvey.find(item => id);
    console.log("itm : ", itm)
    select_id = itm.id;
    $("#title").val(itm.title);

    $("#detail").val(itm.detail);

    $("#status").val(itm.status == "Active" ? 1 : 2)

    $(window).scrollTop(0);
}

/*------------------------------ Survey Update - End ------------------------------*/

/*------------------------------ Survey List Pagination - Start ------------------------------*/
let globalArr=[]
function dynamicPagination(totalPage, size) {
    $('#survey_pagination').twbsPagination({
        totalPages: totalPage,
        visiblePages: 5,
        prev: 'Prev',
        first: 'First',
        last: 'Last',
        startPage: 1,
        onPageClick: function (event, page) {
            //console.log("Page click");
            if($('#searchSurvey').val() === ""){
                getAllSurveysByPage(page - 1, size);
            }else{
                search(page,size,$('#searchSurvey').val())
            }
            //$('#firstLast1-content').text('You are on Page ' + page);
            $('.pagination').find('li').addClass('page-item');
            $('.pagination').find('a').addClass('page-link');
        }
    });
}

$('#survey_pagesize').change(function () {
    if($('#searchSurvey').val() === ""){
        $('#survey_pagination').twbsPagination('destroy');
        getAllSurveysByPage(0, parseInt($(this).val()));
    }else{
        search( page, $('#survey_pagesize').val(),$('#searchSurvey').val());
    }
    //console.log("Show number Change " + parseInt($(this).val()));
});
getAllSurveysByPage(0, 10);
/*------------------------------ Survey List Pagination - End ------------------------------*/


/*------------------------------------------------------------  SURVEY OPTİON - Start ------------------------------------------------------------*/

/*------------------------------ Survey Option Add - Start ------------------------------*/
var success1 = $('#type-success1');
if (success1.length) {
    $('#surveyOptionAddForm').submit((event) => {
        event.preventDefault()
        console.log(globalOptionId);
        const titleOption = $("#titleOption").val()
        const obj = {
            title: titleOption,
            date: fncDate(),
            no: noGenerator(),
            survey: {
                id: globalOptionId
            }
        }
        console.log(surveyOption.surveyId)
        $.ajax({
            url: './surveysOption/add',
            type: 'POST',
            data: JSON.stringify(obj),
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            success: function (data) {
                if (data.status === true && data.result != null) {
                    Swal.fire({
                        title: 'Success!',
                        text: data.message,
                        icon: "success",
                        customClass: {
                            confirmButton: 'btn btn-primary'
                        },
                        buttonStyling: false
                    });
                } else if (data.status == true && data.result == null) {
                    Swal.fire({
                        title: "Warning!",
                        text: "Returned Data is Empty!",
                        icon: "Warning",
                        customClass: {
                            confirmButton: 'btn btn-primary'
                        },
                        buttonStyling: false
                    });
                } else {
                    Swal.fire({
                        title: 'Error!',
                        text: data.message,
                        icon: "error",
                        customClass: {
                            confirmButton: 'btn btn-primary'
                        },
                        buttonStyling: false
                    });
                }
                updateModal(globalOptionId)
            },
            error: function (err) {
                Swal.fire({
                    title: "Error!",
                    text: "An Error occurred during the operation!",
                    icon: "error",
                    customClass: {
                        confirmButton: 'btn btn-primary'
                    },

                    buttonStyling: false
                });
                console.log(err)
            }
        })
    });


    /*------------------------------ Survey Option Add - End ------------------------------*/


    /*------------------------------ Survey Option List By SurveyId - Start ------------------------------*/
    let globalOptionSurvey;

    function optionsBySurveyId(surveyId) {
        var output;
        $.ajax({
            url: './surveysOption/list/' + surveyId,
            type: 'GET',
            contentType: 'application/json; charset=utf-8',
            async: false,
            success: function (data) {
                output = data;
                globalOptionSurvey = output;
                console.log("option data list:" + data);
            },
            error: function (err) {
                console.log("allBuy Error : " + err)
            }
        })
        return output;
    }

    /*------------------------------ Survey Option List By SurveyId - End ------------------------------*/

    /*------------------------------ Survey Option Add Table - Start ------------------------------*/
    var globalOptionId;

    function surveyOption(id) {
        optionsBySurveyId(id);
        globalOptionId = id;
        console.log(id);
        $(' #surveyOptionAdd_modal').modal("toggle");

        let html = ``
        console.log(globalOptionSurvey)
        globalOptionSurvey.forEach((row) => {
            html += `<tr role="row" class="odd">
                <td>` + row.survey.title + `</td>
                <td>` + row.title + `</td>
                <td>` + row.vote + `</td>

                <td class="text-left">
                    <div class="dropdown">
                        <button type="button" class="btn btn-sm dropdown-toggle hide-arrow" data-toggle="dropdown">
                           <i class="fas fa-ellipsis-v"></i>
                        </button>
                        <div class="dropdown-menu">
                           <a class="dropdown-item"  type="button"  href="javascript:surveyOptionDelete(${row.id})" >
                               <i class="mr-50 far fa-trash-alt"></i>
                               <span>Delete</span>
                           </a>
                           </div>
                        </div>
                </td>
                </tr>`;
        })
        $('#surveyOptionTableBody').html(html);
    }

    /*------------------------------ Survey Option Add Table - End ------------------------------*/

    /*------------------------------ Survey Option Modal Update(add-delete => Refresh) - Start ------------------------------*/
    function updateModal(globalOptionId) {
        optionsBySurveyId(globalOptionId);
        let html = ``
        console.log(globalOptionSurvey)
        globalOptionSurvey.forEach((row) => {
            html += `<tr role="row" class="odd">
                <td>` + row.survey.title + `</td>
                <td>` + row.title + `</td>
                <td>` + row.vote + `</td>

                <td class="text-left">
                    <div class="dropdown">
                        <button type="button" class="btn btn-sm dropdown-toggle hide-arrow" data-toggle="dropdown">
                           <i class="fas fa-ellipsis-v"></i>
                        </button>
                        <div class="dropdown-menu">
                           <a class="dropdown-item"  type="button"  href="javascript:surveyOptionDelete(${row.id})" >
                               <i class="mr-50 far fa-trash-alt"></i>
                               <span>Delete</span>
                           </a>
                           </div>
                        </div>
                </td>
                </tr>`;
        })
        $('#surveyOptionTableBody').html(html);
    }

    /*------------------------------ Survey Option Modal Update(add-delete => Refresh) - End ------------------------------*/


    /*------------------------------ Survey Option Delete - Start ------------------------------*/
    function surveyOptionDelete(id) {
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
                    url: './surveysOption/delete/' + id,
                    type: 'delete',
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
                            updateModal(globalOptionId)
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
}
/*------------------------------ Survey Option Delete - End ------------------------------*/

/*------------------------------------------------------------  SURVEY OPTİON - End ------------------------------------------------------------*/


//-------------------------------------- Survey Search - Start --------------------------------------------//

function search(page, showPageSize, searchData) {

    //console.log(searchData)

    $.ajax({
        url: './surveys/search/' + searchData + "/" + (page - 1) + "/" + showPageSize,
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

$('#searchSurvey').keyup(function (event) {
    event.preventDefault();
    const searchData = $(this).val()
    console.log("Key " + searchData)
    if (searchData !== "") {
        $("#surveyTableBody > tr").remove()
        $('#survey_pagination').twbsPagination('destroy');
        search(1, $("#survey_pagesize").val(), searchData)
    } else {
        $('#survey_pagination').twbsPagination('destroy');
        getAllSurveysByPage(1, $("#survey_pagesize").val());
    }
})

//-------------------------------------- Survey Search - End ----------------------------------------------//

//-------------------------------------- Survey Create Row - Start ------------------------------------------//
function createRowData(data) {
    let html = ``
    //console.log("createRowData")
    //console.log(data)
    for (let i = 0; i < data.result.length; i++) {
        globalArr = data.result
        const itm = data.result[i]
        //console.log(itm.title + " " +  itm.details)
        html += `<tr>
                      <td>` + itm.no + `</td>
                <td>` + itm.title + `</td>
                <td>` + itm.detail + `</td>
                <td>` + itm.status + `</td>
                <td>` + itm.date + `</td>
                      <td class="text-left">
                    <div class="dropdown">
                        <button type="button" class="btn btn-sm dropdown-toggle hide-arrow" data-toggle="dropdown">
                           <i class="fas fa-ellipsis-v"></i>
                        </button>
                        <div class="dropdown-menu">
                           <a class="dropdown-item" type="button" href="javascript:surveyUpdate(${itm.id})   ">
                               <i class="mr-50 fas fa-pen"></i>
                               <span>Edit</span>
                           </a>
                           <a class="dropdown-item" href="javascript:surveyOption(${itm.id})">
                               <i class="mr-50 fas fa-plus"></i>
                               <span>Add Option</span>
                           </a>
                           <a class="dropdown-item"  type="button"  href="javascript:surveyDelete(${itm.id})" >
                               <i class="mr-50 far fa-trash-alt"></i>
                               <span>Delete</span>
                           </a>
                           </div>
                        </div>
                </td>
               </tr>`
    }
    $("#surveyTableBody").html(html)
}

//-------------------------------------- Survey Create Row - End --------------------------------------------//

