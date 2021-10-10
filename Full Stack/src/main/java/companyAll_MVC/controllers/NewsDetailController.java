package companyAll_MVC.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/newsDetail")
public class NewsDetailController {

    @GetMapping("")
    public String newsDetail(){
        return "newsDetail";
    }

}
