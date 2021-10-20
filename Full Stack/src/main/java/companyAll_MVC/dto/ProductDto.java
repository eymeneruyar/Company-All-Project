package companyAll_MVC.dto;

import companyAll_MVC.documents.ElasticProduct;
import companyAll_MVC.documents.ElasticProductCategory;
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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.swing.text.html.Option;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductDto {

    final ProductRepository productRepository;
    final ProductCategoryRepository productCategoryRepository;
    final ElasticProductCategoryRepository elasticProductCategoryRepository;
    final ElasticProductRepository elasticProductRepository;

    public ProductDto(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository, ElasticProductCategoryRepository elasticProductCategoryRepository, ElasticProductRepository elasticProductRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.elasticProductCategoryRepository = elasticProductCategoryRepository;
        this.elasticProductRepository = elasticProductRepository;
    }

    //================================== Product Category Section - Start ==================================//

    //Product category List
    public Map<Check,Object> categoryList(@PathVariable String stShowNumber, @PathVariable String stPageNo){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo,showNumber);
            Page<ElasticProductCategory> categoryPage = elasticProductCategoryRepository.findByOrderByIdAsc(pageable);
            map.put(Check.status,true);
            map.put(Check.totalPage,categoryPage.getTotalPages());
            map.put(Check.message, "Content listing on page " + (pageNo + 1) + " is successful");
            map.put(Check.result,categoryPage.getContent());
        } catch (Exception e) {
            map.put(Check.status,false);
            String error = "An error occurred during the operation!";
            System.err.println(e);
            Util.logger(error, ProductCategory.class);
            map.put(Check.message,error);
        }
        return map;
    }

    //================================== Product Category Section - End ====================================//

    //================================== Product Section - Start ====================================//

    //Last added 10 product
    public Map<Check,Object> addedLast10product(){
        Map<Check,Object> map = new LinkedHashMap<>();
        List <ElasticProduct> elasticProductList = elasticProductRepository.findAllProducts();
        System.out.println(elasticProductList);
        System.out.println(elasticProductList.size());
        int size = elasticProductList.size();
        //elasticProductRepository.findElasticProductsBySizeBetween(size-11,size-1);
        return map;
    }

    //List of product with pagination
    public Map<Check, Object> listProductwithPagination(String stShowNumber,String stPageNo){
        Map<Check,Object> map = new LinkedHashMap<>();
        //System.out.println(stShowNumber + " " + stPageNo);
        try {
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo,showNumber);
            Page<ElasticProduct> productPage = elasticProductRepository.findByOrderByIdAsc(pageable);
            map.put(Check.status,true);
            map.put(Check.totalPage,productPage.getTotalPages());
            map.put(Check.message, "Product listing on page " + (pageNo + 1) + " is successful");
            map.put(Check.result,productPage.getContent());
        } catch (Exception e) {
            map.put(Check.status,false);
            String error = "An error occurred during the operation!";
            System.err.println(e);
            Util.logger(error, Product.class);
            map.put(Check.message,error);
        }
        return map;
    }

    //Product List by Category Id
    public Map<Check,Object> productListByCategoryId(String stId,String stShowNumber,String stPageNo){
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

    //Elasticsearch for product
    public Map<Check,Object> searchProduct(String data,String stPageNo,String stShowNumber){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo,showNumber);
            Page<ElasticProduct> searchPage = elasticProductRepository.findBySearchDataMatch(data,pageable);
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

    //Product detail
    public Map<Check,Object> detail(String stId){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<ElasticProduct> elasticProductOptional = elasticProductRepository.findById(id);
            if(elasticProductOptional.isPresent()){
                ElasticProduct elasticProduct = elasticProductOptional.get();
                map.put(Check.status,true);
                map.put(Check.message,"Product detail operation success!");
                map.put(Check.result,elasticProduct);
            }else {
                map.put(Check.status,false);
                map.put(Check.message,"Product detail is not found!");
            }
        } catch (Exception e) {
            map.put(Check.status,false);
            String error = "An error occurred during the search operation!";
            System.err.println(e);
            Util.logger(error, Product.class);
            map.put(Check.message,error);
        }
        return map;
    }

    //================================== Product Section - End ======================================//

}
