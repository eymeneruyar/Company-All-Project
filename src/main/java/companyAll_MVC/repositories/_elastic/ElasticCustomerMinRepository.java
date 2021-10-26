package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticCustomerMin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface ElasticCustomerMinRepository extends ElasticsearchRepository<ElasticCustomerMin, String> {
    @Query("{\"bool\":{\"should\":[{\"match\":{\"status\":\"?0\"}}],\"must_not\":[]}}")
    List<ElasticCustomerMin> findAllByStatus(String status);
    @Query("{\"bool\":{\"should\":[{\"match\":{\"status\":\"?0\"}}],\"must_not\":[]}}")
    Page<ElasticCustomerMin> findAllByStatus(String status, Pageable pageable);

    @Query("{\"bool\":{\"must\":[{\"term\":{\"cid\":\"?0\"}}],\"must_not\":[],\"should\":[]}}")
    Optional<ElasticCustomerMin> findByCid(Integer cid);
}
