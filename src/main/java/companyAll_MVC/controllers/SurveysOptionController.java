package companyAll_MVC.controllers;

import companyAll_MVC.documents.ElasticSurveyOption;
import companyAll_MVC.entities.SurveyOption;
import companyAll_MVC.repositories._elastic.ElasticSurveyOptionRepository;
import companyAll_MVC.repositories._jpa.SurveyOptionRepository;
import companyAll_MVC.utils.Check;
import companyAll_MVC.utils.Util;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/surveysOption")
public class SurveysOptionController {

    final SurveyOptionRepository surveyOptionRepository;
    final ElasticSurveyOptionRepository elasticSurveyOptionRepository;

    public SurveysOptionController(SurveyOptionRepository surveyOptionRepository, ElasticSurveyOptionRepository elasticSurveyOptionRepository) {
        this.surveyOptionRepository = surveyOptionRepository;
        this.elasticSurveyOptionRepository = elasticSurveyOptionRepository;
    }

    @GetMapping("")
    public String surveysOption() {
        return "surveysOption";
    }

    // Option Add - Ajax
    @PostMapping("/add")
    @ResponseBody
    public Map<Check, Object> add(@RequestBody @Valid SurveyOption surveyOption, BindingResult bindingResult) {
        Map<Check, Object> map = new LinkedHashMap<>();
        if (!bindingResult.hasErrors()) {
            try {
                SurveyOption surveyOption1 = surveyOptionRepository.saveAndFlush(surveyOption);
                map.put(Check.status, true);
                map.put(Check.message, "Adding of SurveyOption Operations Successful!");
                map.put(Check.result, surveyOption1);

                ElasticSurveyOption elasticSurveyOption = new ElasticSurveyOption();
                elasticSurveyOption.setId(Integer.toString(surveyOption1.getId()));
                elasticSurveyOption.setNo(Long.toString(surveyOption1.getNo()));
                elasticSurveyOption.setTitle(surveyOption1.getTitle());
                elasticSurveyOption.setVote(Integer.toString(surveyOption1.getVote()));
                elasticSurveyOption.setDate(surveyOption1.getDate());
                elasticSurveyOptionRepository.save(elasticSurveyOption);
            } catch (Exception ex) {
                map.put(Check.status, false);
                if (ex.toString().contains("constraint")) {
                    String error = "This title has already been registered!";
                    Util.logger(error, SurveyOption.class);
                    map.put(Check.message, error);
                    System.out.println(map);
                }
            }
        } else {
            map.put(Check.status, false);
            map.put(Check.errors, Util.errors(bindingResult));
        }
        return map;
    }


    // Option List By SurverID - Ajax
    @GetMapping("/list/{surveyId}")
    @ResponseBody
    public List<SurveyOption> optionList(@PathVariable Integer surveyId) {
        return surveyOptionRepository.findBySurvey_IdEquals(surveyId);
    }

    // Option Delete - Ajax
    @DeleteMapping("/delete/{index}")
    @ResponseBody
    public Map<Check,Object> delete(@PathVariable String index){
        Map<Check,Object> map = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(index);
            Optional<SurveyOption> optionalOptions = surveyOptionRepository.findById(id);
            if(optionalOptions.isPresent()){
                ElasticSurveyOption elasticSurveyOption = new ElasticSurveyOption();
                elasticSurveyOptionRepository.deleteById(index);
                surveyOptionRepository.deleteById(id);
                map.put(Check.status,true);
                map.put(Check.message,"Data has been deleted!");
                map.put(Check.result,optionalOptions.get());
            }else{
                String error = "SurveyOptions not found";
                map.put(Check.status,false);
                map.put(Check.message,error);
                Util.logger(error,SurveyOption.class);
            }
        } catch (Exception e) {
            String error = "An error occurred during the delete operation";
            map.put(Check.status,false);
            map.put(Check.message,error);
            Util.logger(error,SurveyOption.class);
            System.err.println("error delete options controller: " +e);
        }
        return map;
    }

}
