package companyAll_MVC.controllers;

import companyAll_MVC.documents.ElasticContents;
import companyAll_MVC.entities.Contents;
import companyAll_MVC.repositories._elastic.ElasticContentsRepository;
import companyAll_MVC.repositories._jpa.ContentsRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Util;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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


}
