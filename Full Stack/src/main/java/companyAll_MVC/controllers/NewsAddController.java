package companyAll_MVC.controllers;

import companyAll_MVC.documents.ElasticNews;
import companyAll_MVC.documents.ElasticNewsCategory;
import companyAll_MVC.entities.News;
import companyAll_MVC.entities.NewsCategory;
import companyAll_MVC.repositories._elastic.ElasticNewsCategoryRepository;
import companyAll_MVC.repositories._elastic.ElasticNewsRepository;
import companyAll_MVC.repositories._jpa.NewsCategoryRepository;
import companyAll_MVC.repositories._jpa.NewsRepository;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Util;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Controller
@RequestMapping("/news")
public class NewsAddController {

    int chosenId = 0;
    int sendCount = 0;
    int sendSuccessCount = 0;
    String errorMessage = "";

    final NewsRepository newsRepository;
    final NewsCategoryRepository newsCategoryRepository;
    final ElasticNewsRepository elasticNewsRepository;
    final ElasticNewsCategoryRepository elasticNewsCategoryRepository;

    public NewsAddController(NewsRepository newsRepository, NewsCategoryRepository newsCategoryRepository, ElasticNewsRepository elasticNewsRepository, ElasticNewsCategoryRepository elasticNewsCategoryRepository) {
        this.newsRepository = newsRepository;
        this.newsCategoryRepository = newsCategoryRepository;
        this.elasticNewsRepository = elasticNewsRepository;
        this.elasticNewsCategoryRepository = elasticNewsCategoryRepository;
    }

    @GetMapping("")
    public String newsAdd() {
        return "newsAdd";
    }

    // ===========================================Category Operations - START===========================================
    // Add Category
    @PostMapping("/categoryAdd")
    @ResponseBody
    public Map<Check, Object> categoryAdd(@RequestBody @Valid NewsCategory newsCategory, BindingResult bindingResult) {
        Map<Check, Object> map = new LinkedHashMap<>();
        if (!bindingResult.hasErrors()) {
            if (newsCategory.getId() != null) {
                Optional<NewsCategory> optionalNewsCategory = newsCategoryRepository.findById(newsCategory.getId());
                if (optionalNewsCategory.isPresent()) {
                    try {
                        NewsCategory newsCategory1 = newsCategoryRepository.saveAndFlush(newsCategory);
                        ElasticNewsCategory elasticNewsCategory1 = new ElasticNewsCategory();
                        elasticNewsCategory1.setId(Integer.toString(newsCategory1.getId()));
                        elasticNewsCategory1.setCategoryId(newsCategory1.getId());
                        elasticNewsCategory1.setNo(newsCategory1.getNo());
                        elasticNewsCategory1.setName(newsCategory1.getName());
                        elasticNewsCategory1.setDetail(newsCategory1.getDetail());
                        elasticNewsCategory1.setDate(newsCategory1.getDate());
                        elasticNewsCategory1.setStatus(newsCategory1.getStatus());
                        elasticNewsCategoryRepository.save(elasticNewsCategory1);

                        map.put(Check.status, true);
                        map.put(Check.message, "Updated operations success!");
                        map.put(Check.result, newsCategory);
                    } catch (Exception ex) {
                        System.err.println("Elastic news category update" + ex);
                    }
                }
            } else {
                try {
                    NewsCategory newsCategory1 = newsCategoryRepository.saveAndFlush(newsCategory);
                    map.put(Check.status, true);
                    map.put(Check.message, "Adding of News Category Operations Successful!");
                    map.put(Check.result, newsCategory);

                    ElasticNewsCategory elasticNewsCategory2 = new ElasticNewsCategory();
                    elasticNewsCategory2.setId(Integer.toString(newsCategory1.getId()));
                    elasticNewsCategory2.setCategoryId(newsCategory1.getId());
                    elasticNewsCategory2.setNo(newsCategory1.getNo());
                    elasticNewsCategory2.setName(newsCategory1.getName());
                    elasticNewsCategory2.setDetail(newsCategory1.getDetail());
                    elasticNewsCategory2.setDate(newsCategory1.getDate());
                    elasticNewsCategory2.setStatus(newsCategory1.getStatus());
                    elasticNewsCategoryRepository.save(elasticNewsCategory2);
                } catch (Exception ex) {
                    map.put(Check.status, false);
                    if (ex.toString().contains("constraint")) {
                        String error = "This news category name has alredy been registered!";
                        Util.logger(error, NewsCategory.class);
                        map.put(Check.message, error);
                    }
                }
            }
        } else {
            map.put(Check.status, false);
            map.put(Check.errors, Util.errors(bindingResult));
        }
        return map;
    }

    //List of News Category with pagination
    @ResponseBody
    @GetMapping("/categoryList/{stShowNumber}/{stPageNo}")
    public Map<Check, Object> categoryList(@PathVariable String stShowNumber, @PathVariable String stPageNo) {
        Map<Check, Object> map = new LinkedHashMap<>();
        try {
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo, showNumber);
            Page<ElasticNewsCategory> categoryPage = elasticNewsCategoryRepository.findByOrderByIdAsc(pageable);
            map.put(Check.status, true);
            map.put(Check.totalPage, categoryPage.getTotalPages());
            map.put(Check.message, "Content listing on page " + (pageNo + 1) + " is successful");
            map.put(Check.result, categoryPage.getContent());
        } catch (Exception e) {
            map.put(Check.status, false);
            String error = "An error occurred during the operation!";
            System.err.println(e);
            Util.logger(error, NewsCategory.class);
            map.put(Check.message, error);
        }
        return map;
    }

    // Delete Category
    @ResponseBody
    @DeleteMapping("/categoryDelete/{stId}")
    public Map<Check, Object> delete(@PathVariable String stId) {
        Map<Check, Object> map = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<NewsCategory> optionalNewsCategory = newsCategoryRepository.findById(id);
            if (optionalNewsCategory.isPresent()) {
                ElasticNewsCategory elasticNewsCategory = elasticNewsCategoryRepository.findById(id).get();
                newsCategoryRepository.deleteById(id);
                elasticNewsCategoryRepository.deleteById(elasticNewsCategory.getId());
                map.put(Check.status, true);
                map.put(Check.message, "Data has been deleted!");
                map.put(Check.result, optionalNewsCategory.get());
            } else {
                String error = "News Category not found";
                map.put(Check.status, false);
                map.put(Check.message, error);
                Util.logger(error, NewsCategory.class);
            }
        } catch (Exception e) {
            String error = "An error occurred during the delete operation";
            map.put(Check.status, false);
            map.put(Check.message, error);
            Util.logger(error, NewsCategory.class);
            System.err.println(e);
        }
        return map;
    }

    // ElasticSearch Category
    @ResponseBody
    @GetMapping("/searchNewsCategory/{data}/{stPageNo}/{stShowNumber}")
    public Map<Check, Object> searchNewsCategory(@PathVariable String data, @PathVariable String stPageNo, @PathVariable String stShowNumber) {
        Map<Check, Object> map = new LinkedHashMap<>();
        try {
            System.out.println("burada :");
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo, showNumber);
            System.out.println("burada pageable:" + pageable);
            System.out.println("burada data:" + data);
            Page<ElasticNewsCategory> searchPage = elasticNewsCategoryRepository.findBySearchData(data, pageable);
            System.out.println("burada searchPage:" + searchPage);

            List<ElasticNewsCategory> elasticNewsCategoryList = searchPage.getContent();
            System.out.println("burada elasticNewsCategoryList:" + elasticNewsCategoryList);

            int totalData = elasticNewsCategoryList.size(); //for total data in table
            System.out.println("burada totalData:" + totalData);
            if (totalData > 0) {
                map.put(Check.status, true);
                map.put(Check.totalPage, searchPage.getTotalPages());
                map.put(Check.message, "Search operation success!");
                map.put(Check.result, elasticNewsCategoryList);
            } else {
                map.put(Check.status, false);
                map.put(Check.message, "Could not find a result for your search!");
            }
        } catch (NumberFormatException e) {
            map.put(Check.status, false);
            String error = "An error occurred during the search operation!";
            System.err.println(e);
            Util.logger(error, NewsCategory.class);
            map.put(Check.message, error);
        }
        return map;
    }

    // Category List
    @ResponseBody
    @GetMapping("/categoryList")
    public Map<Check, Object> categoryList() {
        Map<Check, Object> map = new LinkedHashMap<>();
        map.put(Check.status, true);
        map.put(Check.message, "News category list operations success!");
        map.put(Check.result, newsCategoryRepository.findAll());
        return map;
    }
    // ===========================================Category Operations - END=============================================


    // ===========================================News Operations - START=================================================

    // News - Add and Update
    @PostMapping("/add")
    @ResponseBody
    public Map<Check, Object> newsAdd(@RequestBody @Valid News news, BindingResult bindingResult) {
        Map<Check, Object> map = new LinkedHashMap<>();
        if (!bindingResult.hasErrors()) {
            if (news.getId() != null) {
                Optional<News> optionalNews = newsRepository.findById(news.getId());
                if (optionalNews.isPresent()) {
                    try {
                        News news1 = newsRepository.saveAndFlush(news);
                        System.out.println("news1: "  + news1);
                        ElasticNews elasticNews = new ElasticNews();
                        elasticNews.setId(Integer.toString(news1.getId()));
                        elasticNews.setNo(Long.toString(news1.getNo()));
                        elasticNews.setTitle(news1.getTitle());
                        elasticNews.setSummary(news1.getSummary());
                        elasticNews.setDescription(news1.getDescription());
                        elasticNews.setStatus(news1.getStatus());
                        elasticNews.setDate(news1.getDate());
                        elasticNews.setCategoryName(news1.getNewsCategory().getName());
                        elasticNewsRepository.save(elasticNews);

                        map.put(Check.status, true);
                        map.put(Check.message, "Updated operations success!");
                        map.put(Check.result, news);
                    }catch (Exception ex){
                        System.err.println("elastic news update : " + ex);
                    }
                }
            } else {
                try {
                    News news1 = newsRepository.saveAndFlush(news);
                    map.put(Check.status, true);
                    map.put(Check.message, "Adding of News Operations Successful!");
                    map.put(Check.result, news);
                    NewsCategory newsCategory = newsCategoryRepository.findById(news.getNewsCategory().getId()).get();
                    ElasticNews elasticNews = new ElasticNews();
                    elasticNews.setId(Integer.toString(news1.getId()));
                    elasticNews.setNo(Long.toString(news1.getNo()));
                    elasticNews.setTitle(news1.getTitle());
                    elasticNews.setSummary(news1.getSummary());
                    elasticNews.setDescription(news1.getDescription());
                    elasticNews.setStatus(news1.getStatus());
                    elasticNews.setDate(news1.getDate());
                    elasticNews.setCategoryName(newsCategory.getName());
                    elasticNewsRepository.save(elasticNews);
                } catch (Exception ex) {
                    map.put(Check.status, false);
                    if (ex.toString().contains("constraint")) {
                        String error = "This news  name has alredy been registered!";
                        Util.logger(error, News.class);
                        map.put(Check.message, error);
                    }
                }
            }
        } else {
            map.put(Check.status, false);
            map.put(Check.errors, Util.errors(bindingResult));
        }
        return map;
    }

    //List of News with pagination
    @ResponseBody
    @GetMapping("/newslist/{stShowNumber}/{stPageNo}")
    public Map<Check,Object> listNews(@PathVariable String stShowNumber,@PathVariable String stPageNo){
        Map<Check,Object> map = new LinkedHashMap<>();
        //System.out.println(stShowNumber + " " + stPageNo);
        try {
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo,showNumber);
            Page<ElasticNews> newsPage = elasticNewsRepository.findByOrderByIdAsc(pageable);
            map.put(Check.status,true);
            map.put(Check.totalPage,newsPage.getTotalPages());
            map.put(Check.message, "News listing on page " + (pageNo + 1) + " is successful");
            map.put(Check.result,newsPage.getContent());
        } catch (Exception e) {
            map.put(Check.status,false);
            String error = "An error occurred during the operation!";
            System.err.println(e);
            Util.logger(error, News.class);
            map.put(Check.message,error);
        }
        return map;
    }

    // Get id data from Choosen news
    @ResponseBody
    @GetMapping("/chosenId/{stId}")
    public int chosenId(@PathVariable String stId) {
        try {
            int id = Integer.parseInt(stId);
            Optional<News> optionalNews = newsRepository.findById(id);
            if (optionalNews.isPresent()) {
                chosenId = id;
            }
        } catch (Exception e) {
            System.err.println("Chosen Id Error: " + e);
        }
        System.out.println("Resim eklemek için seçilen ürünün ıd numarası: " + chosenId);
        return chosenId;
    }

    //  Add News Images
    @PostMapping("/imageUpload")
    public String imageUpload(@RequestParam("imageName") MultipartFile[] files) {
        System.out.println(chosenId);
        Optional<News> optionalNews = newsRepository.findById(chosenId);
        List<String> imageNameList = new ArrayList<>();
        File f = new File(Util.UPLOAD_DIR_NEWS + ("" + chosenId));
        f.delete(); //Varsa önce sil
        if (optionalNews.isPresent()) {
            News news = optionalNews.get();
            if (files != null && files.length != 0) {
                sendCount = files.length;
                String idFolder = "" + chosenId + "/";
                File folderNews = new File(Util.UPLOAD_DIR_NEWS + idFolder);
                boolean status = folderNews.mkdir();
                for (MultipartFile file : files) {
                    long fileSizeMB = file.getSize() / 1024;
                    if (fileSizeMB > Util.maxFileUploadSize) {
                        System.err.println("Dosya boyutu çok büyük Max 5MB");
                        errorMessage = "Dosya boyutu çok büyük Max " + (Util.maxFileUploadSize / 1024) + "MB olmalıdır";
                        System.err.println(errorMessage);
                    } else {
                        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                        String ext = fileName.substring(fileName.length() - 5, fileName.length());
                        String uui = UUID.randomUUID().toString();
                        fileName = uui + ext;
                        try {
                            if (status) {
                                Path path = Paths.get(Util.UPLOAD_DIR_NEWS + idFolder + fileName);
                                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                                sendSuccessCount += 1;
                            } else {
                                errorMessage = "Id numarasına ait haber dosyası bulunamadı!";
                                System.err.println(errorMessage);
                            }
                            // add database
                            imageNameList.add(fileName);

                        } catch (IOException e) {
                            System.err.println(e);
                        }
                    }
                }
                news.setFileName(imageNameList);
                newsRepository.saveAndFlush(news);
                //Elasticsearch save Images - Start
                ElasticNews elasticNews = elasticNewsRepository.findById(Integer.toString(news.getId())).get();
                elasticNews.setFileName(imageNameList);
                elasticNewsRepository.save(elasticNews);
                //Elasticsearch save Images - End
            } else {
                errorMessage = "Lütfen resim seçiniz!";
                System.err.println(errorMessage);
            }
        } else {
            errorMessage = "News is not found!";
            System.err.println(errorMessage);
        }
        return "redirect:/news";
    }

    //Detail of news
    @ResponseBody
    @GetMapping("/detail/{stId}")
    public Map<Check,Object> detailNews(@PathVariable String stId){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            map.put(Check.status,true);
            map.put(Check.message, "News detail operation is successful!");
            map.put(Check.result,newsRepository.findById(id).get());
        } catch (Exception e) {
            map.put(Check.status,false);
            String error = "An error occurred during the operation!";
            System.err.println(e);
            Util.logger(error, News.class);
            map.put(Check.message,error);
        }
        return map;
    }

    //Delete news
    @ResponseBody
    @DeleteMapping("/delete/{stId}")
    public Map<Check,Object> deleteNews(@PathVariable String stId){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<News> optionalNews = newsRepository.findById(id);
            if(optionalNews.isPresent()){
                //Images Delete
                News news = optionalNews.get();
                news.getFileName().forEach(item -> {
                    File file = new File(Util.UPLOAD_DIR_NEWS + stId + "/" + item);
                    file.delete();
                });
                File file = new File(Util.UPLOAD_DIR_NEWS + stId );
                if(file.exists()){
                    file.delete();
                }
                //Images Delete
                ElasticNews elasticNews = elasticNewsRepository.findById(Integer.toString(id)).get();
                newsRepository.deleteById(id);
                elasticNewsRepository.deleteById(elasticNews.getId());
                map.put(Check.status,true);
                map.put(Check.message,"Data has been deleted!");
                map.put(Check.result,optionalNews.get());
            }else{
                String error = "NEws is not found";
                map.put(Check.status,false);
                map.put(Check.message,error);
                Util.logger(error,News.class);
            }
        } catch (Exception e) {
            String error = "An error occurred during the delete operation";
            map.put(Check.status,false);
            map.put(Check.message,error);
            Util.logger(error,News.class);
            System.err.println(e);
        }
        return map;
    }

    //Elasticsearch for News
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

    //Chosen image delete
    @ResponseBody
    @DeleteMapping("/chosenImages/delete/{images}")
    public Map<Check,Object> deleteChosenImage(@PathVariable List<String> images){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            if(images.size() > 0){
                for (int i = 0; i < images.size() ; i++) {
                    System.out.println("Silinmek istenen dosya: " + images.get(i));
                    newsRepository.deleteImageByFileName(images.get(i));
                    File file = new File(Util.UPLOAD_DIR_NEWS + chosenId + "/" + images.get(i));
                    file.delete();
                }
                //Elasticsearch update images - Start
                News news = newsRepository.findById(chosenId).get();
                System.out.println("Id ye ait resimler " + news.getFileName());
                ElasticNews elasticNews = elasticNewsRepository.findById(Integer.toString(chosenId)).get();
                elasticNews.setFileName(news.getFileName());
                elasticNewsRepository.save(elasticNews);
                //Elasticsearch update images - End
                map.put(Check.status,true);
                map.put(Check.message,"The selected pictures have been deleted!");
            }else{
                map.put(Check.status,false);
                map.put(Check.message,"Please select a picture!");
            }
        } catch (Exception e) {
            String error = "An error occurred during the image delete operation";
            map.put(Check.status,false);
            map.put(Check.message,error);
            Util.logger(error,News.class);
            System.err.println(e);
        }
        return map;
    }

    // ===========================================News Operations - END=================================================


}
