package companyAll_MVC.restcontroller;

import companyAll_MVC.dto.ContentsDto;
import companyAll_MVC.utils.Check;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/contents")
@Api(value = "ProductRestController",authorizations = {@Authorization(value = "basicAuth")})
public class ContentsRestController {

    final ContentsDto contentsDto;
    public ContentsRestController(ContentsDto contentsDto) {
        this.contentsDto = contentsDto;
    }

    @GetMapping("/list/{stShowNumber}/{stPageNo}")
    @ApiOperation(value = "It lists the contents according to the entered value and page number.")
    public Map<Check,Object> list(@PathVariable String stShowNumber, @PathVariable String stPageNo){
        return contentsDto.list(stShowNumber, stPageNo);
    }

}
