package companyAll_MVC.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import companyAll_MVC.documents.ElasticAddress;
import companyAll_MVC.documents.ElasticCustomer;
import companyAll_MVC.documents.ElasticCustomerMin;
import companyAll_MVC.entities.Address;
import companyAll_MVC.entities.Customer;
import companyAll_MVC.entities.CustomerTrash;
import companyAll_MVC.repositories._elastic.ElasticAddressRepository;
import companyAll_MVC.repositories._elastic.ElasticCustomerMinRepository;
import companyAll_MVC.repositories._elastic.ElasticCustomerRepository;
import companyAll_MVC.repositories._jpa.AddressRepository;
import companyAll_MVC.repositories._jpa.CustomerRepository;
import companyAll_MVC.repositories._jpa.CustomerTrashRepository;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Util;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.DataInput;
import java.util.*;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    final CustomerRepository customerRepository;
    final AddressRepository addressRepository;
    final ElasticCustomerRepository elasticCustomerRepository;
    final ElasticCustomerMinRepository elasticCustomerMinRepository;
    final ElasticAddressRepository elasticAddressRepository;
    final CustomerTrashRepository customerTrashRepository;

    public CustomerController(CustomerRepository customerRepository, AddressRepository addressRepository, ElasticCustomerRepository elasticCustomerRepository, ElasticCustomerMinRepository elasticCustomerMinRepository, ElasticAddressRepository elasticAddressRepository, CustomerTrashRepository customerTrashRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.elasticCustomerRepository = elasticCustomerRepository;
        this.elasticCustomerMinRepository = elasticCustomerMinRepository;
        this.elasticAddressRepository = elasticAddressRepository;
        this.customerTrashRepository = customerTrashRepository;
    }

    @GetMapping("")
    public String customer() {
        return "customer";
    }

    @GetMapping("/get/id={id}")
    @ResponseBody
    public Map<Check, Object> get(@PathVariable Integer id) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            resultMap.put(Check.status, true);
            resultMap.put(Check.result, customer);
        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No customer found with given id!");
            resultMap.put(Check.result, new Object());
        }
        return resultMap;
    }

    @GetMapping("/all/status={status}")
    @ResponseBody
    public Map<Check, Object> all(@PathVariable String status) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        List<ElasticCustomer> customers = elasticCustomerRepository.findAllByStatus(status);
        if (customers.size() != 0) {
            resultMap.put(Check.status, true);
        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No customer found!");
        }
        resultMap.put(Check.result, customers);
        return resultMap;
    }

    @GetMapping("/min-all")
    @ResponseBody
    public Map<Check, Object> all() {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        List<ElasticCustomerMin> customers = elasticCustomerMinRepository.findAllByStatus("Active");
        if (customers.size() != 0) {
            resultMap.put(Check.status, true);
        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No customer found!");
        }
        resultMap.put(Check.result, customers);
        return resultMap;
    }

    @GetMapping("/all/status={status}/page={page}size={size}")
    @ResponseBody
    public Map<Check, Object> all(@PathVariable String status, @PathVariable Integer page, @PathVariable Integer size) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        resultMap.put(Check.status, true);
        Page<ElasticCustomer> customerPage = elasticCustomerRepository.findAllByStatus(status, PageRequest.of(page, size));
        if (customerPage.getTotalElements() != 0) {
            resultMap.put(Check.status, true);
        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No customer found!");
        }
        resultMap.put(Check.totalPage, customerPage.getTotalPages());
        resultMap.put(Check.result, customerPage.getContent());
        return resultMap;
    }

    @GetMapping("/addresses/cid={cid}")
    @ResponseBody
    public Map<Check, Object> addresses(@PathVariable Integer cid) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        resultMap.put(Check.status, true);
        Optional<Customer> customerOpt = customerRepository.findById(cid);
        if (customerOpt.isPresent()) {
            resultMap.put(Check.status, true);
            resultMap.put(Check.result, customerOpt.get().getAddresses());
        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No customer found with given id!");
            resultMap.put(Check.result, new ArrayList<>());
        }
        return resultMap;
    }

    @PostMapping("/address/update")
    @ResponseBody
    public Map<Check, Object> updateAddress(@RequestBody @Valid Address address, BindingResult bindingResult) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        if (bindingResult.hasErrors()) {
            resultMap.put(Check.status, false);
            resultMap.put(Check.errors, Util.errors(bindingResult));
        } else {
            int saved_id = 0;
            try {
                saved_id = addressRepository.save(address).getId();
                Optional<ElasticAddress> elasticAddressOptional = elasticAddressRepository.findByAid(saved_id);
                if (elasticAddressOptional.isPresent()) {
                    ElasticAddress elasticAddress = elasticAddressOptional.get();
                    elasticAddress.setType(address.getType());
                    elasticAddress.setDetail(address.getDetail());
                    elasticAddressRepository.save(elasticAddress);
                    resultMap.put(Check.status, true);
                    resultMap.put(Check.message, "Address successfully updated!");
                    resultMap.put(Check.result, saved_id);
                }
            } catch (Exception e) {
                resultMap.put(Check.status, false);
                resultMap.put(Check.message, "An error occurred in update operation!");
            }
        }
        return resultMap;
    }

    @DeleteMapping("/address/delete/cid={cid}aid={aid}")
    @ResponseBody
    public Map<Check, Object> deleteAddress(@PathVariable Integer cid, @PathVariable Integer aid) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        Optional<Customer> customerOptional = customerRepository.findById(cid);
        //Optional<Address> addressOptional = addressRepository.findById(aid);
        Optional<ElasticAddress> elasticAddressOptional = elasticAddressRepository.findByAid(aid);
        if (elasticAddressOptional.isPresent() && customerOptional.isPresent()) {
            try {
                Customer customer = customerOptional.get();
                List<Address> addressList = customer.getAddresses();
                addressList.removeIf(address -> address.getId().equals(aid));
                customer.setAddresses(addressList);
                customerRepository.save(customer);
                //addressRepository.deleteById(addressOptional.get().getId());
                elasticAddressRepository.deleteById(elasticAddressOptional.get().getId());
                resultMap.put(Check.status, true);
                resultMap.put(Check.message, "Address has been deleted!");
            } catch (Exception e) {
                e.printStackTrace();
                e.printStackTrace();
                resultMap.put(Check.status, false);
                resultMap.put(Check.message, "An error occurred in delete operation!");
            }
        }else{
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No address found with given id!");
        }

        return resultMap;
    }

    @GetMapping("/search/key={key}/status={status}/page={page}size={size}")
    @ResponseBody
    public Map<Check, Object> search(@PathVariable String key, @PathVariable String status, @PathVariable Integer page, @PathVariable Integer size) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        resultMap.put(Check.status, true);
        Page<ElasticCustomer> customerPage = elasticCustomerRepository.searchByKeyAndStatus(key, status, PageRequest.of(page, size));
        if (customerPage.getTotalElements() != 0) {
            resultMap.put(Check.status, true);
        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No customer found!");
        }
        resultMap.put(Check.totalPage, customerPage.getTotalPages());
        resultMap.put(Check.result, customerPage.getContent());
        return resultMap;
    }

    @GetMapping("/change-status/id={id}status={status}")
    @ResponseBody
    public Map<Check, Object> changeStatus(@PathVariable Integer id, @PathVariable String status) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        Optional<Customer> customerOpt = customerRepository.findById(id);
        Optional<ElasticCustomer> elasticCustomerOptional = elasticCustomerRepository.findByCid(id);
        if (customerOpt.isPresent() && elasticCustomerOptional.isPresent()) {
            Customer customer = customerOpt.get();
            ElasticCustomer elasticCustomer = elasticCustomerOptional.get();
            customer.setStatus(status);
            elasticCustomer.setStatus(status);
            customerRepository.save(customer);
            elasticCustomerRepository.save(elasticCustomer);
            resultMap.put(Check.status, true);
            resultMap.put(Check.result, 1);
        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No customer found with given id!");
        }
        return resultMap;
    }

    @DeleteMapping("/delete/id={id}")
    @ResponseBody
    public Map<Check, Object> delete(@PathVariable Integer id) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        Optional<Customer> customerOptional = customerRepository.findById(id);
        Optional<ElasticCustomer> elasticCustomerOptional = elasticCustomerRepository.findByCid(id);
        Optional<ElasticCustomerMin> elasticCustomerMinOptional = elasticCustomerMinRepository.findByCid(id);
        if (elasticCustomerOptional.isPresent() && customerOptional.isPresent() && elasticCustomerMinOptional.isPresent()) {
            try {
                Customer customer = customerOptional.get();
                customerRepository.deleteById(customer.getId());
                CustomerTrash customerTrash = new CustomerTrash();
                customerTrash.setName(customer.getName());
                customerTrash.setSurname(customer.getSurname());
                customerTrash.setPhone(customer.getPhone1());
                customerTrash.setMail(customer.getMail());
                customerTrashRepository.save(customerTrash);
                elasticCustomerRepository.deleteById(elasticCustomerOptional.get().getId());
                elasticCustomerMinRepository.deleteById(elasticCustomerMinOptional.get().getId());
                for (Address address : customer.getAddresses()) {
                    Optional<ElasticAddress> elasticAddressOptional = elasticAddressRepository.findByAid(address.getId());
                    elasticAddressOptional.ifPresent(elasticAddress -> elasticAddressRepository.deleteById(elasticAddress.getId()));
                }
                resultMap.put(Check.status, true);
                resultMap.put(Check.message, "Customer has been deleted!");
            } catch (Exception e) {
                resultMap.put(Check.status, false);
                resultMap.put(Check.message, "An error occurred in delete operation!");
            }
        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No customer found with given id!");
        }


        return resultMap;
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<Check, Object> add(@RequestBody @Valid Customer customer, BindingResult bindingResult) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        if (bindingResult.hasErrors()) {
            resultMap.put(Check.status, false);
            resultMap.put(Check.errors, Util.errors(bindingResult));
        } else {
            int savedAddressId = 0;
            String savedElasticAddressId = "";
            try {
                //address add
                Address address = customer.getAddresses().get(0);
                savedAddressId = addressRepository.save(address).getId();
                ElasticAddress elasticAddress = new ElasticAddress();
                elasticAddress.setAid(address.getId());
                elasticAddress.setType(address.getType());
                elasticAddress.setDetail(address.getDetail());
                savedElasticAddressId = elasticAddressRepository.save(elasticAddress).getId();

                //customer add
                int saved_id = customerRepository.save(customer).getId();
                ElasticCustomer elasticCustomer = new ElasticCustomer();
                elasticCustomer.setCid(customer.getId());
                elasticCustomer.setNo(customer.getNo());
                elasticCustomer.setName(customer.getName());
                elasticCustomer.setSurname(customer.getSurname());
                elasticCustomer.setPhone1(customer.getPhone1());
                elasticCustomer.setMail(customer.getMail());
                elasticCustomer.setTaxno(customer.getTaxno());
                elasticCustomer.setCountry(customer.getCountry());
                elasticCustomer.setCity(customer.getCity());
                elasticCustomer.setStatus(customer.getStatus());
                elasticCustomerRepository.save(elasticCustomer);
                ElasticCustomerMin elasticCustomerMin = new ElasticCustomerMin();
                elasticCustomerMin.setCid(customer.getId());
                elasticCustomerMin.setNo(customer.getNo());
                elasticCustomerMin.setName(customer.getName());
                elasticCustomerMin.setSurname(customer.getSurname());
                elasticCustomerMin.setStatus(customer.getStatus());
                elasticCustomerMinRepository.save(elasticCustomerMin);
                resultMap.put(Check.status, true);
                resultMap.put(Check.message, "Customer successfully saved!");
                resultMap.put(Check.result, saved_id);

            } catch (DataIntegrityViolationException e) {
                resultMap.put(Check.status, false);
                if (savedAddressId != 0) {
                    addressRepository.deleteById(savedAddressId);
                }
                if (!savedElasticAddressId.equals("")) {
                    elasticAddressRepository.deleteById(savedElasticAddressId);
                }
                if (customerRepository.findByMail(customer.getMail()).isPresent()) {
                    resultMap.put(Check.message, "This email has already been used");
                }
                if (customerRepository.findByPhone1(customer.getPhone1()).isPresent()) {
                    resultMap.put(Check.message, "This phone1 has already been used");
                }
                if (customerRepository.findByTaxno(customer.getTaxno()).isPresent()) {
                    resultMap.put(Check.message, "This tax no has already been used");
                }
            }catch (Exception e){
                resultMap.put(Check.status, false);
                resultMap.put(Check.message, "An error occurred in save operation!");
            }
        }
        return resultMap;
    }

    @PostMapping("/update")
    @ResponseBody
    public Map<Check, Object> update(@RequestBody @Valid Customer customer, BindingResult bindingResult) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        if (bindingResult.hasErrors()) {
            resultMap.put(Check.status, false);
            resultMap.put(Check.errors, Util.errors(bindingResult));
        } else {
            int savedCustomerId = 0;
            int savedAddressId = 0;
            String savedElasticAddressId = "";
            try {
                //address add
                List<Address> addresses = customer.getAddresses();
                Address address = addresses.get(addresses.size()-1);
                if(address.getId() == null || address.getId() == 0){
                    savedAddressId = addressRepository.save(address).getId();
                    ElasticAddress elasticAddress = new ElasticAddress();
                    elasticAddress.setAid(address.getId());
                    elasticAddress.setType(address.getType());
                    elasticAddress.setDetail(address.getDetail());
                    savedElasticAddressId = elasticAddressRepository.save(elasticAddress).getId();
                    savedCustomerId = customerRepository.saveAndFlush(customer).getId();
                    resultMap.put(Check.message, "Address successfully added!");
                }else{
                    //customer update
                    savedCustomerId = customerRepository.saveAndFlush(customer).getId();
                    Optional<ElasticCustomer> elasticCustomerOptional = elasticCustomerRepository.findByCid(savedCustomerId);
                    Optional<ElasticCustomerMin> elasticCustomerMinOptional = elasticCustomerMinRepository.findByCid(savedCustomerId);
                    if (elasticCustomerOptional.isPresent() && elasticCustomerMinOptional.isPresent()) {
                        ElasticCustomer elasticCustomer = elasticCustomerOptional.get();
                        elasticCustomer.setName(customer.getName());
                        elasticCustomer.setSurname(customer.getSurname());
                        elasticCustomer.setPhone1(customer.getPhone1());
                        elasticCustomer.setMail(customer.getMail());
                        elasticCustomer.setTaxno(customer.getTaxno());
                        elasticCustomer.setCountry(customer.getCountry());
                        elasticCustomer.setCity(customer.getCity());
                        elasticCustomer.setStatus(customer.getStatus());
                        elasticCustomerRepository.save(elasticCustomer);
                        ElasticCustomerMin elasticCustomerMin = elasticCustomerMinOptional.get();
                        elasticCustomerMin.setName(customer.getName());
                        elasticCustomerMin.setSurname(customer.getSurname());
                        elasticCustomerMin.setStatus(customer.getStatus());
                        elasticCustomerMinRepository.save(elasticCustomerMin);
                        resultMap.put(Check.message, "Customer successfully updated!");
                    }
                }
                resultMap.put(Check.status, true);
                resultMap.put(Check.result, savedCustomerId);
            } catch (DataIntegrityViolationException e) {
                resultMap.put(Check.status, false);
                if (savedAddressId != 0) {
                    addressRepository.deleteById(savedAddressId);
                }
                if (!savedElasticAddressId.equals("")) {
                    elasticAddressRepository.deleteById(savedElasticAddressId);
                }
                if (customerRepository.findByMail(customer.getMail()).isPresent()) {
                    resultMap.put(Check.message, "This email has already been used");
                }
                if (customerRepository.findByPhone1(customer.getPhone1()).isPresent()) {
                    resultMap.put(Check.message, "This phone1 has already been used");
                }
                if (customerRepository.findByTaxno(customer.getTaxno()).isPresent()) {
                    resultMap.put(Check.message, "This tax no has already been used");
                }
            }
        }
        return resultMap;
    }

}
