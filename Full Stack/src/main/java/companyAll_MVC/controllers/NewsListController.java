package companyAll_MVC.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/newsList")
public class NewsListController {

    @GetMapping("")
    public String newsList(){
        return "newsList";
    }

}
