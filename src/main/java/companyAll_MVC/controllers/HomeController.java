package companyAll_MVC.controllers;

import companyAll_MVC.properties.ContactMailHolder;
import companyAll_MVC.services.MailService;
import companyAll_MVC.utils.Check;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/home")
public class HomeController {

    final MailService mailService;
    public HomeController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("")
    public String home(){
        return "home";
    }

    @PostMapping("/contact_email")
    @ResponseBody
    public Map<Check, Object> contactEmail(@RequestBody ContactMailHolder contactMailHolder){
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        if(contactMailHolder.getName().equals("") || contactMailHolder.getName() == null){
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "Invalid name!");
        }
        if(contactMailHolder.getSubject().equals("") || contactMailHolder.getSubject() == null){
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "Invalid subject!");
        }
        if(contactMailHolder.getMessage().equals("") || contactMailHolder.getMessage() == null){
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "Invalid message!");
        }else{
            if(mailService.sendEmail("companyallcontact@gmail.com", contactMailHolder.getSubject(), contactMailHolder.getMessage())){
                resultMap.put(Check.status, true);
                resultMap.put(Check.message, "Your mail has been sent!");
            }else{
                resultMap.put(Check.status, false);
                resultMap.put(Check.message, "Mail couldn't send!");
            }
        }


        return resultMap;
    }

}
