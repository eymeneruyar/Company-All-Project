package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticSurveyOption;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticSurveyOptionRepository extends ElasticsearchRepository<ElasticSurveyOption,String> {
}
