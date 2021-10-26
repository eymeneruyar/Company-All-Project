package companyAll_MVC.controllers;

import companyAll_MVC.entities.Product;
import companyAll_MVC.repositories._elastic.ElasticProductCategoryRepository;
import companyAll_MVC.repositories._elastic.ElasticProductRepository;
import companyAll_MVC.repositories._jpa.ProductCategoryRepository;
import companyAll_MVC.repositories._jpa.ProductRepository;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Util;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Base64;
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

    @GetMapping("/get_image/id={id}name={name}")
    public void getImage(@PathVariable Integer id, @PathVariable String name, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("image/jpeg; image/jpg; image/png");
        File file = new File(Util.UPLOAD_DIR_PRODUCTS + id + "/" + name);
        if(file.exists()){
            byte[] fileContent = FileUtils.readFileToByteArray(file);
            response.getOutputStream().write(fileContent);
        }else{
            File defaultFile = new File(Util.UPLOAD_DIR_PRODUCTS + "default_image.jpg");
            byte[] defaultFileContent = FileUtils.readFileToByteArray(defaultFile);
            response.getOutputStream().write(defaultFileContent);
        }
        response.getOutputStream().close();
    }

}
