package companyAll_MVC.restcontroller;

import companyAll_MVC.dto.SurveyDto;
import companyAll_MVC.entities.Likes;
import companyAll_MVC.entities.Survey;
import companyAll_MVC.entities.SurveyOption;
import companyAll_MVC.repositories._elastic.ElasticSurveyRepository;
import companyAll_MVC.utils.Check;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/surveys")
//@Api(value = "SurveysRestController",authorizations = {@Authorization(value = "basicAuth")})
public class SurveyRestController {

    final ElasticSurveyRepository elasticSurveyRepository;
    final SurveyDto surveyDto;

    public SurveyRestController(ElasticSurveyRepository elasticSurveyRepository, SurveyDto surveyDto) {
        this.elasticSurveyRepository = elasticSurveyRepository;
        this.surveyDto = surveyDto;
    }

    // Survey List With Pagination
    @GetMapping("/list/{stShowNumber}/{stPageNo}")
    @ApiOperation(value = "Products is listed by page no and number of show.")
    public Map<Check, Object> listSurveyWithPagination(@PathVariable String stShowNumber, @PathVariable String stPageNo) {
        return surveyDto.listSurveyWithPagination(stShowNumber, stPageNo);
    }

    // Survey List PaginationByElasticSearch
    @GetMapping("/search/{data}/{stPageNo}/{stShowNumber}")
    @ApiOperation(value = "Products is searched by page no and number of show.")
    public Map<Check,Object> searchVote(@PathVariable String data,@PathVariable String stPageNo,@PathVariable String stShowNumber) {
        return surveyDto.searchVote(data, stPageNo, stShowNumber);
    }

    // vote - add
    @GetMapping("/addVote/{surveyId}/{surveyOptionId}")
    @ResponseBody
    public Map<Check, Object> addVote(@PathVariable Integer surveyId, @PathVariable Integer surveyOptionId) {
        return surveyDto.addVote(surveyId,surveyOptionId);
    }




}
