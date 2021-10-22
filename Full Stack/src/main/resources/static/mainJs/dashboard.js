
//--------------------------------------- Definition of Days and Months - Start ---------------------------------------//
const months = ['January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
]

const days = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']
//--------------------------------------- Definition of Days and Months - End -----------------------------------------//

//--------------------------------------- General Statics - Start ---------------------------------------//
function generalStatics(){

    $.ajax({
        url: './dashboard/generalStatics',
        type: 'get',
        dataType: "json",
        contentType : 'application/json; charset=utf-8',
        success: function (data) {
            //console.log(data)
            html = `<div class="row">
                        <div class="col-xl-3 col-sm-6 col-12 mb-2 mb-xl-0">
                            <div class="media">
                                <div class="avatar bg-light-primary mr-2">
                                    <div class="avatar-content">
                                        <i data-feather="thumbs-up" class="avatar-icon"></i>
                                    </div>
                                </div>
                                <div class="media-body my-auto">
                                    <h4 class="font-weight-bolder mb-0">${data.totalLikes}</h4>
                                    <p class="card-text font-small-3 mb-0">Likes</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-xl-3 col-sm-6 col-12 mb-2 mb-xl-0">
                            <div class="media">
                                <div class="avatar bg-light-info mr-2">
                                    <div class="avatar-content">
                                        <i data-feather="user" class="avatar-icon"></i>
                                    </div>
                                </div>
                                <div class="media-body my-auto">
                                    <h4 class="font-weight-bolder mb-0">${data.totalCustomers}</h4>
                                    <p class="card-text font-small-3 mb-0">Customers</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-xl-3 col-sm-6 col-12 mb-2 mb-sm-0">
                            <div class="media">
                                <div class="avatar bg-light-danger mr-2">
                                    <div class="avatar-content">
                                        <i data-feather="box" class="avatar-icon"></i>
                                    </div>
                                </div>
                                <div class="media-body my-auto">
                                    <h4 class="font-weight-bolder mb-0">${data.totalOrders}</h4>
                                    <p class="card-text font-small-3 mb-0">Orders</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-xl-3 col-sm-6 col-12">
                            <div class="media">
                                <div class="avatar bg-light-success mr-2">
                                    <div class="avatar-content">
                                        <i data-feather="clipboard" class="avatar-icon"></i>
                                    </div>
                                </div>
                                <div class="media-body my-auto">
                                    <h4 class="font-weight-bolder mb-0">${data.totalContents}</h4>
                                    <p class="card-text font-small-3 mb-0">Contents</p>
                                </div>
                            </div>
                        </div>
                    </div>`
            $("#generalStatics").html(html)
        },
        error: function (err) {
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the general statics listing operation!",
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
generalStatics()
//--------------------------------------- General Statics - End -----------------------------------------//

//--------------------------------------- Last Added Six Product - Start -----------------------------------------//
function lastAddSixProduct(){

    $.ajax({
        url: './dashboard/lastAddSixProduct',
        type: 'get',
        dataType: "json",
        contentType : 'application/json; charset=utf-8',
        success: function (data) {
            //console.log(data)
            createTableLastAddSixProduct(data.lastAddedSixProduct)
        },
        error: function (err) {
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the last added six product listing operation!",
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

function createTableLastAddSixProduct(data){
    let html = ``
    for (let i = 0; i < data.length; i++){
        const itm = data[i]
        let price = priceFormatter(itm.price)
        var returnData = categoryInfo(itm.productCategoryId[0])
        html += `<tr>
                     <td>
                         <div class="d-flex align-items-center">
                             <div>
                                 <div class="font-weight-bolder">${itm.name}</div>
                             </div>
                         </div>
                     </td>
                     <td class="text-nowrap">
                         <div class="d-flex flex-column">
                             <span class="font-weight-bolder mb-25">${itm.description}</span>
                         </div>
                     </td>
                     <td>
                         <div class="d-flex align-items-center">
                             <span>${returnData.name}</span>
                         </div>
                     </td>
                     <td>${price} TL</td>
                     <td>
                         <div class="d-flex align-items-center">
                             <span class="font-weight-bolder mr-1">${itm.date}</span>
                         </div>
                     </td>
                 </tr>`
    }
    $("#lastAddSixProduct").html(html)
}
lastAddSixProduct()
//--------------------------------------- Last Added Six Product - End -------------------------------------------//

//--------------------------------------- Last Six Order - Start -------------------------------------------//
function lastSixOrder(){

    $.ajax({
        url: './dashboard/lastSixOrder',
        type: 'get',
        dataType: "json",
        contentType : 'application/json; charset=utf-8',
        success: function (data) {
            //console.log(data)
            //createTableLastSixOrder(data)
        },
        error: function (err) {
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the last six order listing operation!",
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

function createTableLastSixOrder(){

}
lastSixOrder()
//--------------------------------------- Last Six Order - End ---------------------------------------------//

//--------------------------------------- Category Information - Start ---------------------------------------------//
function categoryInfo(id){

    var returnData

    $.ajax({
        url: './dashboard/productCategoryInfo/' + id,
        type: 'get',
        dataType: "json",
        async: false,
        contentType : 'application/json; charset=utf-8',
        success: function (data) {
            //console.log(data)
            returnData = data.result
            //createTableLastSixOrder(data)
        },
        error: function (err) {
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the last six order listing operation!",
                icon: "error",
                customClass: {
                    confirmButton: 'btn btn-primary'
                },
                buttonsStyling: false
            });
            console.log(err)
        }
    })
    return returnData

}
//--------------------------------------- Category Information - End -----------------------------------------------//

//--------------------------------------- Donut Chart Series and Labels Info - Start -----------------------------------------------//
var allCategories = allCategories()
var glabalLabel = []
var globalSeries = []
let totalProduct = 0
//console.log(allCategories)
function donutChartLabels(){
    for (let i = 0; i < allCategories.length; i++) {
        glabalLabel[i] = allCategories[i].name
        globalSeries[i] = totalProductByCategoryId(allCategories[i].categoryId)
        totalProduct += globalSeries[i]
    }
}
donutChartLabels()
//console.log(glabalLabel)
//console.log(globalSeries)
//console.log(totalProduct)
//--------------------------------------- Donut Chart Series and Labels Info - End -------------------------------------------------//

//--------------------------------------- Donut Chart for Product - Start ---------------------------------------------//
function donutChart(){

    var flatPicker = $('.flat-picker'),
        isRtl = $('html').attr('data-textdirection') === 'rtl',
        chartColors = {
            column: {
                series1: '#826af9',
                series2: '#d2b0ff',
                bg: '#f8d3ff'
            },
            success: {
                shade_100: '#7eefc7',
                shade_200: '#06774f'
            },
            donut: {
                series1: '#ffe700',
                series2: '#00d4bd',
                series3: '#826bf8',
                series4: '#2b9bf4',
                series5: '#FFA1A1'
            },
            area: {
                series3: '#a4f8cd',
                series2: '#60f2ca',
                series1: '#2bdac7'
            }
        };

    var donutChartEl = document.querySelector('#donut-chart'),
        donutChartConfig = {
            chart: {
                height: 350,
                type: 'donut'
            },
            legend: {
                show: true,
                position: 'bottom'
            },
            labels: glabalLabel,
            series: globalSeries,
            colors: [
                chartColors.donut.series1,
                chartColors.donut.series5,
                chartColors.donut.series3,
                chartColors.donut.series2
            ],
            dataLabels: {
                enabled: true,
                formatter: function (val, opt) {
                    return parseInt(val) + '%';
                }
            },
            plotOptions: {
                pie: {
                    donut: {
                        labels: {
                            show: true,
                            name: {
                                fontSize: '2rem',
                                fontFamily: 'Montserrat'
                            },
                            value: {
                                fontSize: '1rem',
                                fontFamily: 'Montserrat',
                                formatter: function (val) {
                                    return parseInt(val) ;
                                }
                            },
                            total: {
                                show: true,
                                fontSize: '1.5rem',
                                label: 'Total Product',
                                formatter: function (w) {
                                    return totalProduct;
                                }
                            }
                        }
                    }
                }
            },
            responsive: [
                {
                    breakpoint: 992,
                    options: {
                        chart: {
                            height: 380
                        }
                    }
                },
                {
                    breakpoint: 576,
                    options: {
                        chart: {
                            height: 320
                        },
                        plotOptions: {
                            pie: {
                                donut: {
                                    labels: {
                                        show: true,
                                        name: {
                                            fontSize: '1.5rem'
                                        },
                                        value: {
                                            fontSize: '1rem'
                                        },
                                        total: {
                                            fontSize: '1.5rem'
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            ]
        };
    if (typeof donutChartEl !== undefined && donutChartEl !== null) {
        var donutChart = new ApexCharts(donutChartEl, donutChartConfig);
        donutChart.render();
    }
}
donutChart()
//--------------------------------------- Donut Chart for Product - End -----------------------------------------------//

//--------------------------------------- All Categories - Start -----------------------------------------------//
function allCategories(){

    var returnData

    $.ajax({
        url: './dashboard/allProductCategories',
        type: 'get',
        dataType: "json",
        async: false,
        contentType : 'application/json; charset=utf-8',
        success: function (data) {
            //console.log(data.result)
            returnData = data.result
        },
        error: function (err) {
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the last six order listing operation!",
                icon: "error",
                customClass: {
                    confirmButton: 'btn btn-primary'
                },
                buttonsStyling: false
            });
            console.log(err)
        }
    })
    return returnData
}
//--------------------------------------- All Categories - End -------------------------------------------------//

//--------------------------------------- Total Product by Category Id - Start -------------------------------------------------//
function totalProductByCategoryId(id){

    var returnData

    $.ajax({
        url: './dashboard/totalProductByCategoryId/' + id,
        type: 'get',
        dataType: "json",
        async: false,
        contentType : 'application/json; charset=utf-8',
        success: function (data) {
            //console.log(data.result)
            returnData = data.result
        },
        error: function (err) {
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the last six order listing operation!",
                icon: "error",
                customClass: {
                    confirmButton: 'btn btn-primary'
                },
                buttonsStyling: false
            });
            console.log(err)
        }
    })
    return returnData

}
//--------------------------------------- Total Product by Category Id - End ---------------------------------------------------//

//--------------------------------------- Daily Announcment - Start ---------------------------------------------------//

function paginationAnnouncment(totalPage){

    if(totalPage > 0){
        $('#pagination_announcment').twbsPagination({
            totalPages: totalPage,
            visiblePages: 5,
            prev: 'Prev',
            first: 'First',
            last: 'Last',
            startPage: 1,
            onPageClick: function (event, page) {
                dailyAnnouncment(page-1)
                //$('#firstLast1-content').text('You are on Page ' + page);
                $('.pagination').find('li').addClass('page-item');
                $('.pagination').find('a').addClass('page-link');
            }
        });
    }


}

function dailyAnnouncment(pageNo){

    var returnData

    $.ajax({
        url: './dashboard/dailyAnnouncment/' + pageNo,
        type: 'get',
        dataType: "json",
        contentType : 'application/json; charset=utf-8',
        success: function (data) {
            //console.log(data.result)
            //console.log(data)
            if(data.status === true){
                if(data.result.length > 0){
                    paginationAnnouncment(data.totalPage)
                    createDailyAnnouncmentCard(data.result)
                }
            }

            returnData = data.result
        },
        error: function (err) {
            Swal.fire({
                title: "Error!",
                text: "An error occurred during the daily announcment listing operation!",
                icon: "error",
                customClass: {
                    confirmButton: 'btn btn-primary'
                },
                buttonsStyling: false
            });
            console.log(err)
        }
    })
    return returnData

}
dailyAnnouncment(0)


function createDailyAnnouncmentCard(data){

    const d = new Date()
    console.log(data)
    let date = data[0].date.split(" ")

    let html = ``
    if(data.length > 0){
        html = `<div class="meetup-header d-flex align-items-center">
                <div class="meetup-day">
                    <h6 class="mb-0">${days[d.getDay()]}</h6>
                    <h3 class="mb-0">${d.getDate()}</h3>
                </div>
                <div class="my-auto">
                    <h4 class="card-title mb-25">${data[0].title}</h4>
                    <p class="card-text mb-0">${data[0].detail}</p>
                </div>
            </div>
            <div class="media">
                <div class="avatar bg-light-primary rounded mr-1">
                    <div class="avatar-content">
                        <i class="far fa-calendar"></i>
                    </div>
                </div>
                <div class="media-body">
                    <h6 class="mb-0">${date[0]}</h6>
                    <small>${date[1]}</small>
                </div>
            </div>`
        $("#dailyAnnouncment").html(html)
    }


}

//--------------------------------------- Daily Announcment - End -----------------------------------------------------//


//--------------------------------------- Other Functions - Start -----------------------------------------------------//



function priceFormatter(price){
    var formattedPrice = (price).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,')
    return formattedPrice
}

function dateFormatter(){

}

//--------------------------------------- Other Functions - Start -----------------------------------------------------//