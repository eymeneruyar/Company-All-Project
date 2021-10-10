package companyAll_MVC.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/productDetail")
public class ProductDetailsController {

    @GetMapping("")
    public String productDetails(){
        return "productDetails";
    }

}
