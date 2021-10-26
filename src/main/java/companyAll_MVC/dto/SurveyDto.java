package companyAll_MVC.dto;

import companyAll_MVC.documents.ElasticSurvey;
import companyAll_MVC.entities.Likes;
import companyAll_MVC.entities.Survey;
import companyAll_MVC.entities.SurveyOption;
import companyAll_MVC.repositories._elastic.ElasticSurveyRepository;
import companyAll_MVC.repositories._jpa.SurveyOptionRepository;
import companyAll_MVC.repositories._jpa.SurveyRepository;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Util;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SurveyDto {


    final ElasticSurveyRepository elasticSurveyRepository;
    final SurveyRepository surveyRepository;
    final SurveyOptionRepository surveyOptionRepository;


    public SurveyDto(ElasticSurveyRepository elasticSurveyRepository, SurveyRepository surveyRepository, SurveyOptionRepository surveyOptionRepository) {
        this.elasticSurveyRepository = elasticSurveyRepository;
        this.surveyRepository = surveyRepository;
        this.surveyOptionRepository = surveyOptionRepository;
    }

    // Survey List With Pagination
    public Map<Check, Object> listSurveyWithPagination(String stShowNumber,String stPageNo){
        Map<Check,Object> map = new LinkedHashMap<>();
        //System.out.println(stShowNumber + " " + stPageNo);
        try {
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo,showNumber);
            Page<ElasticSurvey> surveyPage = elasticSurveyRepository.findByOrderByIdAscPage(pageable);
            map.put(Check.status,true);
            map.put(Check.totalPage,surveyPage.getTotalPages());
            map.put(Check.message, "Survey listing on page " + (pageNo + 1) + " is successful");
            map.put(Check.result,surveyPage.getContent());
        } catch (Exception e) {
            map.put(Check.status,false);
            String error = "An error occurred during the operation!";
            System.err.println(e);
            Util.logger(error, Survey.class);
            map.put(Check.message,error);
        }
        return map;
    }

    // Survey List PaginationByElasticSearch
    public Map<Check,Object> searchVote(String data,String stPageNo,String stShowNumber){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int pageNo = Integer.parseInt(stPageNo); // .th number of page
            int showNumber = Integer.parseInt(stShowNumber); // Get the show number value
            Pageable pageable = PageRequest.of(pageNo,showNumber);
            Page<ElasticSurvey> searchPage = elasticSurveyRepository.findBySearchDataRest(data,pageable);
            List<ElasticSurvey> elasticSurveyList = searchPage.getContent();
            int totalData =  elasticSurveyList.size(); //for total data in table
            if(totalData > 0 ){
                map.put(Check.status,true);
                map.put(Check.totalPage,searchPage.getTotalPages());
                map.put(Check.message,"Search operation success!");
                map.put(Check.result,elasticSurveyList);
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

    // Vote Add
    public Map<Check, Object> addVote(Integer surveyId, Integer surveyOptionId) {
        Map<Check, Object> hm = new LinkedHashMap<>();

            try{
                List<SurveyOption> surveyOptionList = surveyOptionRepository.findBySurvey_IdEquals(surveyId);
                surveyOptionList.forEach(item->{
                    System.out.println("getId: " + item.getId());
                    System.out.println("surveyOptionId : " + surveyOptionId);
                    if (surveyOptionId == item.getId()){
                        item.setVote(item.getVote()+1);
                        item.getVote();
                        surveyOptionRepository.save(item);
                        hm.put(Check.result,item);
                    }
                });
                hm.put(Check.status, true);
                hm.put(Check.message, "Add Vote Survey_s!");
            }catch (Exception e){
                hm.put(Check.status, false);
                hm.put(Check.message, "An error occured during the operation!");
            }
        return hm;
        }




}
