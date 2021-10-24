function getAllOrdersByPage(page, size) {
    $.ajax({
        url: '/order/all' + '/status=' + selectedOrderStatus + '/page=' + page + 'size=' + size,
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            console.log(data.result)
            createOrderTable(data.result);
            dynamicPagination(data.totalPage, size);
        },
        error: function (err) {
            console.log(err)
        }
    })
}

function searchOrderByKey(key, page, size) {

    $.ajax({
        url: '/order/search' + '/key=' + key + '/status=' + selectedOrderStatus + '/page=' + page + 'size=' + size,
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            createOrderTable(data.result);
            dynamicPagination(data.totalPage, size);
        },
        error: function (err) {
            console.log(err)
        }
    })
}

function dynamicPagination(totalPage, size) {
    $('#order_pagination').twbsPagination({
        totalPages: totalPage,
        visiblePages: 5,
        prev: 'Prev',
        first: 'First',
        last: 'Last',
        startPage: 1,
        onPageClick: function (event, page) {
            if ($('#order_search').val() === "") {
                getAllOrdersByPage(page - 1, size);
            } else {
                searchOrderByKey($('#order_search').val(), page - 1, $('#order_pagesize').val());
            }

            //$('#firstLast1-content').text('You are on Page ' + page);
            $('.pagination').find('li').addClass('page-item');
            $('.pagination').find('a').addClass('page-link');
        }
    });
}

function getOrder(iid, index) {

    $.ajax({
        url: '/order/get' + '/id=' + iid,
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            if (data.status === true) {
                selectedOrder = data.result;
                if (index === 1) { //product
                    createProductModal();
                }
                if (index === 2) { //address
                    createAddressModal();
                }
            } else {
                console.log(data.message);
            }
        },
        error: function (err) {
            console.log(err)
        }
    })
    return {};
}

function deleteOrder(iid) {
    Swal.fire({
        title: 'Are you sure?',
        text: "This order will be permanently deleted!",
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
                url: '/order/delete/id=' + iid,
                type: 'DELETE',
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                success: function (data) {
                    if (data.status === true) {
                        Swal.fire({
                            icon: 'success',
                            title: "Deleted!",
                            text: data.message,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        });
                        selectedOrder = {};
                        $('#order_pagination').twbsPagination('destroy');
                        getAllOrdersByPage(0, $('#order_pagesize').val());
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

function createOrderTable(orders) {
    let html = ``;
    for (let i = 0; i < orders.length; i++) {
        const order = orders[i];
        html += `                <tr>
                                    <td>` + order.no + `</td>
                                    <td>` + order.cno + `</td>
                                    <td>` + order.cname + `</td>
                                    <td>` + order.pno + `</td>
                                    <td>
                                        <button type="button" class="btn btn-sm" onClick="getOrder(` + order.iid + `, ` + 1 + `)">
                                            <i class="far fa-file-alt" style="font-size: 20px;"></i>
                                        </button>
                                    </td>
                                    <td>` + order.date + `</td>
                                    <td>
                                        <button type="button" class="btn btn-sm" onClick="getOrder(` + order.iid + `, ` + 2 + `)">
                                            <i class="far fa-file-alt" style="font-size: 20px;"></i>
                                        </button>
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-sm btn-outline-danger" onclick="deleteOrder(` + order.iid + `)">
                                            <i class="far fa-trash-alt" style="font-size: 20px; width: 13px;"></i>
                                        </button>
                                    </td>
                                </tr>`;
    }
    $('#tBodyOrders').html(html);
}

function createProductModal() {
    let html = ``;
    const product = selectedOrder.product;
    const imageArr = product.fileName;
    for (let i = 0; i < imageArr.length; i++) {
        const image = imageArr[i];
        html += ` <div class="swiper-slide">
                    <img src="/productDetail/get_image/id=${product.id}name=${image}" class="img-fluid" alt="banner" />
                    </div>`;
    }

    const buttonHtml = `<button onclick='window.location.href="/productDetail/${product.id}"' class="btn btn-outline-primary">Details</button>`;
    $('#order_product_image_slider').html(html);
    $('#order_product_button_div').html(buttonHtml);
    $('#order_product_modal_title').text("Product No : " + product.no);
    $('#order_product_title').text(product.name);
    $('#order_product_detail').text(product.details);
    $('#order_product_modal').modal('toggle');
}

function createAddressModal() {
    const address = selectedOrder.customer.addresses[selectedOrder.adressIndex];
    $("#order_address_type").text("Type : " + address.type);
    $("#order_address_detail").text(address.detail);
    $("#order_address_modal_title").text("Order No : " + selectedOrder.no);
    $('#order_address_modal').modal('toggle');
}

$('#order_pagesize').change(function () {
    $('#order_pagination').twbsPagination('destroy');
    getAllOrdersByPage(0, parseInt($(this).val()));
});

$('#order_status').change(function () {
    selectedOrderStatus = $(this).val();
    $("#tBodyOrders > tr").remove();
    getAllOrdersByPage(0, $('#order_pagesize').val());
});

$('#order_search').keyup(function (event) {
    event.preventDefault();
    const key = $(this).val();
    if (key !== "") {
        $("#tBodyOrders > tr").remove();
        $('#order_pagination').twbsPagination('destroy');
        searchOrderByKey(key, 0, $('#order_pagesize').val());
    } else {
        $('#order_pagination').twbsPagination('destroy');
        getAllOrdersByPage(0, $('#order_pagesize').val());
    }
});


let selectedOrder = {};
let selectedOrderStatus = "Active";
$('#order_pagination').twbsPagination('destroy');
getAllOrdersByPage(0, 10);
