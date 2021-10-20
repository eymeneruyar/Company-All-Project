package companyAll_MVC.controllers;

import companyAll_MVC.entities.User;
import companyAll_MVC.properties.ChangePassword;
import companyAll_MVC.properties.Company;
import companyAll_MVC.properties.UserInfo;
import companyAll_MVC.repositories._jpa.UserRepository;
import companyAll_MVC.services.UserService;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Util;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    final UserRepository userRepository;
    final UserService userService;
    public SettingsController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("")
    public String settings(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(email);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            model.addAttribute("user",user);
        }else {
            String error = "User is not found!";
            Util.logger(error,User.class);
            System.err.println(error);
        }

        return "settings";
    }

    //Section - 1 (Company)
    @ResponseBody
    @PostMapping("/updateCompany")
    public Map<Check,Object> updateCompany(@RequestBody @Valid Company company, BindingResult bindingResult){
        Map<Check,Object> map = new LinkedHashMap<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        if(!bindingResult.hasErrors()){
            try {
                Optional<User> userOptional = userRepository.findByEmailIgnoreCase(email);
                if (userOptional.isPresent()){
                    User us = userOptional.get();
                    us.setCompanyName(company.getCompanyName());
                    us.setCompanySector(company.getCompanySector());
                    us.setCompanyPhone(company.getCompanyPhone());
                    us.setCompanyAddress(company.getCompanyAddress());
                    User updatedUser = userRepository.saveAndFlush(us);
                    map.put(Check.status,true);
                    map.put(Check.message,"Company Information Update Successful!");
                    map.put(Check.result,updatedUser);
                }else{
                    String error = "User is not found!";
                    map.put(Check.status,false);
                    map.put(Check.message,error);
                    Util.logger(error,User.class);
                    System.err.println(error);
                }
            } catch (Exception e) {
                String error = "An error occurred during the operation!";
                map.put(Check.status,false);
                map.put(Check.message,error);
                Util.logger(error,User.class);
                System.err.println(e);
            }
        }else{
            map.put(Check.status,false);
            map.put(Check.errors, Util.errors(bindingResult));
        }

        return map;
    }

    //Section - 2 (Change Password)
    @ResponseBody
    @PostMapping("/changePassword")
    public Map<Check,Object> changePassword(@RequestBody ChangePassword changePassword){
        Map<Check,Object> map = new LinkedHashMap<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(email);
        if (userOptional.isPresent()){
            if ( changePassword.getOldPassword().equals("") || changePassword.getNewPassword().equals("") || changePassword.getReNewPassword().equals("")){
                map.put(Check.status,false);
                map.put(Check.message,"Please fill in the specified!");
            }else {
                if(changePassword.getNewPassword().equals(changePassword.getReNewPassword())){
                    User us = userOptional.get();
                    us.setPassword(userService.encoder().encode(changePassword.getNewPassword()));
                    User user = userRepository.saveAndFlush(us);
                    map.put(Check.status,true);
                    map.put(Check.message,"Password changed successfully!");
                    map.put(Check.result,user);
                }else {
                    map.put(Check.status,false);
                    map.put(Check.message,"New password and its repetition don't match!");
                }
            }
        }else{
            String error = "User is not found!";
            map.put(Check.status,false);
            map.put(Check.message,error);
            Util.logger(error,User.class);
            System.err.println(error);
        }

        return map;
    }

    //Section - 3 (User)
    @ResponseBody
    @PostMapping("/userInfo")
    public Map<Check,Object> userInfo(@RequestBody @Valid UserInfo userInfo, BindingResult bindingResult){
        Map<Check,Object> map = new LinkedHashMap<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        if(!bindingResult.hasErrors()){
            try {
                Optional<User> userOptional = userRepository.findByEmailIgnoreCase(email);
                if (userOptional.isPresent()){
                    User us = userOptional.get();
                    us.setName(userInfo.getName());
                    us.setSurname(userInfo.getSurname());
                    us.setEmail(userInfo.getEmail());
                    us.setBirthday(userInfo.getBirthday());
                    us.setBio(userInfo.getBio());
                    User updatedUser = userRepository.saveAndFlush(us);
                    map.put(Check.status,true);
                    map.put(Check.message,"User Information Update Successful!");
                    map.put(Check.result,updatedUser);
                }else{
                    String error = "User is not found!";
                    map.put(Check.status,false);
                    map.put(Check.message,error);
                    Util.logger(error,User.class);
                    System.err.println(error);
                }
            } catch (Exception e) {
                String error = "An error occurred during the operation!";
                map.put(Check.status,false);
                map.put(Check.message,error);
                Util.logger(error,User.class);
                System.err.println(e);
            }
        }else{
            map.put(Check.status,false);
            map.put(Check.errors, Util.errors(bindingResult));
        }
        return map;
    }

}
