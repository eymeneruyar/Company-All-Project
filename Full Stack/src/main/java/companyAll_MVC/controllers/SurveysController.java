package companyAll_MVC.controllers;

import companyAll_MVC.documents.ElasticSurvey;
import companyAll_MVC.entities.Survey;
import companyAll_MVC.repositories._elastic.ElasticSurveyRepository;
import companyAll_MVC.repositories._jpa.SurveyRepository;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Util;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/surveys")
public class SurveysController {

    final SurveyRepository surveyRepository;
    final ElasticSurveyRepository elasticSurveyRepository;

    public SurveysController(SurveyRepository surveyRepository, ElasticSurveyRepository elasticSurveyRepository) {
        this.surveyRepository = surveyRepository;
        this.elasticSurveyRepository = elasticSurveyRepository;
    }

    @GetMapping("")
    public String surveys() {
        return "surveys";
    }

    // Survey Add - Ajax
    @PostMapping("/add")
    @ResponseBody
    public Map<Check,Object> add(@RequestBody @Valid Survey survey, BindingResult bindingResult){
        Map<Check,Object> map = new LinkedHashMap<>();
        if(!bindingResult.hasErrors()){
            try{
                Survey survey1 = surveyRepository.saveAndFlush(survey);
                map.put(Check.status,true);
                map.put(Check.message,"Adding of Survey Operations Successful!");
                map.put(Check.result,survey1);

                ElasticSurvey elasticSurvey = new ElasticSurvey();
                elasticSurvey.setId(Integer.toString(survey1.getId()));
                elasticSurvey.setNo(Long.toString(survey1.getNo()));
                elasticSurvey.setTitle(survey1.getTitle());
                elasticSurvey.setDetail(survey1.getDetail());
                elasticSurvey.setStatus(survey1.getStatus());
                elasticSurvey.setDate(survey1.getDate());
                elasticSurveyRepository.save(elasticSurvey);
            }catch (Exception ex) {
                map.put(Check.status,false);
                if (ex.toString().contains("constraint")) {
                    String error = "This title has already been registered!";
                    Util.logger(error, Survey.class);
                    map.put(Check.message,error);
                }
            }
        }else {
            map.put(Check.status,false);
            map.put(Check.errors,Util.errors(bindingResult));
        }
        System.out.println(survey);
        return map;
    }

    // Survey List - Ajax
    @GetMapping("/list")
    @ResponseBody
    public List<Survey> list() {
        return surveyRepository.findAll();
    }

    // Survey Delete - Ajax
    @ResponseBody
    @DeleteMapping("/delete/{stId}")
    public Map<Check,Object> delete(@PathVariable String stId){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<Survey> optionalSurvey = surveyRepository.findById(id);
            if(optionalSurvey.isPresent()){
                ElasticSurvey elasticContents = elasticSurveyRepository.findById(Integer.toString(id)).get();
                surveyRepository.deleteById(id);
                elasticSurveyRepository.deleteById(elasticContents.getId());
                map.put(Check.status,true);
                map.put(Check.message,"Data has been deleted!");
                map.put(Check.result,optionalSurvey.get());
            }else{
                String error = "Survey not found";
                map.put(Check.status,false);
                map.put(Check.message,error);
                Util.logger(error,Survey.class);
            }
        } catch (Exception e) {
            String error = "An error occurred during the delete operation";
            map.put(Check.status,false);
            map.put(Check.message,error);
            Util.logger(error,Survey.class);
            System.err.println(e);
        }
        return map;
    }

    //List of Survey with pagination
    @ResponseBody
    @GetMapping("/all/{page}/{size}")
    public Map<Check,Object> list(@PathVariable String page,@PathVariable String size){
        Map<Check,Object> map = new LinkedHashMap<>();
        System.out.println(size + " " + page);
        try {
            int pageNo = Integer.parseInt(page); // .th number of page
            int showNumber = Integer.parseInt(size); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo,showNumber);
            int totalData =  elasticSurveyRepository.findAllByOrderByIdAsc().size(); //for total data in table
            List<ElasticSurvey> elasticContentsList = elasticSurveyRepository.findByOrderByIdAsc(pageable);
            map.put(Check.status,true);
            map.put(Check.totalPage,Util.getTotalPage(totalData,showNumber));
            map.put(Check.message, "Content listing on page " + (pageNo) + " is successful");
            map.put(Check.result,elasticContentsList);
        } catch (Exception e) {
            map.put(Check.status,false);
            String error = "An error occurred during the operation!";
            System.err.println(e);
            Util.logger(error, Survey.class);
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
            int totalData =  elasticSurveyRepository.findAllByOrderByIdAsc().size(); //for total data in table
            totalPage = Util.getTotalPage(totalData,showNumber);
        } catch (NumberFormatException e) {
            System.err.println(e);
        }
        return totalPage;
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
            Page<ElasticSurvey> searchPage = elasticSurveyRepository.findBySearchData(data,pageable);
            List<ElasticSurvey> elasticSurveysList = searchPage.getContent();
            System.out.println("burada searchPage:" + searchPage);
            int totalData =  elasticSurveysList.size(); //for total data in table
            if(totalData > 0 ){
                map.put(Check.status,true);
                map.put(Check.totalPage,Util.getTotalPage(totalData,showNumber));
                map.put(Check.message,"Search operation success!");
                map.put(Check.result,elasticSurveysList);
            }else{
                map.put(Check.status,false);
                map.put(Check.message,"Could not find a result for your search!");
            }
        } catch (NumberFormatException e) {
            map.put(Check.status,false);
            String error = "An error occurred during the search operation!";
            System.err.println(e);
            Util.logger(error, Survey.class);
            map.put(Check.message,error);
        }
        return map;
    }

}
