package companyAll_MVC.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/advertisement")
public class AdvertisementController {

    @GetMapping("")
    public String advertisement(){
        return "advertisement";
    }

}
