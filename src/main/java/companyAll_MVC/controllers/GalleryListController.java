package companyAll_MVC.controllers;

import companyAll_MVC.documents.ElasticGallery;
import companyAll_MVC.documents.ElasticNews;
import companyAll_MVC.entities.Gallery;
import companyAll_MVC.entities.News;
import companyAll_MVC.entities.NewsCategory;
import companyAll_MVC.repositories._elastic.ElasticGalleryRepository;
import companyAll_MVC.repositories._jpa.GalleryCategoryRepository;
import companyAll_MVC.repositories._jpa.GalleryRepository;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/galleryList")
public class GalleryListController {

    final GalleryCategoryRepository galleryCategoryRepository;
    final GalleryRepository galleryRepository;
    final ElasticGalleryRepository elasticGalleryRepository;

    public GalleryListController(GalleryCategoryRepository galleryCategoryRepository, GalleryRepository galleryRepository, ElasticGalleryRepository elasticGalleryRepository) {
        this.galleryCategoryRepository = galleryCategoryRepository;
        this.galleryRepository = galleryRepository;
        this.elasticGalleryRepository = elasticGalleryRepository;
    }

    @GetMapping("")
    public String galleryList() {
        return "galleryList";
    }

    //------------------------------------ Gallery Category List - Start ------------------------------------//
    @ResponseBody
    @GetMapping("/categories")
    public Map<Check, Object> categories() {
        Map<Check, Object> map = new LinkedHashMap<>();
        try {
            map.put(Check.status, true);
            map.put(Check.message, "Gallery category list operation succesful!");
            map.put(Check.result, galleryCategoryRepository.findAll());
        } catch (Exception e) {
            String error = "An error occurred during the operation!";
            Util.logger(error, NewsCategory.class);
            map.put(Check.status, false);
            map.put(Check.message, error);
            System.err.println(e);
        }
        return map;
    }
    //------------------------------------ Gallery Category List - End --------------------------------------//

    //------------------------------------ Gallery List by Category Id - Start ------------------------------------//
    @ResponseBody
    @GetMapping("/listByCategoryId/{stId}/{stShowNumber}/{stPageNo}")
    public Map<Check, Object> galleryListByCategoryId(@PathVariable String stId, @PathVariable String stShowNumber, @PathVariable String stPageNo) {
        Map<Check, Object> map = new LinkedHashMap<>();
        try {
            int categoryId = Integer.parseInt(stId);
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo, showNumber);
            Page<Gallery> galleryPage = galleryRepository.findByGalleryCategory_Id(categoryId, pageable);

            map.put(Check.status, true);
            map.put(Check.message, "Gallery list operation succesful!");
            map.put(Check.totalPage, galleryPage.getTotalPages());
            map.put(Check.result, galleryPage.getContent());
        } catch (Exception e) {
            String error = "An error occurred during the operation!";
            Util.logger(error, Gallery.class);
            map.put(Check.status, false);
            map.put(Check.message, error);
            System.err.println(e);
        }
        return map;
    }
    //------------------------------------ Gallery List by Category Id List - End --------------------------------------//

    //------------------------------------ Gallery List from category Id with Elasticsearch - Start ----------------------------------------//
    @ResponseBody
    @GetMapping("/listByCategoryIdElasticsearch/{stId}/{stShowNumber}/{stPageNo}")
    public Map<Check,Object> galleryListByCategoryIdElasticsearch(@PathVariable String stId,@PathVariable String stShowNumber,@PathVariable String stPageNo){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int categoryId = Integer.parseInt(stId);
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo,showNumber);
            Page<ElasticGallery> elasticGalleryPage = elasticGalleryRepository.findByGalleryCategoryIdEquals(categoryId,pageable);
            System.out.println("elasticGalleryPage: " + elasticGalleryPage);
            map.put(Check.status,true);
            map.put(Check.message,"News list operation succesful!");
            map.put(Check.totalPage,elasticGalleryPage.getTotalPages());
            map.put(Check.result,elasticGalleryPage.getContent());
        } catch (Exception e) {
            String error = "An error occurred during the operation!";
            Util.logger(error, News.class);
            map.put(Check.status,false);
            map.put(Check.message,error);
            System.err.println(e);
        }
        return map;
    }
    //------------------------------------ Gallery List from category Id with Elasticsearch - End ------------------------------------------//

    //------------------------------------ Elasticsearch for Gallery - Start ------------------------------------//
    @ResponseBody
    @GetMapping("/search/{data}/{stPageNo}/{stShowNumber}")
    public Map<Check,Object> searchGallery(@PathVariable String data,@PathVariable String stPageNo,@PathVariable String stShowNumber){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo,showNumber);
            Page<ElasticGallery> searchPage = elasticGalleryRepository.findBySearchData(data,pageable);
            List<ElasticGallery> elasticGalleryList = searchPage.getContent();
            int totalData =  elasticGalleryList.size(); //for total data in table
            if(totalData > 0 ){
                map.put(Check.status,true);
                map.put(Check.totalPage,searchPage.getTotalPages());
                map.put(Check.message,"Search operation success!");
                map.put(Check.result,elasticGalleryList);
            }else{
                map.put(Check.status,false);
                map.put(Check.message,"Could not find a result for your search!");
            }
        } catch (NumberFormatException e) {
            map.put(Check.status,false);
            String error = "An error occurred during the search operation!";
            System.err.println(e);
            Util.logger(error, Gallery.class);
            map.put(Check.message,error);
        }
        return map;
    }
    //------------------------------------ Elasticsearch for Gallery - End --------------------------------------//


}
