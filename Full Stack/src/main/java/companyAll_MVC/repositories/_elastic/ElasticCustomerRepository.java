package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticCustomer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface ElasticCustomerRepository extends ElasticsearchRepository<ElasticCustomer, String> {
    @Query("{\"bool\":{\"should\":[{\"match\":{\"status\":\"?0\"}}],\"must_not\":[]}}")
    List<ElasticCustomer> findAllByStatus(String status);
    @Query("{\"bool\":{\"should\":[{\"match\":{\"status\":\"?0\"}}],\"must_not\":[]}}")
    Page<ElasticCustomer> findAllByStatus(String status, Pageable pageable);

    @Query("{\"bool\":{\"must\":[{\"term\":{\"cid\":\"?0\"}}],\"must_not\":[],\"should\":[]}}")
    Optional<ElasticCustomer> findByCid(Integer cid);

    @Query("{\"bool\":{\"should\":[{\"match\":{\"no\":\"?0\"}},{\"term\":{\"name\":\"?0\"}}, " +
            "{\"term\":{\"surname\":\"?0\"}}," +
            "{\"term\":{\"phone1\":\"?0\"}}," +
            "{\"term\":{\"mail\":\"?0\"}}," +
            "{\"term\":{\"taxno\":\"?0\"}}," +
            "{\"term\":{\"country\":\"?0\"}}," +
            "{\"term\":{\"city\":\"?0\"}},"+
            "{\"term\":{\"status\":\"?1\"}}"+
            "],\"must_not\":[]}}")
    List<ElasticCustomer> searchByKeyAndStatus(String key, String status);
    @Query("{\"bool\":{\"should\":[{\"match\":{\"no\":\"?0\"}},{\"term\":{\"name\":\"?0\"}}, " +
            "{\"term\":{\"surname\":\"?0\"}}," +
            "{\"term\":{\"phone1\":\"?0\"}}," +
            "{\"term\":{\"mail\":\"?0\"}}," +
            "{\"term\":{\"taxno\":\"?0\"}}," +
            "{\"term\":{\"country\":\"?0\"}}," +
            "{\"term\":{\"city\":\"?0\"}}," +
            "{\"term\":{\"status\":\"?1\"}}" +
            "],\"must_not\":[]}}")
    Page<ElasticCustomer> searchByKeyAndStatus(String key, String status, Pageable pageable);

}
