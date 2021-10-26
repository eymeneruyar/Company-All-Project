package companyAll_MVC.controllers;

import companyAll_MVC.documents.ElasticProduct;
import companyAll_MVC.entities.Product;
import companyAll_MVC.entities.ProductCategory;
import companyAll_MVC.model.RedisProduct;
import companyAll_MVC.model.RedisProductCategory;
import companyAll_MVC.repositories._elastic.ElasticProductCategoryRepository;
import companyAll_MVC.repositories._elastic.ElasticProductRepository;
import companyAll_MVC.repositories._jpa.ProductCategoryRepository;
import companyAll_MVC.repositories._jpa.ProductRepository;
import companyAll_MVC.repositories._redis.RedisProductCategoryRepository;
import companyAll_MVC.repositories._redis.RedisProductRepository;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Util;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/productList")
public class ProductListController {

    final ProductRepository productRepository;
    final ProductCategoryRepository productCategoryRepository;
    final ElasticProductCategoryRepository elasticProductCategoryRepository;
    final ElasticProductRepository elasticProductRepository;
    final RedisProductRepository redisProductRepository;
    final RedisProductCategoryRepository redisProductCategoryRepository;

    public ProductListController(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository, ElasticProductCategoryRepository elasticProductCategoryRepository, ElasticProductRepository elasticProductRepository, RedisProductRepository redisProductRepository, RedisProductCategoryRepository redisProductCategoryRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.elasticProductCategoryRepository = elasticProductCategoryRepository;
        this.elasticProductRepository = elasticProductRepository;
        this.redisProductRepository = redisProductRepository;
        this.redisProductCategoryRepository = redisProductCategoryRepository;
    }

    @GetMapping("")
    public String productList(Model model){

        model.addAttribute("totalProduct_ZeroToOne",productRepository.countByTotalLikeGreaterThanEqualAndTotalLikeLessThan(0.0,1.0));
        model.addAttribute("totalProduct_OneToTwo",productRepository.countByTotalLikeGreaterThanEqualAndTotalLikeLessThan(1.0,2.0));
        model.addAttribute("totalProduct_TwoToThree",productRepository.countByTotalLikeGreaterThanEqualAndTotalLikeLessThan(2.0,3.0));
        model.addAttribute("totalProduct_ThreeToFour",productRepository.countByTotalLikeGreaterThanEqualAndTotalLikeLessThan(3.0,4.0));
        model.addAttribute("totalProduct_FourToFive",productRepository.countByTotalLikeGreaterThanEqualAndTotalLikeLessThan(4.0,5.0));
        model.addAttribute("totalProduct_FiveToFive",productRepository.countByTotalLikeEquals(5.0));

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

    //------------------------------------ Product Insert the Redis Database - Start --------------------------------------//
    @GetMapping("/insertDataRedis")
    public Map<Check,Object> add(){
        Map<Check,Object> map = new LinkedHashMap<>();
        List<Product> productList = productRepository.findAll();
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();

        if(productList != null && productCategoryList != null){
            if(productList.size() > 0 && productCategoryList.size() > 0){
                map.put(Check.status,true);
                map.put(Check.message,"Ürün ve kategori ekleme işlemi başarılı!");
                productList.forEach(item->{
                    RedisProduct rp = new RedisProduct();
                    List<String> fileName = new ArrayList<>();
                    List<Integer> proCatId = new ArrayList<>();
                    rp.setProductId(item.getId());
                    rp.setNo(item.getNo());
                    rp.setName(item.getName());
                    rp.setDescription(item.getDescription());
                    rp.setPrice(item.getPrice());
                    rp.setDetails(item.getDetails());
                    rp.setDate(item.getDate());
                    rp.setStatus(item.getStatus());
                    item.getFileName().forEach(it->{
                        fileName.add(it);
                    });
                    rp.setFileName(fileName);
                    rp.setCampaignStatus(item.getCampaignStatus());
                    rp.setCampaignName(item.getCampaignName());
                    rp.setCampaignDetails(item.getCampaignDetails());
                    item.getProductCategories().forEach(it->{
                        proCatId.add(it.getId());
                    });
                    rp.setProductCategoryId(proCatId);
                    redisProductRepository.save(rp);
                });
                productCategoryList.forEach(item->{
                    RedisProductCategory rpc = new RedisProductCategory();
                    rpc.setCategoryId(item.getId());
                    rpc.setNo(item.getNo());
                    rpc.setName(item.getName());
                    rpc.setStatus(item.getStatus());
                    rpc.setDate(item.getDate());
                    rpc.setDetails(item.getDetails());
                    redisProductCategoryRepository.save(rpc);
                });
            }
        }else{
            map.put(Check.status,false);
            map.put(Check.message,"Redis veri tabanına veriler yüklenirken bir hata oluştu!");
            map.put(Check.result,null);
        }

        return map;
    }
    //------------------------------------ Product Insert the Redis Database - End ----------------------------------------//

    //------------------------------------ Product List from category Id with Redis - Start ----------------------------------------//
    @ResponseBody
    @GetMapping("/listByCategoryIdRedis/{stId}/{stShowNumber}/{stPageNo}")
    public Map<Check,Object> productListByCategoryIdRedis(@PathVariable String stId,@PathVariable String stShowNumber,@PathVariable String stPageNo){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int categoryId = Integer.parseInt(stId);
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo,showNumber);
            Page<RedisProduct> redisProductPage = redisProductRepository.findByProductCategoryIdEquals(categoryId,pageable);
            map.put(Check.status,true);
            map.put(Check.message,"Product list operation succesful!");
            map.put(Check.totalPage,redisProductPage.getTotalPages());
            map.put(Check.result,redisProductPage.getContent());
        } catch (Exception e) {
            String error = "An error occurred during the operation!";
            Util.logger(error, Product.class);
            map.put(Check.status,false);
            map.put(Check.message,error);
            System.err.println(e);
        }
        return map;
    }
    //------------------------------------ Product List from category Id with Redis - End ------------------------------------------//

    //------------------------------------ Product List from category Id with Elasticsearch - Start ----------------------------------------//
    @ResponseBody
    @GetMapping("/listByCategoryIdElasticsearch/{stId}/{stShowNumber}/{stPageNo}")
    public Map<Check,Object> productListByCategoryIdElasticsearch(@PathVariable String stId,@PathVariable String stShowNumber,@PathVariable String stPageNo){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int categoryId = Integer.parseInt(stId);
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo,showNumber);
            Page<ElasticProduct> elasticProductPage = elasticProductRepository.findByProductCategoryIdEquals(categoryId,pageable);
            map.put(Check.status,true);
            map.put(Check.message,"Product list operation succesful!");
            map.put(Check.totalPage,elasticProductPage.getTotalPages());
            map.put(Check.result,elasticProductPage.getContent());
        } catch (Exception e) {
            String error = "An error occurred during the operation!";
            Util.logger(error, Product.class);
            map.put(Check.status,false);
            map.put(Check.message,error);
            System.err.println(e);
        }
        return map;
    }
    //------------------------------------ Product List from category Id with Elasticsearch - End ------------------------------------------//

    //------------------------------------ Elasticsearch for product - Start ------------------------------------//
    @ResponseBody
    @GetMapping("/search/{data}/{stPageNo}/{stShowNumber}")
    public Map<Check,Object> searchProduct(@PathVariable String data,@PathVariable String stPageNo,@PathVariable String stShowNumber){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo,showNumber);
            Page<ElasticProduct> searchPage = elasticProductRepository.findBySearchData(data,pageable);
            List<ElasticProduct> elasticProductList = searchPage.getContent();
            int totalData =  elasticProductList.size(); //for total data in table
            if(totalData > 0 ){
                map.put(Check.status,true);
                map.put(Check.totalPage,searchPage.getTotalPages());
                map.put(Check.message,"Search operation success!");
                map.put(Check.result,elasticProductList);
            }else{
                map.put(Check.status,false);
                map.put(Check.message,"Could not find a result for your search!");
            }
        } catch (NumberFormatException e) {
            map.put(Check.status,false);
            String error = "An error occurred during the search operation!";
            System.err.println(e);
            Util.logger(error, Product.class);
            map.put(Check.message,error);
        }
        return map;
    }
    //------------------------------------ Elasticsearch for product - End --------------------------------------//

}
