package companyAll_MVC.controllers;

import companyAll_MVC.entities.Product;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/productDetail")
public class ProductDetailsController {

    final ProductRepository productRepository;
    final ProductCategoryRepository productCategoryRepository;
    final ElasticProductCategoryRepository elasticProductCategoryRepository;
    final ElasticProductRepository elasticProductRepository;

    public ProductDetailsController(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository, ElasticProductCategoryRepository elasticProductCategoryRepository, ElasticProductRepository elasticProductRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.elasticProductCategoryRepository = elasticProductCategoryRepository;
        this.elasticProductRepository = elasticProductRepository;
    }

    @GetMapping("/{stId}")
    public String productDetails(@PathVariable String stId, Model model){

        try {
            int id = Integer.parseInt(stId);
            Product product = productRepository.findById(id).get();
            DecimalFormat df = new DecimalFormat("###,###.##"); // or pattern "###,###.##$"
            model.addAttribute("detail",product);
            model.addAttribute("priceFormat",df.format(product.getPrice()));
            //System.out.println(product);
        } catch (Exception e) {
            System.err.println(e);
            return "redirect:/productList";
        }
        return "productDetails";
    }

    @ResponseBody
    @GetMapping("/mostPopularProducts")
    public Map<Check,Object> mostPopular(){
        Map<Check,Object> map = new LinkedHashMap<>();
        map.put(Check.status,true);
        map.put(Check.message,"Most popular 5 products listing operations success!");
        map.put(Check.result,productRepository.mostPopularFiveProducts());
        return map;
    }

}
