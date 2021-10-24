package companyAll_MVC.dto;

import companyAll_MVC.documents.ElasticIndent;
import companyAll_MVC.documents.ElasticProduct;
import companyAll_MVC.entities.Announcement;
import companyAll_MVC.repositories._elastic.*;
import companyAll_MVC.repositories._jpa.AnnouncementRepository;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Statics;
import companyAll_MVC.utils.Util;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardDto {

    final ElasticLikesRepository elasticLikesRepository;
    final ElasticCustomerRepository elasticCustomerRepository;
    final ElasticIndentRepository elasticIndentRepository;
    final ElasticContentsRepository elasticContentsRepository;
    final ElasticProductRepository elasticProductRepository;
    final ElasticAnnouncementRepository elasticAnnouncementRepository;
    final ElasticProductCategoryRepository elasticProductCategoryRepository;
    final AnnouncementRepository announcementRepository;
    public DashboardDto(ElasticLikesRepository elasticLikesRepository, ElasticCustomerRepository elasticCustomerRepository, ElasticIndentRepository elasticIndentRepository, ElasticContentsRepository elasticContentsRepository, ElasticProductRepository elasticProductRepository, ElasticAnnouncementRepository elasticAnnouncementRepository, ElasticProductCategoryRepository elasticProductCategoryRepository, AnnouncementRepository announcementRepository) {
        this.elasticLikesRepository = elasticLikesRepository;
        this.elasticCustomerRepository = elasticCustomerRepository;
        this.elasticIndentRepository = elasticIndentRepository;
        this.elasticContentsRepository = elasticContentsRepository;
        this.elasticProductRepository = elasticProductRepository;
        this.elasticAnnouncementRepository = elasticAnnouncementRepository;
        this.elasticProductCategoryRepository = elasticProductCategoryRepository;
        this.announcementRepository = announcementRepository;
    }

    //General Statics Information
    public Map<Statics,Object> generalStatics(){
        Map<Statics,Object> map = new LinkedHashMap<>();
        map.put(Statics.status,true);
        map.put(Statics.message,"General statics information listing operation success!");
        map.put(Statics.totalLikes,elasticLikesRepository.allLikes().size());
        map.put(Statics.totalCustomers,elasticCustomerRepository.allCustomers().size());
        map.put(Statics.totalOrders,elasticIndentRepository.allOrders().size());
        map.put(Statics.totalContents,elasticContentsRepository.allContents().size());
        return map;
    }

    //Last Added 6 Product
    public Map<Statics,Object> lastAddSixProduct(){
        Map<Statics,Object> map = new LinkedHashMap<>();
        Pageable pageable = PageRequest.of(0,8);
        Page<ElasticProduct> elasticProductPage = elasticProductRepository.findByOrderByProductIdDesc(pageable);
        map.put(Statics.status,true);
        map.put(Statics.message,"Last added 6 product information listing operation success!");
        map.put(Statics.lastAddedSixProduct,elasticProductPage.getContent());
        return map;
    }

    //Last Six Order
    public Map<Statics,Object> lastSixOrder(){
        Map<Statics,Object> map = new LinkedHashMap<>();
        Pageable pageable = PageRequest.of(0,8);
        Page<ElasticIndent> elasticIndentPage = elasticIndentRepository.findAllByOrderByIidDesc(pageable);
        map.put(Statics.status,true);
        map.put(Statics.message,"Last 6 order information listing operation success!");
        map.put(Statics.lastSixOrder,elasticIndentPage.getContent());
        return map;
    }

    //Total product by Category Id
    public Map<Check,Object> totalProductByCategoryId(String stId){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            List<ElasticProduct> elasticProductList = elasticProductRepository.findByProductCategoryIdEquals(id);
            map.put(Check.status,true);
            map.put(Check.message,"Total Product by category Id listing operations success!");
            map.put(Check.result,elasticProductList.size());
        } catch (NumberFormatException e) {
            map.put(Check.status, false);
            map.put(Check.message, "An error occurred in total product by category Id listing operation!");
        }
        return map;
    }

    //Daily Announcment
    public Map<Check,Object> dailyAnnouncment(String stPageNo){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int pageNo = Integer.parseInt(stPageNo);
            String[] date = Util.getDateFormatter().split(" ");
            Pageable pageable = PageRequest.of(pageNo,1);
            Page<Announcement> announcementPage = announcementRepository.findByDateContainsIgnoreCaseOrderByIdAsc(date[0],pageable);
            //System.out.println(announcementPage.getContent());
            map.put(Check.status,true);
            map.put(Check.message,"Daily Announcment Listing Operation Success!");
            map.put(Check.totalPage,announcementPage.getTotalPages());
            map.put(Check.result,announcementPage.getContent());
        } catch (Exception e) {
            String error = "An error occurred in Daily Announcment Listing listing operation!";
            map.put(Check.status, false);
            map.put(Check.message,error);
            System.err.println(e);
            Util.logger(error, Announcement.class);
        }
        return map;
    }


}
