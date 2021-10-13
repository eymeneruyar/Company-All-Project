function getAllCustomers(status) {

    $.ajax({
        url: '/customer/all/status=' + status,
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            createCustomerTable(data);
        },
        error: function (err) {
            console.log(err)
        }
    })

}

function searchCustomerByKey(key, page, size) {
    console.log(selectedCustomerStatus)

    $.ajax({
        url: '/customer/search' + '/key=' + key + '/status=' + selectedCustomerStatus + '/page=' + page + 'size=' + size,
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            console.log(data.result)
            createCustomerTable(data.result);
            dynamicPagination(data.totalPage, size);
        },
        error: function (err) {
            console.log(err)
        }
    })
}

function getAllCustomersByPage(page, size) {
    $.ajax({
        url: '/customer/all' + '/status=' + selectedCustomerStatus + '/page=' + page + 'size=' + size,
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            console.log(data.result)
            createCustomerTable(data.result);
            dynamicPagination(data.totalPage, size);
        },
        error: function (err) {
            console.log(err)
        }
    })

}

function createCustomerTable(customers) {
    let html = ``;
    for (let i = 0; i < customers.length; i++) {
        const customer = customers[i];

        html += `<tr role="row" class="odd">
            <td>` + customer.no + `</td>
            <td>` + customer.name + `</td>
            <td>` + customer.surname + `</td>
            <td>` + customer.mail + `</td>
            <td>` + customer.phone1 + `</td>
            <td>` + customer.taxno + `</td>
            <td>` + customer.country + `</td>
            <td>` + customer.city + `</td>
            <td>
                <div class="dropdown">
                <button type="button" class="btn btn-sm dropdown-toggle hide-arrow" data-toggle="dropdown">
                    <i class="fas fa-ellipsis-v"></i>
                </button>
                    <div class="dropdown-menu">
                        <a class="dropdown-item" href="javascript:deneme(`+5+`);">
                          <i class="mr-50 fas fa-pen"></i>
                          <span>Edit</span>
                        </a>
                        <a class="dropdown-item" href="javascript:void(0);">
                          <i class="mr-50 fas fa-ban"></i>
                          <span>Ban</span>
                        </a>
                        <a class="dropdown-item" href="javascript:void(0);">
                          <i class="mr-50 far fa-trash-alt"></i>
                          <span>Delete</span>
                        </a>
                    </div>
                </div>   
            </td>
                              
          </tr>`;
        $('#tbodyCustomer').html(html);
    }
}

function deneme(a) {
    console.log(a)
}

function dynamicPagination(totalPage, size) {
        $('#customer_pagination').twbsPagination({
            totalPages: totalPage,
            visiblePages: 5,
            prev: 'Prev',
            first: 'First',
            last: 'Last',
            startPage: 1,
            onPageClick: function (event, page) {
                console.log("Page click");
                getAllCustomersByPage(page-1, size);
                //$('#firstLast1-content').text('You are on Page ' + page);
                $('.pagination').find('li').addClass('page-item');
                $('.pagination').find('a').addClass('page-link');
            }
        });

}

function codeGenerator() {
    const date = new Date();
    const time = date.getTime();
    return time.toString().substring(3);
}

$('#customer_form').submit( ( event ) => {
    event.preventDefault();
    const firstname = $("#firstname").val();
    const lastname = $("#lastname").val();
    const phone1 = $("#phone1").val();
    const phone2 = $("#phone2").val();
    const mail = $("#mail").val();
    const tax_no = $("#tax_no").val();
    const country = $("#country").val();
    const city = $("#city").val();
    const address_type = $("#address_type_customer_form").val();
    const address_detail = $("#address_detail").val();

    let addressList = []
    const addressObj = {
        type: address_type,
        detail: address_detail
    }
    addressList.push(addressObj);

    const customerObj = {
        no: codeGenerator(),
        name: firstname,
        surname: lastname,
        phone1: phone1,
        phone2: phone2,
        mail: mail,
        taxno: tax_no,
        country: country,
        city: city,
        addresses: addressList,
        status: "Active"
    }

    $.ajax({
        url: '/customer/add',
        type: 'POST',
        data: JSON.stringify(customerObj),
        dataType: 'JSON',
        contentType : 'application/json; charset=utf-8',
        success: function (data) {
            if(data.status === true){
                Swal.fire({
                    title: 'Success!',
                    text: data.message,
                    icon: "success",
                    customClass: {
                        confirmButton: 'btn btn-primary'
                    },
                    buttonsStyling: false
                });
                getAllCustomers();
                $('#customer_form').trigger("reset");
            }else{
                if(!jQuery.isEmptyObject(data.errors)){
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
                }else{
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

$('#customer_pagesize').change(function(){
    $('#customer_pagination').twbsPagination('destroy');
    getAllCustomersByPage(0, parseInt($(this).val()));
});

$('#customer_status').change(function(){
    selectedCustomerStatus = $(this).val();
    $("#tbodyCustomer > tr").remove();
    getAllCustomersByPage(0, $('#customer_pagesize').val());
});

$('#customer_search').keyup( function (event) {
    event.preventDefault();
    const key = $(this).val();
    console.log(key)
    if(key !== ""){
        $("#tbodyCustomer > tr").remove();
        searchCustomerByKey(key, 0, $('#customer_pagesize').val());
    }else{
        getAllCustomersByPage(0, $('#customer_pagesize').val());
    }

});
let selectedCustomerStatus = "Active";
getAllCustomersByPage(0, 10);

//getAllCustomers("Active");