package companyAll_MVC.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/news")
public class NewsAddController {

    @GetMapping("")
    public String newsAdd(){
        return "newsAdd";
    }

}
