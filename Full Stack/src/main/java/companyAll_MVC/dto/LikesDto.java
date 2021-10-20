package companyAll_MVC.dto;

import companyAll_MVC.documents.ElasticProduct;
import companyAll_MVC.entities.*;
import companyAll_MVC.repositories._elastic.ElasticProductRepository;
import companyAll_MVC.repositories._jpa.IndentRepository;
import companyAll_MVC.repositories._jpa.LikesProductRepository;
import companyAll_MVC.repositories._jpa.LikesRepository;
import companyAll_MVC.repositories._jpa.ProductRepository;
import companyAll_MVC.utils.Check;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class LikesDto {

    final LikesRepository likesRepository;
    final IndentRepository indentRepository;
    final LikesProductRepository likesProductRepository;
    final ProductRepository productRepository;

    final ElasticProductRepository elasticProductRepository;

    public LikesDto(LikesRepository likesRepository, IndentRepository indentRepository, LikesProductRepository likesProductRepository, ProductRepository productRepository, ElasticProductRepository elasticProductRepository) {
        this.likesRepository = likesRepository;
        this.indentRepository = indentRepository;
        this.likesProductRepository = likesProductRepository;
        this.productRepository = productRepository;
        this.elasticProductRepository = elasticProductRepository;
    }
    public Map<Check,Object> list(Integer id) {
        Map<Check,Object> hm = new LinkedHashMap<>();
        hm.put(Check.status,true);
        List<Likes> ls = likesRepository.findByIndent_IdEquals(id);
        hm.put(Check.result,ls);
        return hm;

    }

    public Map<Check,Object> listCustomerById(Integer id) {
        Map<Check,Object> hm1 = new LinkedHashMap<>();
        hm1.put(Check.status,true);
//        Indent indent = new Indent();
//        Indent indent1 = indentRepository.findByCustomer_IdEquals(id);
//        List<Indent> ls1= indentRepository.findByCustomer_IdEquals(id);
//        List<Likes> ls = new ArrayList<>();

          List<Likes> ls = likesRepository.findByIndent_Customer_IdEquals(id);

          hm1.put(Check.result,ls);
        return hm1;

    }

    // Like Add and Rating Point Actions
    public Map<Check, Object> add(Likes likes) {
        Map<Check, Object> hm = new LinkedHashMap<>();
        try {

            hm.put(Check.status, true);
            hm.put(Check.message, "add likes successful");
            Likes likes1 = likesRepository.save(likes);
            hm.put(Check.result, likes1);
            Indent indent = indentRepository.findById(likes.getIndent().getId()).get();
            Product product = productRepository.findById(indent.getProduct().getId()).get();

            LikesProduct likesProduct = new LikesProduct();
            likesProduct.setProduct(indent.getProduct());
            if (likesProduct.getTotalLike()==null){
                likesProduct.setTotalLike(likes.getRating());
            }else {
                likesProduct.setTotalLike(likesProduct.getTotalLike()+likes.getRating());
            }
            likesProductRepository.save(likesProduct);
            List<LikesProduct> listAll = likesProductRepository.findByProduct_IdEquals(indent.getProduct().getId());
            double total = 0; double avaregaRating=0; double count=0;
            for(LikesProduct item:listAll){
                count++;
                total =total + item.getTotalLike();
                avaregaRating = total/count;
            }
            System.out.println("total: " + total + "count: " + count + "avarageRating: " + avaregaRating );
            System.out.println("productObject: " + product);
            product.setTotalLike(avaregaRating);
            productRepository.saveAndFlush(product);
            //Elasticsearch total likes save - Start
            ElasticProduct elasticProduct = elasticProductRepository.findById(product.getId()).get();
            elasticProduct.setTotalLike(product.getTotalLike());
            elasticProductRepository.save(elasticProduct);
            //Elasticsearch total likes save - End
        } catch (Exception exception) {
            hm.put(Check.status, false);
            hm.put(Check.message, "Failed to add likes");
            System.err.println(exception);
        } return hm;
    }







}
