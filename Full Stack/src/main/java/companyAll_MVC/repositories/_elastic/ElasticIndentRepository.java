package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticIndent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface ElasticIndentRepository extends ElasticsearchRepository<ElasticIndent, String> {

    @Query("{\"bool\":{\"must\":[{\"term\":{\"iid\":\"?0\"}}],\"must_not\":[],\"should\":[]}}")
    Optional<ElasticIndent> findByIid(Integer cid);

    @Query("{\"bool\":{\"should\":[{\"match\":{\"status\":\"?0\"}}],\"must_not\":[]}}")
    List<ElasticIndent> findAllByStatus(String status);
    @Query("{\"bool\":{\"should\":[{\"match\":{\"status\":\"?0\"}}],\"must_not\":[]}}")
    Page<ElasticIndent> findAllByStatus(String status, Pageable pageable);

    @Query("{\"bool\":{\"should\":[{\"match\":{\"no\":\"?0\"}},{\"term\":{\"cno\":\"?0\"}}, " +
            "{\"term\":{\"pno\":\"?0\"}}," +
            "],\"must_not\":[]}}")
    List<ElasticIndent> searchByKeyAndStatus(String key, String status);
    @Query("{\"bool\":{\"should\":[{\"match\":{\"no\":\"?0\"}},{\"term\":{\"cno\":\"?0\"}}, " +
            "{\"term\":{\"pno\":\"?0\"}}," +
            "],\"must_not\":[]}}")
    Page<ElasticIndent> searchByKeyAndStatus(String key, String status, Pageable pageable);

    @Query("{\"bool\":{\"must\":[{\"match_all\":{}}],\"must_not\":[],\"should\":[]}}")
    List<ElasticIndent> allOrders();

    Page<ElasticIndent> findAllByOrderByIidDesc(Pageable pageable);

}
