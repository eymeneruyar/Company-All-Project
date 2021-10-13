package companyAll_MVC.controllers;

import companyAll_MVC.documents.ElasticContents;
import companyAll_MVC.entities.Contents;
import companyAll_MVC.repositories._elastic.ElasticContentsRepository;
import companyAll_MVC.repositories._jpa.ContentsRepository;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Util;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@Controller
@RequestMapping("/contents")
public class ContentsController {

    final ContentsRepository contentsRepository;
    final ElasticContentsRepository elasticContentsRepository;
    final ElasticsearchOperations elasticsearchOperations;
    public ContentsController(ContentsRepository contentsRepository, ElasticContentsRepository elasticContentsRepository, ElasticsearchOperations elasticsearchOperations) {
        this.contentsRepository = contentsRepository;
        this.elasticContentsRepository = elasticContentsRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @GetMapping("")
    public String contents(){
        return "contents";
    }

    //Addition of content
    @ResponseBody
    @PostMapping("/add")
    public Map<Check,Object> add(@RequestBody @Valid Contents c, BindingResult bindingResult){
        Map<Check,Object> map = new LinkedHashMap<>();
        if(!bindingResult.hasErrors()){
            if(c.getId() != null){
                c.setDate(Util.getDateFormatter());
                String no = contentsRepository.findById(c.getId()).get().getNo();
                c.setNo(no);
                System.out.println(c);
                Optional<Contents> optionalContents = contentsRepository.findById(c.getId());
                if(optionalContents.isPresent()){
                    try {
                        //ElasticSearch and SQL DB Update -Start
                        ElasticContents elasticContents = elasticContentsRepository.findById(c.getId()).get();
                        elasticContentsRepository.deleteById(elasticContents.getId());
                        Contents contents = contentsRepository.saveAndFlush(c);
                        ElasticContents elasticContentsNew = new ElasticContents();
                        elasticContentsNew.setContentsId(contents.getId());
                        elasticContentsNew.setTitle(contents.getTitle());
                        elasticContentsNew.setDescription(contents.getDescription());
                        elasticContentsNew.setStatus(contents.getStatus());
                        elasticContentsNew.setNo(contents.getNo());
                        elasticContentsNew.setDetails(contents.getDetails());
                        elasticContentsNew.setDate(contents.getDate());
                        elasticContentsRepository.save(elasticContentsNew);
                        //ElasticSearch and SQL DB Update - End
                        map.put(Check.status,true);
                        map.put(Check.message,"Updated operations success!");
                        map.put(Check.result,contents);
                    } catch (Exception e) {
                        System.err.println("Elasticsearch update" + e);
                    }
                }
            }else{
                try {
                    c.setDate(Util.getDateFormatter());
                    c.setNo(Util.getRandomNumberString());
                    Contents contents = contentsRepository.saveAndFlush(c);
                    map.put(Check.status,true);
                    map.put(Check.message,"Adding of Content Operations Successful!");
                    map.put(Check.result,contents);
                    //Elasticsearch save
                    ElasticContents elasticContents = new ElasticContents();
                    elasticContents.setContentsId(contents.getId());
                    elasticContents.setTitle(contents.getTitle());
                    elasticContents.setDescription(contents.getDescription());
                    elasticContents.setStatus(contents.getStatus());
                    elasticContents.setNo(contents.getNo());
                    elasticContents.setDetails(contents.getDetails());
                    elasticContents.setDate(contents.getDate());
                    elasticContentsRepository.save(elasticContents);
                } catch (Exception e) {
                    map.put(Check.status,false);
                    if(e.toString().contains("constraint")){
                        String error = "This title has already been registered!";
                        Util.logger(error, Contents.class);
                        map.put(Check.message,error);
                    }
                }
            }
        }else {
            map.put(Check.status,false);
            map.put(Check.errors, Util.errors(bindingResult));
        }

        return map;

    }

    //List of Content with pagination
    @ResponseBody
    @GetMapping("/list/{stShowNumber}/{stPageNo}")
    public Map<Check,Object> list(@PathVariable String stShowNumber,@PathVariable String stPageNo){
        Map<Check,Object> map = new LinkedHashMap<>();
        //System.out.println(stShowNumber + " " + stPageNo);
        try {
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo,showNumber);
            int totalData =  elasticContentsRepository.findAllByOrderByIdAsc().size(); //for total data in table
            List<ElasticContents> elasticContentsList = elasticContentsRepository.findByOrderByIdAsc(pageable);
            map.put(Check.status,true);
            map.put(Check.totalPage,Util.getTotalPage(totalData,showNumber));
            map.put(Check.message, "Content listing on page " + (pageNo) + " is successful");
            map.put(Check.result,elasticContentsList);
        } catch (Exception e) {
            map.put(Check.status,false);
            String error = "An error occurred during the operation!";
            System.err.println(e);
            Util.logger(error, Contents.class);
            map.put(Check.message,error);
        }
        System.out.println(map);
        return map;
    }

    //Total page number
    @ResponseBody
    @GetMapping("/totalPageValue/{stShowNumber}")
    public int totalPageValue(@PathVariable String stShowNumber){
        int totalPage = 0;
        try {
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            int totalData =  elasticContentsRepository.findAllByOrderByIdAsc().size(); //for total data in table
            totalPage = Util.getTotalPage(totalData,showNumber);
        } catch (NumberFormatException e) {
            System.err.println(e);
        }
        return totalPage;
    }

    @ResponseBody
    @DeleteMapping("/delete/{stId}")
    public Map<Check,Object> delete(@PathVariable String stId){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<Contents> optionalContents = contentsRepository.findById(id);
            if(optionalContents.isPresent()){
                ElasticContents elasticContents = elasticContentsRepository.findById(id).get();
                contentsRepository.deleteById(id);
                elasticContentsRepository.deleteById(elasticContents.getId());
                map.put(Check.status,true);
                map.put(Check.message,"Data has been deleted!");
                map.put(Check.result,optionalContents.get());
            }else{
                String error = "Content not found";
                map.put(Check.status,false);
                map.put(Check.message,error);
                Util.logger(error,Contents.class);
            }
        } catch (Exception e) {
            String error = "An error occurred during the delete operation";
            map.put(Check.status,false);
            map.put(Check.message,error);
            Util.logger(error,Contents.class);
            System.err.println(e);
        }
        return map;
    }

    //Elasticsearch
    @ResponseBody
    @GetMapping("/search/{data}/{stPageNo}/{stShowNumber}")
    public Map<Check,Object> search(@PathVariable String data,@PathVariable String stPageNo,@PathVariable String stShowNumber){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo,showNumber);
            Page<ElasticContents> searchPage = elasticContentsRepository.findBySearchData(data,pageable);
            List<ElasticContents> elasticContentsList = searchPage.getContent();
            int totalData =  elasticContentsList.size(); //for total data in table
            if(totalData > 0 ){
                map.put(Check.status,true);
                map.put(Check.totalPage,Util.getTotalPage(totalData,showNumber));
                map.put(Check.message,"Search operation success!");
                map.put(Check.result,elasticContentsList);
            }else{
                map.put(Check.status,false);
                map.put(Check.message,"Could not find a result for your search!");
            }
        } catch (NumberFormatException e) {
            map.put(Check.status,false);
            String error = "An error occurred during the search operation!";
            System.err.println(e);
            Util.logger(error, Contents.class);
            map.put(Check.message,error);
        }
        return map;
    }

    //Contents insert all data to elasticsearch database
    @GetMapping("/elasticInsertData")
    public Map<Check,Object> elasticInsertData(){
        Map<Check,Object> map = new LinkedHashMap<>();
        List<Contents> contentsList = contentsRepository.findAll();
        try {
            if(contentsList.size() > 0){
                contentsList.forEach(item -> {
                    //ElasticSearch Save
                    ElasticContents elasticContents = new ElasticContents();
                    elasticContents.setContentsId(item.getId());
                    elasticContents.setStatus(item.getStatus());
                    elasticContents.setTitle(item.getTitle());
                    elasticContents.setDescription(item.getDescription());
                    elasticContents.setDetails(item.getDetails());
                    elasticContents.setDate(item.getDate());
                    elasticContents.setNo(item.getNo());
                    elasticContentsRepository.save(elasticContents);
                });
                map.put(Check.status,true);
                map.put(Check.message,"Elasticsearch veri ekleme işlemi başarılı!");
                //map.put(Check.result,elasticContentsRepository.findAll());
            }else {
                String error = "Sisteme kayıtlı içerik bulunmamaktadır!";
                map.put(Check.status,false);
                map.put(Check.message,error);
                Util.logger(error,Contents.class);
            }
        } catch (Exception e) {
            String error = "Elasticsearch veri tabanına ekleme yapılırken bir hata oluştu!" + e;
            map.put(Check.status,false);
            map.put(Check.message,error);
            Util.logger(error,Contents.class);
        }
        return map;
    }

}
