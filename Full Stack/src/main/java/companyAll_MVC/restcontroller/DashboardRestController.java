package companyAll_MVC.restcontroller;

import companyAll_MVC.dto.DashboardDto;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Statics;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    //News Chart
    @ResponseBody
    @GetMapping("/newsChart")
    @ApiOperation(value = "News statics shows.")
    public Map<Statics,Object> newsChart(){
        return dashboardDto.newsChart();
    }

    //General Statics Information
    @ResponseBody
    @GetMapping("/generalStatics")
    @ApiOperation(value = "General statics shows for firm.")
    public Map<Statics,Object> generalStatics(){
        return dashboardDto.generalStatics();
    }

    //Last Added 6 Product
    @ResponseBody
    @GetMapping("/lastAddSixProduct")
    @ApiOperation(value = "Last added six product shows for the firm.")
    public Map<Statics,Object> lastAddSixProduct(){
        return dashboardDto.lastAddSixProduct();
    }

    //Last Six Order
    @ResponseBody
    @GetMapping("/lastSixOrder")
    @ApiOperation(value = "Last added six order shows for the firm.")
    public Map<Statics,Object> lastSixOrder(){
        return dashboardDto.lastSixOrder();
    }

    //Total product by Category Id
    @ResponseBody
    @GetMapping("/totalProductByCategoryId/{stId}")
    @ApiOperation(value = "Total product is listed by category id.")
    public Map<Check,Object> totalProductByCategoryId(@PathVariable String stId){
        return dashboardDto.totalProductByCategoryId(stId);
    }

    //Daily Announcment
    @ResponseBody
    @GetMapping("/dailyAnnouncment/{stPageNo}")
    @ApiOperation(value = "Daily announcment is listed by page no.")
    public Map<Check,Object> dailyAnnouncment(@PathVariable String stPageNo){
        return dashboardDto.dailyAnnouncment(stPageNo);
    }

}
