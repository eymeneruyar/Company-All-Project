package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticCustomer;
import companyAll_MVC.documents.ElasticLikes;
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

    @Query("{\"bool\":{\"should\":[{\"prefix\":{\"no\":\"?0\"}},{\"prefix\":{\"name\":\"?0\"}}, " +
            "{\"prefix\":{\"surname\":\"?0\"}}," +
            "{\"prefix\":{\"phone1\":\"?0\"}}," +
            "{\"prefix\":{\"mail\":\"?0\"}}," +
            "{\"prefix\":{\"taxno\":\"?0\"}}," +
            "{\"prefix\":{\"country\":\"?0\"}}," +
            "{\"prefix\":{\"city\":\"?0\"}}," +
            "{\"prefix\":{\"status\":\"?1\"}}" +
            "],\"must_not\":[]}}")
    List<ElasticCustomer> searchByKeyAndStatus(String key, String status);
    @Query("{\"bool\":{\"should\":[{\"prefix\":{\"no\":\"?0\"}},{\"prefix\":{\"name\":\"?0\"}}, " +
            "{\"prefix\":{\"surname\":\"?0\"}}," +
            "{\"prefix\":{\"phone1\":\"?0\"}}," +
            "{\"prefix\":{\"mail\":\"?0\"}}," +
            "{\"prefix\":{\"taxno\":\"?0\"}}," +
            "{\"prefix\":{\"country\":\"?0\"}}," +
            "{\"prefix\":{\"city\":\"?0\"}}," +
            "{\"prefix\":{\"status\":\"?1\"}}" +
            "],\"must_not\":[]}}")
    Page<ElasticCustomer> searchByKeyAndStatus(String key, String status, Pageable pageable);

    @Query("{\"bool\":{\"must\":[{\"match_all\":{}}],\"must_not\":[],\"should\":[]}}")
    List<ElasticCustomer> allCustomers();

}
