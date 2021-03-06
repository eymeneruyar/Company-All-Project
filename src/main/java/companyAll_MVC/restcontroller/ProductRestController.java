package companyAll_MVC.restcontroller;

import companyAll_MVC.dto.ProductDto;
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
@RequestMapping("/api/product")
@Api(value = "ProductRestController",authorizations = {@Authorization(value = "basicAuth")})
public class ProductRestController {

    final ProductDto productDto;

    public ProductRestController(ProductDto productDto) {
        this.productDto = productDto;
    }

    //Product category list
    @GetMapping("/categoryList/{stShowNumber}/{stPageNo}")
    @ApiOperation(value = "Product category is listed by page no and number of show.")
    public Map<Check, Object> productCategoryList(@PathVariable String stShowNumber, @PathVariable String stPageNo) {
        return productDto.categoryList(stShowNumber, stPageNo);
    }

    //Last added 10 product
    @GetMapping("/productListLast10")
    @ApiOperation(value = "Last added product is listed.")
    public Map<Check, Object> listLastAdd10Prodcut() {
        return productDto.addedLast10product();
    }

    //List of product with pagination
    @GetMapping("/list/{stShowNumber}/{stPageNo}")
    @ApiOperation(value = "Products is listed by page no and number of show.")
    public Map<Check, Object> listProductwithPagination(@PathVariable String stShowNumber, @PathVariable String stPageNo) {
        return productDto.listProductwithPagination(stShowNumber, stPageNo);
    }

    //Product List by Category Id
    @GetMapping("/listByCategoryId/{stId}/{stShowNumber}/{stPageNo}")
    @ApiOperation(value = "Products is listed by page no, number of show and category id.")
    public Map<Check, Object> productListByCategoryId(@PathVariable String stId, @PathVariable String stShowNumber, @PathVariable String stPageNo) {
        return productDto.productListByCategoryId(stId, stShowNumber, stPageNo);
    }

    //Elasticsearch for product
    @GetMapping("/search/{data}/{stPageNo}/{stShowNumber}")
    @ApiOperation(value = "Products is searched by page no and number of show.")
    public Map<Check,Object> searchProduct(@PathVariable String data,@PathVariable String stPageNo,@PathVariable String stShowNumber) {
        return productDto.searchProduct(data, stPageNo, stShowNumber);
    }

    //Product Detail
    @GetMapping("/detail/{stId}")
    @ApiOperation(value = "Product is showed details by product id.")
    public Map<Check,Object> detail(@PathVariable String stId){
        return productDto.detail(stId);
    }

}
