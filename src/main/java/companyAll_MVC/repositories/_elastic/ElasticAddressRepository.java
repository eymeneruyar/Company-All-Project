package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticAddress;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface ElasticAddressRepository extends ElasticsearchRepository<ElasticAddress, String> {
    @Query("{\"bool\":{\"must\":[{\"term\":{\"aid\":\"?0\"}}],\"must_not\":[],\"should\":[]}}")
    Optional<ElasticAddress> findByAid(Integer aid);
}
