package companyAll_MVC.controllers;

import companyAll_MVC.documents.ElasticNews;
import companyAll_MVC.entities.News;
import companyAll_MVC.entities.NewsCategory;
import companyAll_MVC.repositories._elastic.ElasticNewsRepository;
import companyAll_MVC.repositories._jpa.NewsCategoryRepository;
import companyAll_MVC.repositories._jpa.NewsRepository;
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


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/newsList")
public class NewsListController {

    final NewsCategoryRepository newsCategoryRepository;
    final NewsRepository newsRepository;
    final ElasticNewsRepository elasticNewsRepository;

    public NewsListController(NewsCategoryRepository newsCategoryRepository, NewsRepository newsRepository,ElasticNewsRepository elasticNewsRepository) {
        this.newsCategoryRepository = newsCategoryRepository;
        this.newsRepository = newsRepository;
        this.elasticNewsRepository = elasticNewsRepository;
    }

    @GetMapping("")
    public String newsList() {
        return "newsList";
    }

    //------------------------------------ News Category List - Start ------------------------------------//
    @ResponseBody
    @GetMapping("/categories")
    public Map<Check, Object> categories() {
        Map<Check, Object> map = new LinkedHashMap<>();
        try {
            map.put(Check.status, true);
            map.put(Check.message, "News category list operation succesful!");
            map.put(Check.result, newsCategoryRepository.findAll());
        } catch (Exception e) {
            String error = "An error occurred during the operation!";
            Util.logger(error, NewsCategory.class);
            map.put(Check.status, false);
            map.put(Check.message, error);
            System.err.println(e);
        }
        return map;
    }
    //------------------------------------ News Category List - End --------------------------------------//

    //------------------------------------ News List by Category Id - Start ------------------------------------//
    @ResponseBody
    @GetMapping("/listByCategoryId/{stId}/{stShowNumber}/{stPageNo}")
    public Map<Check, Object> newsListByCategoryId(@PathVariable String stId, @PathVariable String stShowNumber, @PathVariable String stPageNo) {
        Map<Check, Object> map = new LinkedHashMap<>();
        try {
            int categoryId = Integer.parseInt(stId);
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo, showNumber);
            Page<News> newsPage = newsRepository.findByNewsCategory_Id(categoryId, pageable);

            map.put(Check.status, true);
            map.put(Check.message, "News list operation succesful!");
            map.put(Check.totalPage, newsPage.getTotalPages());
            map.put(Check.result, newsPage.getContent());
        } catch (Exception e) {
            String error = "An error occurred during the operation!";
            Util.logger(error, News.class);
            map.put(Check.status, false);
            map.put(Check.message, error);
            System.err.println(e);
        }
        return map;
    }
    //------------------------------------ News List by Category Id List - End --------------------------------------//

    //------------------------------------ News List from category Id with Elasticsearch - Start ----------------------------------------//
    @ResponseBody
    @GetMapping("/listByCategoryIdElasticsearch/{stId}/{stShowNumber}/{stPageNo}")
    public Map<Check,Object> newsListByCategoryIdElasticsearch(@PathVariable String stId,@PathVariable String stShowNumber,@PathVariable String stPageNo){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int categoryId = Integer.parseInt(stId);
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo,showNumber);
            Page<ElasticNews> elasticNewsPage = elasticNewsRepository.findByNewsCategoryIdEquals(categoryId,pageable);
            System.out.println("elasticNewsPage: " + elasticNewsPage);
            map.put(Check.status,true);
            map.put(Check.message,"News list operation succesful!");
            map.put(Check.totalPage,elasticNewsPage.getTotalPages());
            map.put(Check.result,elasticNewsPage.getContent());
        } catch (Exception e) {
            String error = "An error occurred during the operation!";
            Util.logger(error, News.class);
            map.put(Check.status,false);
            map.put(Check.message,error);
            System.err.println(e);
        }
        return map;
    }
    //------------------------------------ News List from category Id with Elasticsearch - End ------------------------------------------//

    //------------------------------------ Elasticsearch for News - Start ------------------------------------//
    @ResponseBody
    @GetMapping("/search/{data}/{stPageNo}/{stShowNumber}")
    public Map<Check,Object> searchNews(@PathVariable String data,@PathVariable String stPageNo,@PathVariable String stShowNumber){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo,showNumber);
            Page<ElasticNews> searchPage = elasticNewsRepository.findBySearchData(data,pageable);
            List<ElasticNews> elasticNewsList = searchPage.getContent();
            int totalData =  elasticNewsList.size(); //for total data in table
            if(totalData > 0 ){
                map.put(Check.status,true);
                map.put(Check.totalPage,searchPage.getTotalPages());
                map.put(Check.message,"Search operation success!");
                map.put(Check.result,elasticNewsList);
            }else{
                map.put(Check.status,false);
                map.put(Check.message,"Could not find a result for your search!");
            }
        } catch (NumberFormatException e) {
            map.put(Check.status,false);
            String error = "An error occurred during the search operation!";
            System.err.println(e);
            Util.logger(error, News.class);
            map.put(Check.message,error);
        }
        return map;
    }
    //------------------------------------ Elasticsearch for News - End --------------------------------------//

}
