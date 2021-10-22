package companyAll_MVC.restcontroller;

import companyAll_MVC.dto.DashboardDto;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Statics;
import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@Api(value = "DashboardRestController",authorizations = {@Authorization(value = "basicAuth")})
public class DashboardRestController {

    final DashboardDto dashboardDto;
    public DashboardRestController(DashboardDto dashboardDto) {
        this.dashboardDto = dashboardDto;
    }

    //General Statics Information
    @ResponseBody
    @GetMapping("/generalStatics")
    public Map<Statics,Object> generalStatics(){
        return dashboardDto.generalStatics();
    }

    //Last Added 6 Product
    @ResponseBody
    @GetMapping("/lastAddSixProduct")
    public Map<Statics,Object> lastAddSixProduct(){
        return dashboardDto.lastAddSixProduct();
    }

    //Last Six Order
    @ResponseBody
    @GetMapping("/lastSixOrder")
    public Map<Statics,Object> lastSixOrder(){
        return dashboardDto.lastSixOrder();
    }

    //Total product by Category Id
    @ResponseBody
    @GetMapping("/totalProductByCategoryId/{stId}")
    public Map<Check,Object> totalProductByCategoryId(@PathVariable String stId){
        return dashboardDto.totalProductByCategoryId(stId);
    }

}
