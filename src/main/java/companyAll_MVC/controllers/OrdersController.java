package companyAll_MVC.controllers;

import companyAll_MVC.documents.ElasticIndent;
import companyAll_MVC.entities.Customer;
import companyAll_MVC.entities.Indent;
import companyAll_MVC.entities.Product;
import companyAll_MVC.repositories._elastic.ElasticIndentRepository;
import companyAll_MVC.repositories._jpa.CustomerRepository;
import companyAll_MVC.repositories._jpa.IndentRepository;
import companyAll_MVC.repositories._jpa.ProductRepository;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Util;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/order")
public class OrdersController {

    final IndentRepository indentRepository;
    final ElasticIndentRepository elasticIndentRepository;
    final ProductRepository productRepository;
    final CustomerRepository customerRepository;

    public OrdersController(IndentRepository indentRepository, ElasticIndentRepository elasticIndentRepository, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.indentRepository = indentRepository;
        this.elasticIndentRepository = elasticIndentRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping("")
    public String orders(){
        return "orders";
    }

    @GetMapping("/get/id={id}")
    @ResponseBody
    public Map<Check, Object> get(@PathVariable Integer id) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        Optional<Indent> indent = indentRepository.findById(id);
        if (indent.isPresent()) {
            resultMap.put(Check.status, true);
            resultMap.put(Check.result, indent);
        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No order found with given id!");
            resultMap.put(Check.result, new Object());
        }
        return resultMap;
    }

    @GetMapping("/all/status={status}")
    @ResponseBody
    public Map<Check, Object> all(@PathVariable String status) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        List<ElasticIndent> elasticIndents = elasticIndentRepository.findAllByStatus(status);
        if (elasticIndents.size() != 0) {
            resultMap.put(Check.status, true);
        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No order found!");
        }
        resultMap.put(Check.result, elasticIndents);
        return resultMap;
    }

    @GetMapping("/all/status={status}/page={page}size={size}")
    @ResponseBody
    public Map<Check, Object> all(@PathVariable String status, @PathVariable Integer page, @PathVariable Integer size) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        resultMap.put(Check.status, true);
        Page<ElasticIndent> elasticIndentPage = elasticIndentRepository.findAllByStatus(status, PageRequest.of(page, size));
        if (elasticIndentPage.getTotalElements() != 0) {
            resultMap.put(Check.status, true);
        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No order found!");
        }
        resultMap.put(Check.totalPage, elasticIndentPage.getTotalPages());
        resultMap.put(Check.result, elasticIndentPage.getContent());
        return resultMap;
    }

    @GetMapping("/search/key={key}/status={status}/page={page}size={size}")
    @ResponseBody
    public Map<Check, Object> search(@PathVariable String key, @PathVariable String status, @PathVariable Integer page, @PathVariable Integer size) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        resultMap.put(Check.status, true);
        Page<ElasticIndent> elasticIndentPage = elasticIndentRepository.searchByKeyAndStatus(key, status, PageRequest.of(page, size));
        if (elasticIndentPage.getTotalElements() != 0) {
            resultMap.put(Check.status, true);
        } else {
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No order found!");
        }
        resultMap.put(Check.totalPage, elasticIndentPage.getTotalPages());
        resultMap.put(Check.result, elasticIndentPage.getContent());
        return resultMap;
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<Check, Object> add(@RequestBody @Valid Indent indent, BindingResult bindingResult) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        if (bindingResult.hasErrors()) {
            resultMap.put(Check.status, false);
            resultMap.put(Check.errors, Util.errors(bindingResult));
        } else {
            try{
                Optional<Customer> customerOptional = customerRepository.findById(indent.getCustomer().getId());
                Optional<Product> productOptional = productRepository.findById(indent.getProduct().getId());
                if(customerOptional.isPresent() && productOptional.isPresent()){
                    Customer customer = customerOptional.get();
                    Product product = productOptional.get();
                    indent.setCustomer(customer);
                    indent.setProduct(product);
                    indent.setDate(Util.getDateFormatter());
                    int savedIndentId = indentRepository.save(indent).getId();
                    ElasticIndent elasticIndent = new ElasticIndent();
                    elasticIndent.setIid(indent.getId());
                    elasticIndent.setNo(indent.getNo());
                    elasticIndent.setDate(indent.getDate());
                    elasticIndent.setCid(customer.getId());
                    elasticIndent.setPid(product.getId());
                    elasticIndent.setCno(customer.getNo());
                    elasticIndent.setPno(product.getNo());
                    elasticIndent.setCname(customer.getName() + " " + customer.getSurname());
                    elasticIndent.setStatus(indent.getStatus());
                    elasticIndentRepository.save(elasticIndent);
                    resultMap.put(Check.status, true);
                    resultMap.put(Check.message, "Order successfully saved!");
                    resultMap.put(Check.result, savedIndentId);
                }else{
                    resultMap.put(Check.status, false);
                    resultMap.put(Check.message, "Customer or product couldn't found");
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
    public Map<Check, Object> update(@RequestBody @Valid Indent indent, BindingResult bindingResult) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        if (bindingResult.hasErrors()) {
            resultMap.put(Check.status, false);
            resultMap.put(Check.errors, Util.errors(bindingResult));
        } else {
            try{
                int savedIndentId = indentRepository.saveAndFlush(indent).getId();
                Optional<ElasticIndent> elasticIndentOptional = elasticIndentRepository.findByIid(savedIndentId);
                if(elasticIndentOptional.isPresent()){
                    ElasticIndent elasticIndent = elasticIndentOptional.get();
                    elasticIndent.setIid(indent.getId());
                    elasticIndent.setNo(indent.getNo());
                    elasticIndent.setCno(indent.getCustomer().getNo());
                    elasticIndent.setPno(indent.getProduct().getNo());
                    elasticIndent.setStatus(indent.getStatus());
                    elasticIndentRepository.save(elasticIndent);
                    resultMap.put(Check.status, true);
                    resultMap.put(Check.message, "Order successfully saved!");
                    resultMap.put(Check.result, savedIndentId);
                }
            }catch (Exception e){
                resultMap.put(Check.status, false);
                resultMap.put(Check.message, "An error occurred in save operation!");
            }
        }

        return resultMap;
    }

    @DeleteMapping("/delete/id={id}")
    @ResponseBody
    public Map<Check, Object> delete(@PathVariable Integer id) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        Optional<Indent> indentOptional = indentRepository.findById(id);
        Optional<ElasticIndent> elasticIndentOptional = elasticIndentRepository.findByIid(id);
        if(indentOptional.isPresent() && elasticIndentOptional.isPresent()){
            try{
                indentRepository.deleteById(indentOptional.get().getId());
                elasticIndentRepository.deleteById(elasticIndentOptional.get().getId());
                resultMap.put(Check.status, true);
                resultMap.put(Check.message, "Order has been deleted!");
            }catch (Exception e){
                resultMap.put(Check.status, false);
                resultMap.put(Check.message, "An error occurred in delete operation!");
            }
        }else{
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "No order found with given id!");
        }

        return resultMap;
    }

    @GetMapping("/change-status/id={id}status={status}")
    @ResponseBody
    public Map<Check, Object> changeStatus(@PathVariable Integer id, @PathVariable String status) {
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        Optional<Indent> indentOptional = indentRepository.findById(id);
        Optional<ElasticIndent> elasticIndentOptional = elasticIndentRepository.findByIid(id);
        if(indentOptional.isPresent() && elasticIndentOptional.isPresent()){
            try{
                Indent indent = indentOptional.get();
                ElasticIndent elasticIndent = elasticIndentOptional.get();
                indent.setStatus(status);
                elasticIndent.setStatus(status);
                indentRepository.save(indent);
                elasticIndentRepository.save(elasticIndent);
                resultMap.put(Check.status, true);
                resultMap.put(Check.result, 1);
            }catch (Exception e){
                resultMap.put(Check.status, false);
                resultMap.put(Check.message, "An error occurred during the operation.");
            }
        }
        return resultMap;
    }


}
