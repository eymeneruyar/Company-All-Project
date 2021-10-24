package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticAnnouncement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface ElasticAnnouncementRepository extends ElasticsearchRepository<ElasticAnnouncement, String> {
    @Query("{\"bool\":{\"must\":[{\"term\":{\"aid\":\"?0\"}}],\"must_not\":[],\"should\":[]}}")
    Optional<ElasticAnnouncement> findByAid(Integer cid);

    @Query("{\"bool\":{\"should\":[{\"match\":{\"status\":\"?0\"}}],\"must_not\":[]}}")
    List<ElasticAnnouncement> findAllByStatus(String status);
    @Query("{\"bool\":{\"should\":[{\"match\":{\"status\":\"?0\"}}],\"must_not\":[]}}")
    Page<ElasticAnnouncement> findAllByStatus(String status, Pageable pageable);

    @Query("{\"bool\":{\"should\":[{\"prefix\":{\"no\":\"?0\"}},{\"prefix\":{\"title\":\"?0\"}}, " +
            "{\"prefix\":{\"detail\":\"?0\"}}," +
            "{\"prefix\":{\"status\":\"?1\"}}" +
            "],\"must_not\":[]}}")
    List<ElasticAnnouncement> searchByKeyAndStatus(String key, String status);
    @Query("{\"bool\":{\"should\":[{\"prefix\":{\"no\":\"?0\"}},{\"prefix\":{\"title\":\"?0\"}}, " +
            "{\"prefix\":{\"detail\":\"?0\"}}," +
            "{\"prefix\":{\"status\":\"?1\"}}" +
            "],\"must_not\":[]}}")
    Page<ElasticAnnouncement> searchByKeyAndStatus(String key, String status, Pageable pageable);
}
