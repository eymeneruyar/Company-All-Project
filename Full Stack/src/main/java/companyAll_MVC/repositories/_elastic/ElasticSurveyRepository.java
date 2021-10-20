package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticSurvey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface ElasticSurveyRepository extends ElasticsearchRepository<ElasticSurvey,String> {

    @Query("{\"bool\":{\"must\":[{\"match_all\":{}}],\"must_not\":[],\"should\":[]}}")
    List<ElasticSurvey> findByOrderByIdAsc(Pageable pageable);

    @Query("{\"bool\":{\"must\":[{\"match_all\":{}}],\"must_not\":[],\"should\":[]}}")
    List<ElasticSurvey> findAllByOrderByIdAsc();

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"prefix\":{\"detail\":\"?0\"}},{\"prefix\":{\"date\":\"?0\"}},{\"prefix\":{\"no\":\"?0\"}},{\"prefix\":{\"status\":\"?0\"}},{\"prefix\":{\"title\":\"?0\"}}]}}")
    Page<ElasticSurvey> findBySearchData(String data, Pageable pageable);

    @Query("{\"bool\":{\"must\":[{\"term\":{\"surveyId\":\"?0\"}}],\"must_not\":[],\"should\":[]}}")
    Optional<ElasticSurvey> findById(Integer surveyId);

}
