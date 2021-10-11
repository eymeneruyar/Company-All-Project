package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticContents;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface ElasticContentsRepository extends ElasticsearchRepository<ElasticContents,String> {
}
