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

function getMinCustomers() {

    $.ajax({
        url: '/customer/min-all',
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            createCustomerSelect(data.result);
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
            createCustomerTable(data.result);
            dynamicPagination(data.totalPage, size);
        },
        error: function (err) {
            console.log(err)
        }
    })

}

function getCustomer(cid, index) {

    $.ajax({
        url: '/customer/get' + '/id=' + cid,
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            selectedCustomer = data.result;
            if(index === 0){ //detail
                createCustomerDetailsModal();
            }
            if(index === 1){ //update
                createCustomerEditModal();
            }
            if(index === 3){
                addressList = selectedCustomer.addresses;
                createAddressTable(addressList);
            }
        },
        error: function (err) {
            console.log(err)
        }
    })
    return {};

}

function deleteCustomer(cid) {
    Swal.fire({
        title: 'Are you sure?',
        text: "This customer will be sent to trash and all of its information will be deleted!",
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
                url: '/customer/delete/id=' + cid,
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
                        addressesTabCount = 0;
                        getAllCustomersByPage(0, $('#customer_pagesize').val());
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

function changeCustomerStatus(cid, statusInt) {

    let url;
    let text;
    let confirmButtonText;
    let successTitle;
    if(statusInt === 1){ //Ban
        url = '/customer/change-status/id=' + cid + 'status=Passive';
        text = "This customer will be banned and won't be able to do any operations!";
        confirmButtonText = "Ban";
        successTitle = "Banned!"
    }else if(statusInt === 0){ //Unban
        url = '/customer/change-status/id=' + cid + 'status=Active';
        text = "This customer will be unbanned and will be able to do any operations!";
        confirmButtonText = "Unban";
        successTitle = "Unbanned!"
    }else{
        return;
    }

    Swal.fire({
        title: 'Are you sure?',
        text: text,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: confirmButtonText,
        customClass: {
            confirmButton: 'btn btn-primary',
            cancelButton: 'btn btn-outline-danger ml-1'
        },
        buttonsStyling: false
    }).then(function (result) {
        console.log(url)
        if (result.value) {
            $.ajax({
                url: url,
                type: 'GET',
                dataType: 'json',
                success: function (data) {
                    if( data.status === true){
                        Swal.fire({
                            icon: 'success',
                            title: successTitle,
                            text: data.message,
                            customClass: {
                                confirmButton: 'btn btn-success'
                            }
                        });
                        addressesTabCount = 0;
                        getAllCustomersByPage(0, $('#customer_pagesize').val());
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
                        text: "An error occurred during the operation",
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

function saveCustomer(customerObj, index) {
    let url;
    if(customerObj["id"] === undefined){
        url = '/customer/add';
    }else{
        url = '/customer/update';
    }
    $.ajax({
        url: url,
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
                addressesTabCount = 0;
                if(index === 0){
                    getAllCustomersByPage(0, $('#customer_pagesize').val());
                    $('#customer_form').trigger("reset");
                    setTimeout(function(){
                        $("#customer_form_modal").modal('hide');
                    }, 0);
                    selectedCustomer = {};
                    return;
                }
                if(index === 1){ // address add
                    getAllAddressesByCid(selectedCustomer.id)
                }

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
}

function getAllAddressesByCid(cid) {

    $.ajax({
        url: '/customer/addresses/cid=' + cid,
        type: 'GET',
        dataType: 'Json',
        success: function (data) {
            addressList = data.result;
            createAddressTable(addressList);
        },
        error: function (err) {
            console.log(err)
        }
    })
}

function updateAddress(addressObj) {
    $.ajax({
        url: '/customer/address/update',
        type: 'POST',
        data: JSON.stringify(addressObj),
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
                getAllAddressesByCid(selectedCustomer.id)
                $('#address_form').trigger("reset");
                setTimeout(function(){
                    $("#address_form").modal('hide');
                }, 0);
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
}

function deleteAddress(aid, cid) {
    Swal.fire({
        title: 'Are you sure?',
        text: "This address will be permanently deleted!",
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
                url: '/customer/address/delete/cid=' + cid + 'aid=' + aid,
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
                        getAllAddressesByCid($('#customer_select').val());
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

 function getAddress(aid) {
    let address;
    let addressTemp;
    for(let i = 0; i < addressList.length; i++){
        addressTemp = addressList[i];
        if(addressTemp.id === aid ){
            address = addressTemp;
        }
    }
    selectedAddressId = aid;
    createAddressEditModal(address);
}

function createAddressEditModal(address) {
    $("#address_type_address_form select").val(address.type).change();
    $("#address_detail_address_form").val(address.detail);
    $('#address_form_modal').modal('toggle');
}

function createCustomerTable(customers) {
    let html = ``;
    let statusHtml = ``;
    let statusDropdownHtml = ``;
    let statusInt;
    for (let i = 0; i < customers.length; i++) {
        const customer = customers[i];
        let status = customer.status;
        if(status === "Active"){
            statusInt = 1;
            statusHtml = `<i class="fas fa-check-circle"></i>`;
            statusDropdownHtml = `<i class="mr-50 fas fa-ban"></i>
                                  <span>Ban</span>`;
        }
        if(status === "Passive"){
            statusInt = 0;
            statusHtml = `<i class="fas fa-ban"></i>`;
            statusDropdownHtml = `<i class="fas fa-check-circle"></i>
                                  <span>Unban</span>`;
        }
        html += `<tr role="row" class="odd">
            <td>` + customer.no + `</td>
            <td>` + customer.name + `</td>
            <td>` + customer.surname + `</td>
            <td>` + customer.mail + `</td>
            <td>` + customer.phone1 + `</td>
            <td>` + customer.taxno + `</td>
            <td>` + customer.country + `</td>
            <td>` + customer.city + `</td>
            <td>` + statusHtml + `</td>
            <td>
                <div class="dropdown">
                <button type="button" class="btn btn-sm dropdown-toggle hide-arrow" data-toggle="dropdown">
                    <i class="fas fa-ellipsis-v"></i>
                </button>
                    <div class="dropdown-menu">
                         <a class="dropdown-item" href="javascript:getCustomer(`+customer.cid+`, `+0+`);">
                          <i class="mr-50 fas fa-info-circle"></i>
                          <span>Info</span>
                        </a>
                        <a class="dropdown-item" href="javascript:getCustomer(`+customer.cid+`, `+1+`);">
                          <i class="mr-50 fas fa-pen"></i>
                          <span>Edit</span>
                        </a>
                        <a class="dropdown-item" href="javascript:changeCustomerStatus(`+customer.cid+`, `+statusInt+`);">
                          `+statusDropdownHtml+`
                        </a>
                        <a class="dropdown-item" href="javascript:deleteCustomer(`+customer.cid+`);">
                          <i class="mr-50 far fa-trash-alt"></i>
                          <span>Delete</span>
                        </a>
                    </div>
                </div>   
            </td>
                              
          </tr>`;
    }
    $('#tbodyCustomer').html(html);
}

function createAddressTable(addresses) {
    let html = ``;
    let typeInt;
    for (let i = 0; i < addresses.length; i++) {
        const address = addresses[i];
        if (address.type === "Home"){
            typeInt = 0;
        }
        if(address.type === "Work"){
            typeInt = 1;
        }

        html += `<tr role="row" class="odd">
            <td>` + address.detail + `</td>            
            <td>` + address.type + `</td>
            <td>
                <div class="dropdown">
                <button type="button" class="btn btn-sm dropdown-toggle hide-arrow" data-toggle="dropdown">
                    <i class="fas fa-ellipsis-v"></i>
                </button>
                    <div class="dropdown-menu">                         
                        <a class="dropdown-item" href="javascript:getAddress(`+address.id+`);">
                          <i class="mr-50 fas fa-pen"></i>
                          <span>Edit</span>
                        </a>                        
                        <a class="dropdown-item" href="javascript:deleteAddress(`+address.id+`, `+selectedCustomer.id+`);">
                          <i class="mr-50 far fa-trash-alt"></i>
                          <span>Delete</span>
                        </a>
                    </div>
                </div>   
            </td>                              
          </tr>`;
    }
    $('#tBodyAddresses').html(html);
}

function createCustomerSelect(customers) {
    let html = ``;
    for(let i = 0; i < customers.length; i++){
        const customer = customers[i];
        html += `<option value=`+customer.cid+`> <b>`+customer.no+`</b> `+customer.name+ " " +customer.surname+`</option>`;
    }
    $('#customer_select').append(html);
}

function createCustomerDetailsModal() {
    $("#detail_firstname").text(selectedCustomer.name);
    $("#detail_lastname").text( selectedCustomer.surname);
    $("#detail_phone1").text( selectedCustomer.phone1);
    $("#detail_phone2").text( selectedCustomer.phone2);
    $("#detail_mail").text( selectedCustomer.mail);
    $("#detail_tax_no").text( selectedCustomer.taxno);
    $("#detail_country").text( selectedCustomer.country);
    $("#detail_city").text( selectedCustomer.city);
    $("#customer_detail_modal_title").text( "Customer No : " + selectedCustomer.no);
    $('#customer_detail_modal').modal('toggle');
}

function createCustomerEditModal() {

    $("#firstname").val(selectedCustomer.name);
    $("#lastname").val( selectedCustomer.surname);
    $("#phone1").val( selectedCustomer.phone1);
    $("#phone2").val( selectedCustomer.phone2);
    $("#mail").val( selectedCustomer.mail);
    $("#tax_no").val( selectedCustomer.taxno);
    $("#country select").val( selectedCustomer.country).change();
    $("#city select").val( selectedCustomer.city).change();
    $("#customer_form_col").remove();
    $("#customer_form_modal_title").text( "Edit Customer - No : " + selectedCustomer.no);
    $('#customer_form_modal').modal('toggle');
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
                if($('#customer_search').val() === ""){
                    getAllCustomersByPage(page-1, size);
                }else{
                    searchCustomerByKey($('#customer_search').val(), page-1, $('#customer_pagesize').val());
                }

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

$("a[href='#addresses']").on('shown.bs.tab', function(e) {
    if(addressesTabCount === 0){
        console.log('shown - after the tab has been shown');
        getMinCustomers();
        addressesTabCount++;
    }
});

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

    const customerObj = {
        name: firstname,
        surname: lastname,
        phone1: phone1,
        phone2: phone2,
        mail: mail,
        taxno: tax_no,
        country: country,
        city: city,
        status: "Active"
    }
    if(jQuery.isEmptyObject(selectedCustomer)){
        let addressList = []
        const addressObj = {
            type: address_type,
            detail: address_detail
        }
        addressList.push(addressObj);
        customerObj["no"] = codeGenerator();
        customerObj["addresses"] = addressList;
    }else{
        customerObj["id"] = selectedCustomer.id;
        customerObj["no"] = selectedCustomer.no;
        customerObj["addresses"] = selectedCustomer.addresses;
    }
    saveCustomer(customerObj);
})

$('#address_form').submit( ( event ) => {
    event.preventDefault();
    const type = $("#address_type_address_form").val();
    const detail = $("#address_detail_address_form").val();
    const addressObj = {
        type: type,
        detail: detail
    }
    $("#tBodyAddresses > tr").remove();
    if(selectedAddressId !== 0){
        addressObj["id"] = selectedAddressId;
        updateAddress(addressObj);
    }else{
        addressList.push(addressObj);
        selectedCustomer["addresses"] = addressList;
        saveCustomer(selectedCustomer);
    }
});

$('#customer_pagesize').change(function(){
    $('#customer_pagination').twbsPagination('destroy');
    getAllCustomersByPage(0, parseInt($(this).val()));
});

$('#customer_status').change(function(){
    selectedCustomerStatus = $(this).val();
    $("#tbodyCustomer > tr").remove();
    getAllCustomersByPage(0, $('#customer_pagesize').val());
});

$('#customer_select').change(function(){
    $("#tBodyAddresses > tr").remove();
    getCustomer($(this).val(), 3);
});

$('#address_type').change(function(){
    $("#tBodyAddresses > tr").remove();
    if($(this).val() === "All"){
        createAddressTable(addressList);
        return;
    }
    let selectedTypeAddressList = [];
    for(let i = 0; i < addressList.length; i++){
        const address = addressList[i];
        if(address.type === $(this).val()){
            selectedTypeAddressList.push(address);
        }
    }
    createAddressTable(selectedTypeAddressList);
});

$('#customer_search').keyup( function (event) {
    event.preventDefault();
    const key = $(this).val();
    if(key !== ""){
        $("#tbodyCustomer > tr").remove();
        $('#customer_pagination').twbsPagination('destroy');
        searchCustomerByKey(key, 0, $('#customer_pagesize').val());
    }else{
        $('#customer_pagination').twbsPagination('destroy');
        getAllCustomersByPage(0, $('#customer_pagesize').val());
    }
});

$('#address_search').keyup( function (event) {
    let value = $(this).val().toLowerCase();
    $("#address_table tr").filter(function() {
        $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
});

$( "#customer_form_modal_button" ).click(function() {
    $('#customer_form').trigger("reset");
    $("#customer_form_modal_title").text( "New Customer");
    $('#customer_form_modal').modal('toggle');
    let html= `
                                                    <div class="form-group mt-1">
                                                        <label>
                                                            Address Type
                                                            <select class="custom-select form-control-lg"
                                                                id="address_type_customer_form">
                                                                <option value="Home">Home</option>
                                                                <option value="Work">Work</option>
                                                            </select>
                                                        </label>
                                                    </div>
                                                </div>
                                                
                                                    <div class="form-group">
                                                        <label for="address_detail">Address (You can add more addresses
                                                            later.)</label>
                                                        <textarea type="text" id="address_detail" class="form-control"
                                                            name="address_detail" required></textarea>
                                                        <div class="valid-feedback">Looks good!</div>
                                                        <div class="invalid-feedback">Please enter you full address
                                                        </div>
                                                    </div>`;


    $("#customer_form_col").append(html);
});
let selectedCustomer = {};
let selectedCustomerStatus = "Active";
let addressesTabCount = 0;
let addressList = [];
let selectedAddressId = 0;
getAllCustomersByPage(0, 10);

//getAllCustomers("Active");