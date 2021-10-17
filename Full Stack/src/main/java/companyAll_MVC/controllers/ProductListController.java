package companyAll_MVC.controllers;

import companyAll_MVC.entities.Product;
import companyAll_MVC.entities.ProductCategory;
import companyAll_MVC.repositories._elastic.ElasticProductCategoryRepository;
import companyAll_MVC.repositories._elastic.ElasticProductRepository;
import companyAll_MVC.repositories._jpa.ProductCategoryRepository;
import companyAll_MVC.repositories._jpa.ProductRepository;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Util;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/productList")
public class ProductListController {

    final ProductRepository productRepository;
    final ProductCategoryRepository productCategoryRepository;
    final ElasticProductCategoryRepository elasticProductCategoryRepository;
    final ElasticProductRepository elasticProductRepository;

    public ProductListController(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository, ElasticProductCategoryRepository elasticProductCategoryRepository, ElasticProductRepository elasticProductRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.elasticProductCategoryRepository = elasticProductCategoryRepository;
        this.elasticProductRepository = elasticProductRepository;
    }

    @GetMapping("")
    public String productList(){
        return "productsList";
    }

    //------------------------------------ Product Category List - Start ------------------------------------//
    @ResponseBody
    @GetMapping("/categories")
    public Map<Check,Object> categories(){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            map.put(Check.status,true);
            map.put(Check.message,"Product category list operation succesful!");
            map.put(Check.result,productCategoryRepository.findAll());
        } catch (Exception e) {
            String error = "An error occurred during the operation!";
            Util.logger(error, ProductCategory.class);
            map.put(Check.status,false);
            map.put(Check.message,error);
            System.err.println(e);
        }
        return map;
    }
    //------------------------------------ Product Category List - End --------------------------------------//

    //------------------------------------ Product List by Category Id - Start ------------------------------------//
    @ResponseBody
    @GetMapping("/listByCategoryId/{stId}/{stShowNumber}/{stPageNo}")
    public Map<Check,Object> productListByCategoryId(@PathVariable String stId,@PathVariable String stShowNumber,@PathVariable String stPageNo){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int categoryId = Integer.parseInt(stId);
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo,showNumber);
            Page<Product> productPage = productRepository.findByProductCategories_Id(categoryId,pageable);
            map.put(Check.status,true);
            map.put(Check.message,"Product list operation succesful!");
            map.put(Check.totalPage,productPage.getTotalPages());
            map.put(Check.result,productPage.getContent());
        } catch (Exception e) {
            String error = "An error occurred during the operation!";
            Util.logger(error, Product.class);
            map.put(Check.status,false);
            map.put(Check.message,error);
            System.err.println(e);
        }
        return map;
    }
    //------------------------------------ Product List by Category Id List - End --------------------------------------//

}
