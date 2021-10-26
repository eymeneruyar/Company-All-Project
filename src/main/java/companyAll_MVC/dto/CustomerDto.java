package companyAll_MVC.dto;

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
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.*;

@Service
public class CustomerDto {

    final CustomerRepository customerRepository;
    final AddressRepository addressRepository;
    final CustomerTrashRepository customerTrashRepository;
    final ElasticCustomerRepository elasticCustomerRepository;
    final ElasticCustomerMinRepository elasticCustomerMinRepository;
    final ElasticAddressRepository elasticAddressRepository;

    public CustomerDto(CustomerRepository customerRepository, AddressRepository addressRepository, CustomerTrashRepository customerTrashRepository, ElasticCustomerRepository elasticCustomerRepository, ElasticCustomerMinRepository elasticCustomerMinRepository, ElasticAddressRepository elasticAddressRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.customerTrashRepository = customerTrashRepository;
        this.elasticCustomerRepository = elasticCustomerRepository;
        this.elasticCustomerMinRepository = elasticCustomerMinRepository;
        this.elasticAddressRepository = elasticAddressRepository;
    }

    public Map<Check, Object> get(Integer id) {
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

    public Map<Check, Object> add(Customer customer, BindingResult bindingResult) {
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

    public Map<Check, Object> update(Customer customer, BindingResult bindingResult) {
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

    public Map<Check, Object> delete(Integer id) {
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

    public Map<Check, Object> addresses(Integer cid) {
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

    public Map<Check, Object> updateAddress(Address address, BindingResult bindingResult) {
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

    public Map<Check, Object> deleteAddress(Integer cid, Integer aid) {
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

}
