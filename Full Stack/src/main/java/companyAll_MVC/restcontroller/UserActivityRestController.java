package companyAll_MVC.restcontroller;

import companyAll_MVC.entities.UserActivity;
import companyAll_MVC.services.UserService;
import companyAll_MVC.utils.Check;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class UserActivityRestController {

    final UserService userService;
    public UserActivityRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/userActivity")
    @ApiOperation(value = "Sistemde olan kullanıcının bilgilerini getirir.")
    public Map<Check,Object> list(HttpServletRequest req, HttpServletResponse res, UserActivity userActivity) throws IOException {
        Map<Check,Object> hm = new LinkedHashMap<>();
        hm.put(Check.status,true);
        hm.put(Check.result,userService.info(req,res,userActivity));
        return hm;
    }

}
