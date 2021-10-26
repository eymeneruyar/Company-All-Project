package companyAll_MVC.restcontroller;

import companyAll_MVC.dto.AdvertisementDto;
import companyAll_MVC.utils.Check;
import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/advertisement")
@Api(value = "AdvertisementRestController",authorizations = {@Authorization(value = "basicAuth")})
public class AdvertisementRestController {

    final AdvertisementDto advertisementDto;

    public AdvertisementRestController(AdvertisementDto advertisementDto) {
        this.advertisementDto = advertisementDto;
    }

    @GetMapping("/get/id={id}")
    @ResponseBody
    public Map<Check, Object> get(@PathVariable Integer id) {
        return advertisementDto.get(id);
    }

    @GetMapping(value = "/get/image/name={image}")
    @ResponseBody
    public Map<Check, Object> getImage(@PathVariable String image) throws IOException {
        return advertisementDto.getImage(image);
    }

    @GetMapping("/click/id={id}")
    @ResponseBody
    public Map<Check, Object> click(@PathVariable Integer id) {
        return advertisementDto.click(id);
    }

    @GetMapping("/view/id={id}")
    @ResponseBody
    public Map<Check, Object> view(@PathVariable Integer id) {
        return advertisementDto.view(id);
    }

}
