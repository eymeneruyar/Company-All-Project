package companyAll_MVC.controllers;

import companyAll_MVC.entities.ProductCategory;
import companyAll_MVC.entities.Role;
import companyAll_MVC.entities.User;
import companyAll_MVC.repositories._jpa.RoleRepository;
import companyAll_MVC.services.UserService;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Util;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/register")
public class RegisterController {

    final UserService userService;
    final RoleRepository roleRepository;
    public RegisterController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("")
    public String register(){
        return "register";
    }

    @ResponseBody
    @PostMapping("/save")
    public Map<Check,Object> save(@RequestBody @Valid User user, BindingResult bindingResult){
        Map<Check,Object> map = new LinkedHashMap<>();
        List<Role> roleList = new ArrayList<>();
        if(!bindingResult.hasErrors()){
            try {
                int roleId = 1; //ADMIN
                user.setEnabled(true);
                user.setTokenExpired(true);
                user.setProfileImage("defaultProfileImage.png");
                Role role = roleRepository.findById(roleId).get();
                roleList.add(role);
                user.setRoles(roleList);
                User newUser = userService.register(user);
                map.put(Check.status,true);
                map.put(Check.message,"Registration operation succes!");
                map.put(Check.result,newUser);
            } catch (Exception e) {
                map.put(Check.status,false);
                if(e.toString().contains("constraint")){
                    String error = "This user has already been registered!";
                    Util.logger(error, User.class);
                    map.put(Check.message,error);
                }
            }
        }else{
            map.put(Check.status,false);
            map.put(Check.errors, Util.errors(bindingResult));
        }
        return map;
    }

}
