package companyAll_MVC.controllers;

import companyAll_MVC.documents.ElasticLikes;
import companyAll_MVC.documents.ElasticLikesProduct;
import companyAll_MVC.documents.ElasticProduct;
import companyAll_MVC.entities.*;
import companyAll_MVC.repositories._elastic.ElasticLikeProductRepository;
import companyAll_MVC.repositories._elastic.ElasticLikesRepository;
import companyAll_MVC.repositories._elastic.ElasticProductRepository;
import companyAll_MVC.repositories._jpa.IndentRepository;
import companyAll_MVC.repositories._jpa.LikesProductRepository;
import companyAll_MVC.repositories._jpa.LikesRepository;
import companyAll_MVC.repositories._jpa.ProductRepository;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Util;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/likes")
public class LikesController {

    final LikesRepository likesRepository;
    final LikesProductRepository likesProductRepository;
    final IndentRepository indentRepository;
    final ProductRepository productRepository;
    final ElasticLikesRepository elasticLikesRepository;
    final ElasticProductRepository elasticProductRepository;
    final ElasticLikeProductRepository elasticLikeProductRepository;


    public LikesController(LikesRepository likesRepository, LikesProductRepository likesProductRepository, IndentRepository indentRepository, ProductRepository productRepository, ElasticLikesRepository elasticLikesRepository, ElasticProductRepository elasticProductRepository, ElasticLikeProductRepository elasticLikeProductRepository) {
        this.likesRepository = likesRepository;
        this.likesProductRepository = likesProductRepository;
        this.indentRepository = indentRepository;
        this.productRepository = productRepository;
        this.elasticLikesRepository = elasticLikesRepository;
        this.elasticProductRepository = elasticProductRepository;
        this.elasticLikeProductRepository = elasticLikeProductRepository;
    }

    @GetMapping("")
    public String likes() {
        return "likes";
    }

    // List of Likes Table
    @GetMapping("/list")
    @ResponseBody
    public List<Likes> listAll() {
        List<Likes> listAll = likesRepository.findAll();
        return listAll;
    }


    // ====================== Likes With Customer - START ======================
    // List With Pagination
    @ResponseBody
    @GetMapping("/likesListByCustomer/{stShowNumber}/{stPageNo}")
    public Map<Check, Object> likesListByCustomer(@PathVariable String stShowNumber, @PathVariable String stPageNo) {
        Map<Check, Object> map = new LinkedHashMap<>();
        try {
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo, showNumber);
            Page<ElasticLikes> likesCustomerPage = elasticLikesRepository.findByOrderByIdAsc(pageable);
            map.put(Check.status, true);
            map.put(Check.totalPage, likesCustomerPage.getTotalPages());
            map.put(Check.message, "LikesCustomer listing on page " + (pageNo + 1) + " is successful");
            map.put(Check.result, likesCustomerPage.getContent());
        } catch (Exception e) {
            map.put(Check.status, false);
            String error = "An error occurred during the operation!";
            System.err.println(e);
            Util.logger(error, Likes.class);
            map.put(Check.message, error);
        }
        return map;
    }

    // Delete LikesCustomer
    @ResponseBody
    @DeleteMapping("/likesCustomer/{stId}")
    public Map<Check, Object> delete(@PathVariable String stId) {
        Map<Check, Object> map = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<Likes> optionalLikesCustomer = likesRepository.findById(id);
            if (optionalLikesCustomer.isPresent()) {
                ElasticLikes elasticLikes = elasticLikesRepository.findById(stId).get();
                likesRepository.deleteById(id);
                elasticLikesRepository.deleteById(elasticLikes.getId());
                map.put(Check.status, true);
                map.put(Check.message, "Data has been deleted!");
                map.put(Check.result, optionalLikesCustomer.get());
            } else {
                String error = "CustomerLike not found";
                map.put(Check.status, false);
                map.put(Check.message, error);
                Util.logger(error, Likes.class);
            }
        } catch (Exception e) {
            String error = "An error occurred during the delete operation";
            map.put(Check.status, false);
            map.put(Check.message, error);
            Util.logger(error, Likes.class);
            System.err.println("likesController DeleteMapping Error: " + e);
        }
        return map;
    }

    // ElasticSearch LikesCustomer
    @ResponseBody
    @GetMapping("/searchLikesCustomer/{data}/{stPageNo}/{stShowNumber}")
    public Map<Check, Object> searchLikesCustomerCategory(@PathVariable String data, @PathVariable String stPageNo, @PathVariable String stShowNumber) {
        Map<Check, Object> map = new LinkedHashMap<>();
        try {
            System.out.println("burada :");
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo, showNumber);
            System.out.println("burada pageable:" + pageable);
            System.out.println("burada data:" + data);
            Page<ElasticLikes> searchPage = elasticLikesRepository.findBySearchData(data, pageable);
            System.out.println("burada searchPage:" + searchPage);

            List<ElasticLikes> elasticLikesCustomerList = searchPage.getContent();
            System.out.println("burada elasticLikesCustomerList:" + elasticLikesCustomerList);

            int totalData = elasticLikesCustomerList.size(); //for total data in table
            System.out.println("burada totalData:" + totalData);
            if (totalData > 0) {
                map.put(Check.status, true);
                map.put(Check.totalPage, searchPage.getTotalPages());
                map.put(Check.message, "Search operation success!");
                map.put(Check.result, elasticLikesCustomerList);
            } else {
                map.put(Check.status, false);
                map.put(Check.message, "Could not find a result for your search!");
            }
        } catch (NumberFormatException e) {
            map.put(Check.status, false);
            String error = "An error occurred during the search operation!";
            System.err.println(e);
            Util.logger(error, Likes.class);
            map.put(Check.message, error);
        }
        return map;
    }
    // ====================== Likes With Customer - END ======================

    // ====================== Likes With Product - START ======================

    // List with pagination
    @ResponseBody
    @GetMapping("/likesListByProduct/{stShowNumber}/{stPageNo}")
    public Map<Check, Object> likesListByProduct(@PathVariable String stShowNumber, @PathVariable String stPageNo) {
        Map<Check, Object> map = new LinkedHashMap<>();
        try {
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo, showNumber);
            Page<ElasticLikesProduct> likesProductPage = elasticLikeProductRepository.findByOrderByIdAsc(pageable);
            map.put(Check.status, true);
            map.put(Check.totalPage, likesProductPage.getTotalPages());
            map.put(Check.message, "likesProduct listing on page " + (pageNo + 1) + " is successful");
            map.put(Check.result, likesProductPage.getContent());
        } catch (Exception e) {
            map.put(Check.status, false);
            String error = "An error occurred during the operation!";
            System.err.println(e);
            Util.logger(error, ElasticLikesProduct.class);
            map.put(Check.message, error);
        }
        return map;
    }

    // ElasticSearch LikesProduct
    @ResponseBody
    @GetMapping("/searchLikesProduct/{data}/{stPageNo}/{stShowNumber}")
    public Map<Check, Object> searchLikesProductCategory(@PathVariable String data, @PathVariable String stPageNo, @PathVariable String stShowNumber) {
        Map<Check, Object> map = new LinkedHashMap<>();
        try {
            System.out.println("burada :");
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo, showNumber);
            System.out.println("burada pageable:" + pageable);
            System.out.println("burada data:" + data);
            Page<ElasticLikesProduct> searchPage = elasticLikeProductRepository.findBySearchDataProductLike(data, pageable);
            System.out.println("burada searchPage:" + searchPage);

            List<ElasticLikesProduct> elasticLikesProductList = searchPage.getContent();
            System.out.println("burada elasticLikesProductList:" + elasticLikesProductList);

            int totalData = elasticLikesProductList.size(); //for total data in table
            System.out.println("burada totalData:" + totalData);
            if (totalData > 0) {
                map.put(Check.status, true);
                map.put(Check.totalPage, searchPage.getTotalPages());
                map.put(Check.message, "Search operation success!");
                map.put(Check.result, elasticLikesProductList);
            } else {
                map.put(Check.status, false);
                map.put(Check.message, "Could not find a result for your search!");
            }
        } catch (NumberFormatException e) {
            map.put(Check.status, false);
            String error = "An error occurred during the search operation!";
            System.err.println(e);
            Util.logger(error, ElasticLikesProduct.class);
            map.put(Check.message, error);
        }
        return map;
    }


    // ====================== Likes With Product - END ======================


}
