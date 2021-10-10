package companyAll_MVC.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductAddController {

    @GetMapping("")
    public String productAdd(){
        return "productAdd";
    }

}
