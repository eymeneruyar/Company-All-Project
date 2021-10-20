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
            if($('#order_search').val() === ""){
                getAllOrdersByPage(page-1, size);
            }else{
                searchOrderByKey($('#order_search').val(), page-1, $('#order_pagesize').val());
            }

            //$('#firstLast1-content').text('You are on Page ' + page);
            $('.pagination').find('li').addClass('page-item');
            $('.pagination').find('a').addClass('page-link');
        }
    });
}

function getOrder(aid, index) {

    if(!jQuery.isEmptyObject(selectedOrder)) {
        if(index === 1){
            createProductModal();
        }
        if(index === 2){
            createAddressModal();
        }
        return;
    }

        $.ajax({
        url: '/announcements/get' + '/id=' + aid,
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            if(data.status === true){
                selectedOrder = data.result;
                if(index === 1){ //product
                    createProductModal();
                }
                if(index === 2){ //address
                    createAddressModal();
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
                    if( data.status === true){
                        Swal.fire({
                            icon: 'success',
                            title: "Deleted!",
                            text: data.message,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        });
                        selectedOrder = {};
                        getAllOrdersByPage(0, $('#order_pagesize').val());
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

function createOrderTable(orders) {
    let html = ``;
    for(let i = 0; i < orders.length; i++){
        const order = orders[i];
        html += `                <tr>
                                    <td>`+order.no+`</td>
                                    <td>`+order.cno+`</td>
                                    <td>`+order.cname+`</td>
                                    <td>`+order.pno+`</td>
                                    <td>
                                        <button type="button" class="btn btn-sm" onClick="getOrder(` + order.iid + `, ` + 1 + `)">
                                            <i class="far fa-file-alt" style="font-size: 20px;"></i>
                                        </button>
                                    </td>
                                    <td>`+order.date+`</td>
                                    <td>
                                        <button type="button" class="btn btn-sm" onClick="getOrder(` + order.iid + `, ` + 2 + `)">
                                            <i class="far fa-file-alt" style="font-size: 20px;"></i>
                                        </button>
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-sm btn-outline-danger" onclick="deleteOrder(`+order.iid+`)">
                                            <i class="far fa-trash-alt" style="font-size: 20px; width: 13px;"></i>
                                        </button>
                                    </td>
                                </tr>`;
    }
    $('#tBodyOrders').html(html);
}

function createProductModal(bytes) {
    $("#order_product_modal_image").attr("src", "data:image/jpg;base64," + bytes);
    $("#order_product_modal_title").text( "Order No : " + selectedOrder.no);
    $('#order_product_modal').modal('toggle');
}

function createAddressModal() {
    $("#order_address_modal").text(selectedOrder.customer.addresses[selectedOrder.adressIndex]);
    $("#order_address_modal_title").text( "Order No : " + selectedOrder.no);
    $('#announcement_detail_modal').modal('toggle');
}

$('#order_pagesize').change(function () {
    $('#order_pagination').twbsPagination('destroy');
    getAllOrdersByPage(0, parseInt($(this).val()));
});

$('#order_status').change(function(){
    selectedAnnouncementStatus = $(this).val();
    $("#tBodyAnnouncements > tr").remove();
    getAllOrdersByPage(0, $('#order_pagesize').val());
});

$('#order_search').keyup( function (event) {
    event.preventDefault();
    const key = $(this).val();
    if(key !== ""){
        $("#tBodyOrders > tr").remove();
        $('#order_pagination').twbsPagination('destroy');
        searchOrderByKey(key, 0, $('#order_pagesize').val());
    }else{
        $('#order_pagination').twbsPagination('destroy');
        getAllOrdersByPage(0, $('#order_pagesize').val());
    }
});


let selectedOrder = {};
let selectedOrderStatus = "Active";
getAllOrdersByPage(0, 10);
