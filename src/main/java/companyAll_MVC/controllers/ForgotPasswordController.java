package companyAll_MVC.controllers;

import companyAll_MVC.entities.ReferenceHolder;
import companyAll_MVC.entities.User;
import companyAll_MVC.repositories._jpa.ReferenceHolderRepository;
import companyAll_MVC.repositories._jpa.UserRepository;
import companyAll_MVC.services.MailService;
import companyAll_MVC.utils.Check;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {

    final UserRepository userRepository;
    final ReferenceHolderRepository referenceHolderRepository;
    final MailService mailService;

    public ForgotPasswordController(UserRepository userRepository, ReferenceHolderRepository referenceHolderRepository, MailService mailService) {
        this.userRepository = userRepository;
        this.referenceHolderRepository = referenceHolderRepository;
        this.mailService = mailService;
    }

    @GetMapping("")
    public String forgotPassword(){
        return "forgotPassword";
    }

    @GetMapping("/send_email/email={email}")
    @ResponseBody
    public Map<Check, Object> sendEmail(@PathVariable String email){
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(email);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if (referenceHolderRepository.existsByUid(user.getId())) {
                resultMap.put(Check.status, false);
                resultMap.put(Check.message, "A code already sent to your email, please check again.");
            }
            String reference = "http://localhost:8080/newPassword/" + UUID.randomUUID();
            try{
                ReferenceHolder referenceHolder = new ReferenceHolder();
                referenceHolder.setUid(user.getId());
                referenceHolder.setReference(reference);
                referenceHolderRepository.save(referenceHolder);
                if(mailService.sendEmail(user.getEmail(), "Change Password", reference)){
                    resultMap.put(Check.status, true);
                    resultMap.put(Check.message, "A reference no sent to your email, please check it..");
                }else{
                    resultMap.put(Check.status, false);
                    resultMap.put(Check.message, "Mail couldn't send!");
                }
            }catch (Exception e){
                resultMap.put(Check.status, false);
                resultMap.put(Check.message, "An error occured during the operation!");
            }
        }else{
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "There is no user with this email!");
        }
        return resultMap;
    }
}
