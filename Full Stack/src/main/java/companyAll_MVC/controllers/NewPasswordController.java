package companyAll_MVC.controllers;

import companyAll_MVC.entities.ReferenceHolder;
import companyAll_MVC.entities.User;
import companyAll_MVC.properties.ChangePassword;
import companyAll_MVC.repositories._jpa.ReferenceHolderRepository;
import companyAll_MVC.repositories._jpa.UserRepository;
import companyAll_MVC.services.UserService;
import companyAll_MVC.utils.Check;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/newPassword")
public class NewPasswordController {

    final ReferenceHolderRepository referenceHolderRepository;
    final UserRepository userRepository;
    final UserService userService;
    private User user;

    public NewPasswordController(ReferenceHolderRepository referenceHolderRepository, UserRepository userRepository, UserService userService) {
        this.referenceHolderRepository = referenceHolderRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/{ref}")
    public String newPassword(@PathVariable String ref){
        String reference = "http://localhost:8080/newPassword/" + ref;
        Optional<ReferenceHolder> referenceHolderOptional = referenceHolderRepository.findByReference(reference);
        if(referenceHolderOptional.isPresent()){
            Optional<User> userOptional = userRepository.findById(referenceHolderOptional.get().getUid());
            if(userOptional.isPresent()){
                user = userOptional.get();
            }else{
                return "forgotPassword";
            }
            return "newPassword";
        }else{
            return "forgotPassword";
        }
    }

    @PostMapping("/setPassword")
    @ResponseBody
    public Map<Check, Object> newPassword(@RequestBody ChangePassword changePassword){
        Map<Check, Object> resultMap = new LinkedHashMap<>();
        if(changePassword.getNewPassword().equals(changePassword.getReNewPassword())){
            try{
                Optional<ReferenceHolder> referenceHolderOptional = referenceHolderRepository.findByUid(user.getId());
                if(referenceHolderOptional.isPresent()){
                    referenceHolderRepository.delete(referenceHolderOptional.get());
                    user.setPassword(userService.encoder().encode(changePassword.getNewPassword()));
                    userRepository.saveAndFlush(user);
                    resultMap.put(Check.status, true);
                    resultMap.put(Check.message, "Your password successfully changed!");
                }else{
                    resultMap.put(Check.status, false);
                    resultMap.put(Check.message, "Ref no doesnt exists!");
                }
            }catch (Exception e){
                resultMap.put(Check.status, false);
                resultMap.put(Check.message, "An error occured during the operation!");
                e.printStackTrace();
            }
        }else{
            resultMap.put(Check.status, false);
            resultMap.put(Check.message, "Your passwords don't match! ");
        }

        return resultMap;
    }

}
