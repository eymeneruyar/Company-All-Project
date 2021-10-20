package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticAdvertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface ElasticAdvertisementRepository extends ElasticsearchRepository<ElasticAdvertisement, String> {
    @Query("{\"bool\":{\"must\":[{\"term\":{\"aid\":\"?0\"}}],\"must_not\":[],\"should\":[]}}")
    Optional<ElasticAdvertisement> findByAid(Integer cid);

    @Query("{\"bool\":{\"should\":[{\"match\":{\"status\":\"?0\"}}],\"must_not\":[]}}")
    List<ElasticAdvertisement> findAllByStatus(String status);
    @Query("{\"bool\":{\"should\":[{\"match\":{\"status\":\"?0\"}}],\"must_not\":[]}}")
    Page<ElasticAdvertisement> findAllByStatus(String status, Pageable pageable);

    @Query("{\"bool\":{\"should\":[{\"match\":{\"no\":\"?0\"}},{\"term\":{\"name\":\"?0\"}}, " +
            "{\"term\":{\"view\":\"?0\"}}," +
            "{\"term\":{\"width\":\"?0\"}}," +
            "{\"term\":{\"height\":\"?0\"}}," +
            "{\"term\":{\"click\":\"?0\"}}," +
            "{\"term\":{\"status\":\"?1\"}}" +
            "],\"must_not\":[]}}")
    List<ElasticAdvertisement> searchByKeyAndStatus(String key, String status);
    @Query("{\"bool\":{\"should\":[{\"match\":{\"no\":\"?0\"}},{\"term\":{\"name\":\"?0\"}}, " +
            "{\"term\":{\"view\":\"?0\"}}," +
            "{\"term\":{\"width\":\"?0\"}}," +
            "{\"term\":{\"height\":\"?0\"}}," +
            "{\"term\":{\"click\":\"?0\"}}," +
            "{\"term\":{\"status\":\"?1\"}}" +
            "],\"must_not\":[]}}")
    Page<ElasticAdvertisement> searchByKeyAndStatus(String key, String status, Pageable pageable);
}
