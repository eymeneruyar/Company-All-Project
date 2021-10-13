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
            try {
                //Date and Number insert operation
                c.setNo(Util.getRandomNumberString());
                c.setDate(Util.getDateFormatter());
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
        }else {
            map.put(Check.status,false);
            map.put(Check.errors, Util.errors(bindingResult));
        }

        System.out.println(c);

        return map;

    }

    //List of Content with pagination
    @ResponseBody
    @GetMapping("/list/{stShowNumber}/{stPageNo}")
    public Map<Check,Object> list(@PathVariable String stShowNumber,@PathVariable String stPageNo){
        Map<Check,Object> map = new LinkedHashMap<>();
        System.out.println(stShowNumber + " " + stPageNo);
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



}
