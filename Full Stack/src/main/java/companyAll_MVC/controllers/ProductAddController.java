package companyAll_MVC.controllers;

import companyAll_MVC.documents.ElasticContents;
import companyAll_MVC.documents.ElasticProduct;
import companyAll_MVC.documents.ElasticProductCategory;
import companyAll_MVC.entities.Contents;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/product")
public class ProductAddController {

    final ProductRepository productRepository;
    final ProductCategoryRepository productCategoryRepository;
    final ElasticProductCategoryRepository elasticProductCategoryRepository;
    final ElasticProductRepository elasticProductRepository;

    public ProductAddController(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository, ElasticProductCategoryRepository elasticProductCategoryRepository, ElasticProductRepository elasticProductRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.elasticProductCategoryRepository = elasticProductCategoryRepository;
        this.elasticProductRepository = elasticProductRepository;
    }

    @GetMapping("")
    public String productAdd(){
        return "productAdd";
    }

    //====================================== Category Section - Start ======================================//

    //Product category add
    @ResponseBody
    @PostMapping("/categoryAdd")
    public Map<Check,Object> categoryAdd(@RequestBody @Valid ProductCategory pc, BindingResult bindingResult){
        Map<Check,Object> map = new LinkedHashMap<>();
        if(!bindingResult.hasErrors()){
            if( pc.getId() != null ){
                pc.setDate(Util.getDateFormatter());
                String no = productCategoryRepository.findById(pc.getId()).get().getNo();
                pc.setNo(no);
                Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findById(pc.getId());
                if(optionalProductCategory.isPresent()){
                    try {
                        //ElasticSearch and SQL DB Update -Start
                        ElasticProductCategory elasticProductCategory = elasticProductCategoryRepository.findById(pc.getId()).get();
                        elasticProductCategoryRepository.deleteById(elasticProductCategory.getId());
                        ProductCategory productCategory = productCategoryRepository.saveAndFlush(pc);
                        ElasticProductCategory elasticProductCategoryNew = new ElasticProductCategory();
                        elasticProductCategoryNew.setCategoryId(productCategory.getId());
                        elasticProductCategoryNew.setNo(productCategory.getNo());
                        elasticProductCategoryNew.setName(productCategory.getName());
                        elasticProductCategoryNew.setDetails(productCategory.getDetails());
                        elasticProductCategoryNew.setDate(productCategory.getDate());
                        elasticProductCategoryNew.setStatus(productCategory.getStatus());
                        elasticProductCategoryRepository.save(elasticProductCategoryNew);
                        //ElasticSearch and SQL DB Update - End
                        map.put(Check.status,true);
                        map.put(Check.message,"Updated operations success!");
                        map.put(Check.result,productCategory);
                    } catch (Exception e) {
                        System.err.println("Elasticsearch product category update" + e);
                    }
                }
            }else{
                try {
                    pc.setDate(Util.getDateFormatter());
                    ProductCategory productCategory = productCategoryRepository.saveAndFlush(pc);
                    map.put(Check.status,true);
                    map.put(Check.message,"Adding of Product Category Operations Successful!");
                    map.put(Check.result,productCategory);
                    //Elasticsearch save
                    ElasticProductCategory elasticProductCategory = new ElasticProductCategory();
                    elasticProductCategory.setCategoryId(productCategory.getId());
                    elasticProductCategory.setNo(productCategory.getNo());
                    elasticProductCategory.setName(productCategory.getName());
                    elasticProductCategory.setDetails(productCategory.getDetails());
                    elasticProductCategory.setDate(productCategory.getDate());
                    elasticProductCategory.setStatus(productCategory.getStatus());
                    elasticProductCategoryRepository.save(elasticProductCategory);
                } catch (Exception e) {
                    map.put(Check.status,false);
                    if(e.toString().contains("constraint")){
                        String error = "This product category name has already been registered!";
                        Util.logger(error, ProductCategory.class);
                        map.put(Check.message,error);
                    }
                }
            }
        }else{
            map.put(Check.status,false);
            map.put(Check.errors, Util.errors(bindingResult));
        }
        return map;
    }

    //List of Product Category with pagination
    @ResponseBody
    @GetMapping("/categoryList/{stShowNumber}/{stPageNo}")
    public Map<Check,Object> categoryList(@PathVariable String stShowNumber,@PathVariable String stPageNo){
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

    @ResponseBody
    @DeleteMapping("/categoryDelete/{stId}")
    public Map<Check,Object> delete(@PathVariable String stId){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findById(id);
            if(optionalProductCategory.isPresent()){
                ElasticProductCategory elasticProductCategory = elasticProductCategoryRepository.findById(id).get();
                productCategoryRepository.deleteById(id);
                elasticProductCategoryRepository.deleteById(elasticProductCategory.getId());
                map.put(Check.status,true);
                map.put(Check.message,"Data has been deleted!");
                map.put(Check.result,optionalProductCategory.get());
            }else{
                String error = "Product Category not found";
                map.put(Check.status,false);
                map.put(Check.message,error);
                Util.logger(error,ProductCategory.class);
            }
        } catch (Exception e) {
            String error = "An error occurred during the delete operation";
            map.put(Check.status,false);
            map.put(Check.message,error);
            Util.logger(error,ProductCategory.class);
            System.err.println(e);
        }
        return map;
    }

    //Elasticsearch for product category
    @ResponseBody
    @GetMapping("/searchProductCategory/{data}/{stPageNo}/{stShowNumber}")
    public Map<Check,Object> searchProductCategory(@PathVariable String data,@PathVariable String stPageNo,@PathVariable String stShowNumber){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo,showNumber);
            Page<ElasticProductCategory> searchPage = elasticProductCategoryRepository.findBySearchData(data,pageable);
            List<ElasticProductCategory> elasticProductCategoryList = searchPage.getContent();
            int totalData =  elasticProductCategoryList.size(); //for total data in table
            if(totalData > 0 ){
                map.put(Check.status,true);
                map.put(Check.totalPage,searchPage.getTotalPages());
                map.put(Check.message,"Search operation success!");
                map.put(Check.result,elasticProductCategoryList);
            }else{
                map.put(Check.status,false);
                map.put(Check.message,"Could not find a result for your search!");
            }
        } catch (NumberFormatException e) {
            map.put(Check.status,false);
            String error = "An error occurred during the search operation!";
            System.err.println(e);
            Util.logger(error, ProductCategory.class);
            map.put(Check.message,error);
        }
        return map;
    }

    //Product Category insert all data to elasticsearch database
    @GetMapping("/elasticInsertData/productCategory")
    public Map<Check,Object> elasticInsertDataPC(){
        Map<Check,Object> map = new LinkedHashMap<>();
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        try {
            if(productCategoryList.size() > 0){
                productCategoryList.forEach(item -> {
                    //ElasticSearch Save
                    ElasticProductCategory elasticProductCategory = new ElasticProductCategory();
                    elasticProductCategory.setCategoryId(item.getId());
                    elasticProductCategory.setNo(item.getNo());
                    elasticProductCategory.setName(item.getName());
                    elasticProductCategory.setDetails(item.getDetails());
                    elasticProductCategory.setDate(item.getDate());
                    elasticProductCategory.setStatus(item.getStatus());
                    elasticProductCategoryRepository.save(elasticProductCategory);
                });
                map.put(Check.status,true);
                map.put(Check.message,"Elasticsearch veri ekleme işlemi başarılı!");
                //map.put(Check.result,elasticContentsRepository.findAll());
            }else {
                String error = "Sisteme kayıtlı içerik bulunmamaktadır!";
                map.put(Check.status,false);
                map.put(Check.message,error);
                Util.logger(error,ProductCategory.class);
            }
        } catch (Exception e) {
            String error = "Elasticsearch veri tabanına ekleme yapılırken bir hata oluştu!" + e;
            map.put(Check.status,false);
            map.put(Check.message,error);
            Util.logger(error,ProductCategory.class);
        }
        return map;
    }

    //Product Category List
    @ResponseBody
    @GetMapping("/categoryList")
    public Map<Check,Object> categoryList(){
        Map<Check,Object> map = new LinkedHashMap<>();
        map.put(Check.status,true);
        map.put(Check.message,"Product category list operations success!");
        map.put(Check.result,productCategoryRepository.findAll());
        return map;
    }

    //====================================== Category Section - End ========================================//

    //====================================== Product Section - Start ========================================//

    //Addition and update of product
    @ResponseBody
    @PostMapping("/add")
    public Map<Check,Object> productAdd(@RequestBody @Valid Product p, BindingResult bindingResult){
        Map<Check,Object> map = new LinkedHashMap<>();
        List<ProductCategory> productCategoryList = new ArrayList<>();
        if(!bindingResult.hasErrors()){
            if(p.getId() != null){
                p.setDate(Util.getDateFormatter());
                String no = productRepository.findById(p.getId()).get().getNo();
                p.setNo(no);
                p.getProductCategories().forEach(item -> {
                    Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findById(item.getId());
                    if(optionalProductCategory.isPresent()){
                        ProductCategory productCategory = optionalProductCategory.get();
                        productCategoryList.add(productCategory);
                    }
                });
                System.out.println("Update Section " + productCategoryList);
                Optional<Product> optionalProduct = productRepository.findById(p.getId());
                if (optionalProduct.isPresent()){
                    try {
                        //ElasticSearch and SQL DB Update -Start
                        ElasticProduct elasticProduct = elasticProductRepository.findById(p.getId()).get();
                        elasticProductRepository.deleteById(elasticProduct.getId());
                        Product product = productRepository.saveAndFlush(p);
                        ElasticProduct elasticProductNew = new ElasticProduct();
                        elasticProductNew.setProductId(product.getId());
                        elasticProductNew.setDate(product.getDate());
                        elasticProductNew.setNo(product.getNo());
                        elasticProductNew.setName(product.getName());
                        elasticProductNew.setDescription(product.getDescription());
                        elasticProductNew.setPrice(product.getPrice());
                        elasticProductNew.setStatus(product.getStatus());
                        elasticProductRepository.save(elasticProductNew);
                        //ElasticSearch and SQL DB Update - End
                        map.put(Check.status,true);
                        map.put(Check.message,"Updated operations success!");
                        map.put(Check.result,product);
                    } catch (Exception e) {
                        System.err.println("Elasticsearch update" + e);
                    }
                }
            }else{
                try {
                    p.setDate(Util.getDateFormatter());
                    p.getProductCategories().forEach(item -> {
                        Optional<ProductCategory> optionalProductCategory = productCategoryRepository.findById(item.getId());
                        if(optionalProductCategory.isPresent()){
                            ProductCategory productCategory = optionalProductCategory.get();
                            productCategoryList.add(productCategory);
                        }
                    });
                    System.out.println("New Save Section " + productCategoryList);
                    Product product = productRepository.saveAndFlush(p);
                    map.put(Check.status,true);
                    map.put(Check.message,"Adding of Product Operations Successful!");
                    map.put(Check.result,product);
                    ElasticProduct elasticProduct = new ElasticProduct();
                    elasticProduct.setProductId(product.getId());
                    elasticProduct.setDate(product.getDate());
                    elasticProduct.setNo(product.getNo());
                    elasticProduct.setName(product.getName());
                    elasticProduct.setDescription(product.getDescription());
                    elasticProduct.setPrice(product.getPrice());
                    elasticProduct.setStatus(product.getStatus());
                    elasticProductRepository.save(elasticProduct);
                } catch (Exception e) {
                    map.put(Check.status,false);
                    if(e.toString().contains("constraint")){
                        String error = "This product no has already been registered! Please try again.";
                        Util.logger(error, Product.class);
                        map.put(Check.message,error);
                    }
                }
            }
        }else{
            map.put(Check.status,false);
            map.put(Check.errors, Util.errors(bindingResult));
        }
        System.out.println(map);
        return map;
    }

    //List of Product with pagination
    @ResponseBody
    @GetMapping("/list/{stShowNumber}/{stPageNo}")
    public Map<Check,Object> listProduct(@PathVariable String stShowNumber,@PathVariable String stPageNo){
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

    @ResponseBody
    @DeleteMapping("/delete/{stId}")
    public Map<Check,Object> deleteProduct(@PathVariable String stId){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<Product> optionalProduct = productRepository.findById(id);
            if(optionalProduct.isPresent()){
                ElasticProduct elasticProduct = elasticProductRepository.findById(id).get();
                productRepository.deleteById(id);
                elasticProductRepository.deleteById(elasticProduct.getId());
                map.put(Check.status,true);
                map.put(Check.message,"Data has been deleted!");
                map.put(Check.result,optionalProduct.get());
            }else{
                String error = "Product is not found";
                map.put(Check.status,false);
                map.put(Check.message,error);
                Util.logger(error,Product.class);
            }
        } catch (Exception e) {
            String error = "An error occurred during the delete operation";
            map.put(Check.status,false);
            map.put(Check.message,error);
            Util.logger(error,Product.class);
            System.err.println(e);
        }
        return map;
    }

    //Detail of product
    @ResponseBody
    @GetMapping("/detail/{stId}")
    public Map<Check,Object> detailProduct(@PathVariable String stId){
        Map<Check,Object> map = new LinkedHashMap<>();
        //System.out.println(stShowNumber + " " + stPageNo);
        try {
            int id = Integer.parseInt(stId);
            map.put(Check.status,true);
            map.put(Check.message, "Product detail operation is successful!");
            map.put(Check.result,productRepository.findById(id).get());
        } catch (Exception e) {
            map.put(Check.status,false);
            String error = "An error occurred during the operation!";
            System.err.println(e);
            Util.logger(error, Product.class);
            map.put(Check.message,error);
        }
        return map;
    }

    //Change status for product
    @ResponseBody
    @GetMapping("/changeStatus/{stId}/passive")
    public Map<Check,Object> changeStatusPassiveProduct(@PathVariable String stId){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<Product> optionalProduct = productRepository.findById(id);
            if(optionalProduct.isPresent()){
                map.put(Check.status,true);
                map.put(Check.message, "Product change status operation is successful!");
                Product p = optionalProduct.get();
                ElasticProduct elasticProduct = elasticProductRepository.findById(p.getId()).get();
                elasticProductRepository.deleteById(elasticProduct.getId());
                p.setStatus("Unavailable");
                Product product = productRepository.saveAndFlush(p);
                ElasticProduct elasticProductNew = new ElasticProduct();
                elasticProductNew.setProductId(product.getId());
                elasticProductNew.setDate(product.getDate());
                elasticProductNew.setNo(product.getNo());
                elasticProductNew.setName(product.getName());
                elasticProductNew.setDescription(product.getDescription());
                elasticProductNew.setPrice(product.getPrice());
                elasticProductNew.setStatus(product.getStatus());
                elasticProductRepository.save(elasticProductNew);
                //ElasticSearch and SQL DB Update - End
            }else {
                map.put(Check.status,false);
                map.put(Check.message, "Product is not found!");
            }
        } catch (Exception e) {
            map.put(Check.status,false);
            String error = "An error occurred during the change status operation!";
            System.err.println(e);
            Util.logger(error, Product.class);
            map.put(Check.message,error);
        }
        return map;
    }

    //Change status for product
    @ResponseBody
    @GetMapping("/changeStatus/{stId}/active")
    public Map<Check,Object> changeStatusActiveProduct(@PathVariable String stId){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<Product> optionalProduct = productRepository.findById(id);
            if(optionalProduct.isPresent()){
                map.put(Check.status,true);
                map.put(Check.message, "Product change status operation is successful!");
                Product p = optionalProduct.get();
                ElasticProduct elasticProduct = elasticProductRepository.findById(p.getId()).get();
                elasticProductRepository.deleteById(elasticProduct.getId());
                p.setStatus("Available");
                Product product = productRepository.saveAndFlush(p);
                ElasticProduct elasticProductNew = new ElasticProduct();
                elasticProductNew.setProductId(product.getId());
                elasticProductNew.setDate(product.getDate());
                elasticProductNew.setNo(product.getNo());
                elasticProductNew.setName(product.getName());
                elasticProductNew.setDescription(product.getDescription());
                elasticProductNew.setPrice(product.getPrice());
                elasticProductNew.setStatus(product.getStatus());
                elasticProductRepository.save(elasticProductNew);
                //ElasticSearch and SQL DB Update - End
            }else {
                map.put(Check.status,false);
                map.put(Check.message, "Product is not found!");
            }
        } catch (Exception e) {
            map.put(Check.status,false);
            String error = "An error occurred during the change status operation!";
            System.err.println(e);
            Util.logger(error, Product.class);
            map.put(Check.message,error);
        }
        return map;
    }

    //Elasticsearch for product
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

    //Product insert all data to elasticsearch database
    @GetMapping("/elasticInsertData")
    public Map<Check,Object> elasticInsertData(){
        Map<Check,Object> map = new LinkedHashMap<>();
        List<Product> productList = productRepository.findAll();
        try {
            if(productList.size() > 0){
                productList.forEach(item -> {
                    //ElasticSearch Save
                    ElasticProduct elasticProduct = new ElasticProduct();
                    elasticProduct.setProductId(item.getId());
                    elasticProduct.setDate(item.getDate());
                    elasticProduct.setNo(item.getNo());
                    elasticProduct.setName(item.getName());
                    elasticProduct.setDescription(item.getDescription());
                    elasticProduct.setPrice(item.getPrice());
                    elasticProduct.setStatus(item.getStatus());
                    elasticProductRepository.save(elasticProduct);
                });
                map.put(Check.status,true);
                map.put(Check.message,"Elasticsearch veri ekleme işlemi başarılı!");
                //map.put(Check.result,elasticContentsRepository.findAll());
            }else {
                String error = "Sisteme kayıtlı içerik bulunmamaktadır!";
                map.put(Check.status,false);
                map.put(Check.message,error);
                Util.logger(error,Product.class);
            }
        } catch (Exception e) {
            String error = "Elasticsearch veri tabanına ekleme yapılırken bir hata oluştu!" + e;
            map.put(Check.status,false);
            map.put(Check.message,error);
            Util.logger(error,Product.class);
        }
        return map;
    }

    //====================================== Product Section - End ==========================================//

}
