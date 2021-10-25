package companyAll_MVC.dto;

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
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderDto {

    final IndentRepository indentRepository;
    final ElasticIndentRepository elasticIndentRepository;
    final ProductRepository productRepository;
    final CustomerRepository customerRepository;

    public OrderDto(IndentRepository indentRepository, ElasticIndentRepository elasticIndentRepository, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.indentRepository = indentRepository;
        this.elasticIndentRepository = elasticIndentRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    public Map<Check, Object> get(Integer id) {
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

    public Map<Check, Object> add(Indent indent, BindingResult bindingResult) {
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

    public Map<Check, Object> update(Indent indent, BindingResult bindingResult) {
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

    public Map<Check, Object> delete(Integer id) {
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

}
