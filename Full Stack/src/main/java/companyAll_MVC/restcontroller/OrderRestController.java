package companyAll_MVC.restcontroller;

import companyAll_MVC.dto.OrderDto;
import companyAll_MVC.entities.Indent;
import companyAll_MVC.utils.Check;
import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@Api(value = "OrderRestController",authorizations = {@Authorization(value = "basicAuth")})
public class OrderRestController {

    final OrderDto orderDto;

    public OrderRestController(OrderDto orderDto) {
        this.orderDto = orderDto;
    }

    @GetMapping("/get/id={id}")
    @ResponseBody
    public Map<Check, Object> get(@PathVariable Integer id) {
        return orderDto.get(id);
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<Check, Object> add(@RequestBody @Valid Indent indent, BindingResult bindingResult) {
        return orderDto.add(indent, bindingResult);
    }

    @PostMapping("/update")
    @ResponseBody
    public Map<Check, Object> update(@RequestBody @Valid Indent indent, BindingResult bindingResult) {
        return orderDto.update(indent, bindingResult);
    }

    @DeleteMapping("/delete/id={id}")
    @ResponseBody
    public Map<Check, Object> delete(@PathVariable Integer id) {
        return orderDto.delete(id);
    }

}
