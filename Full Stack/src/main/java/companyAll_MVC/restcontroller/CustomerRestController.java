package companyAll_MVC.restcontroller;

import companyAll_MVC.dto.CustomerDto;
import companyAll_MVC.entities.Address;
import companyAll_MVC.entities.Customer;
import companyAll_MVC.utils.Check;
import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
@Api(value = "CustomerRestController",authorizations = {@Authorization(value = "basicAuth")})
public class CustomerRestController {

    final CustomerDto customerDto;

    public CustomerRestController(CustomerDto customerDto) {
        this.customerDto = customerDto;
    }

    @GetMapping("/get/id={id}")
    @ResponseBody
    public Map<Check, Object> get(@PathVariable Integer id) {
        return customerDto.get(id);
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<Check, Object> add(@RequestBody @Valid Customer customer, BindingResult bindingResult) {
        return customerDto.add(customer, bindingResult);
    }

    @PostMapping("/update")
    @ResponseBody
    public Map<Check, Object> update(@RequestBody @Valid Customer customer, BindingResult bindingResult) {
        return customerDto.update(customer, bindingResult);
    }

    @DeleteMapping("/delete/id={id}")
    @ResponseBody
    public Map<Check, Object> delete(@PathVariable Integer id) {
        return customerDto.delete(id);
    }

    @GetMapping("/addresses/cid={cid}")
    @ResponseBody
    public Map<Check, Object> addresses(@PathVariable Integer cid) {
        return customerDto.addresses(cid);
    }

    @PostMapping("/address/update")
    @ResponseBody
    public Map<Check, Object> updateAddress(@RequestBody @Valid Address address, BindingResult bindingResult) {
        return customerDto.updateAddress(address, bindingResult);
    }

    @DeleteMapping("/address/delete/cid={cid}aid={aid}")
    @ResponseBody
    public Map<Check, Object> deleteAddress(@PathVariable Integer cid, @PathVariable Integer aid) {
        return customerDto.deleteAddress(cid, aid);
    }
}
