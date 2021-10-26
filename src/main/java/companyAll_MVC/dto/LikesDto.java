package companyAll_MVC.dto;

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
    final ElasticLikesRepository elasticLikesRepository;
    final ElasticProductRepository elasticProductRepository;
    final ElasticLikeProductRepository elasticLikeProductRepository;

    public LikesDto(LikesRepository likesRepository, IndentRepository indentRepository, LikesProductRepository likesProductRepository, ProductRepository productRepository, ElasticLikesRepository elasticLikesRepository, ElasticProductRepository elasticProductRepository, ElasticLikeProductRepository elasticLikeProductRepository) {
        this.likesRepository = likesRepository;
        this.indentRepository = indentRepository;
        this.likesProductRepository = likesProductRepository;
        this.productRepository = productRepository;
        this.elasticLikesRepository = elasticLikesRepository;
        this.elasticProductRepository = elasticProductRepository;
        this.elasticLikeProductRepository = elasticLikeProductRepository;
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

            Indent indent = indentRepository.findById(likes.getIndent().getId()).get();
            Product product = productRepository.findById(indent.getProduct().getId()).get();
            if (!indent.isOrderStatus()){
                // saving data in sql and elasticsearch
                Likes likes1 = likesRepository.save(likes);
                ElasticLikes elasticLikes = new ElasticLikes();
                elasticLikes.setId(Integer.toString(likes1.getId()));
                elasticLikes.setLikesId(likes1.getId());
                elasticLikes.setCustomerName(indent.getCustomer().getName());
                elasticLikes.setProductName(indent.getProduct().getName());
                elasticLikes.setProductRating(likes1.getRating());
                elasticLikes.setProductDetail(indent.getProduct().getDetails());
                elasticLikes.setProductNo(indent.getProduct().getNo());
                System.out.println("elasticLikes: " + elasticLikes);



                elasticLikesRepository.save(elasticLikes);

                hm.put(Check.status, true);
                hm.put(Check.message, "add likes successful");
                hm.put(Check.result, likes1);


                LikesProduct likesProduct = new LikesProduct();
                likesProduct.setProduct(indent.getProduct());
                if (likesProduct.getTotalLike() == null) {
                    likesProduct.setTotalLike(likes.getRating());
                } else {
                    likesProduct.setTotalLike(likesProduct.getTotalLike() + likes.getRating());
                }
                likesProductRepository.save(likesProduct);
                List<LikesProduct> listAll = likesProductRepository.findByProduct_IdEquals(indent.getProduct().getId());
                double total = 0;
                double avaregaRating = 0;
                double count = 0;
                for (LikesProduct item : listAll) {
                    count++;
                    total = total + item.getTotalLike();
                    avaregaRating = total / count;
                }
                System.out.println("total: " + total + "count: " + count + "avarageRating: " + avaregaRating);
                System.out.println("productObject: " + product);
                product.setTotalLike(avaregaRating);
                ElasticLikesProduct elasticLikesProduct= new ElasticLikesProduct();
                elasticLikesProduct.setId(Integer.toString(product.getId()));
                elasticLikesProduct.setName(product.getName());
                elasticLikesProduct.setTotalRating(Double.toString(product.getTotalLike()));
                elasticLikeProductRepository.save(elasticLikesProduct);

                productRepository.saveAndFlush(product);
                indent.setOrderStatus(true);
                indentRepository.saveAndFlush(indent);
            }else {
                hm.put(Check.status,false);
                hm.put(Check.message,"customer has already voted");
            }

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


    //List of Likes With Customer Pagination
    public Map<Check, Object> listLikesWithPaginationCustomerId(String stShowNumber,String stPageNo){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int pageNo = Integer.parseInt(stPageNo);
            int showNumber = Integer.parseInt(stShowNumber);
            Pageable pageable = PageRequest.of(pageNo,showNumber);
            Page<ElasticLikes> likesPage = elasticLikesRepository.findByOrderByIdAsc(pageable);
            map.put(Check.status,true);
            map.put(Check.totalPage,likesPage.getTotalPages());
            map.put(Check.message, "Likes listing on page " + (pageNo + 1) + " is successful");
            map.put(Check.result,likesPage.getContent());
        } catch (Exception e) {
            map.put(Check.status,false);
            String error = "An error occurred during the operation!";
            System.err.println(e);
            Util.logger(error, Likes.class);
            map.put(Check.message,error);
        }
        return map;
    }




}
