package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticContents;
import companyAll_MVC.documents.ElasticLikes;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface ElasticContentsRepository extends ElasticsearchRepository<ElasticContents,String> {

    @Query("{\"bool\":{\"must\":[{\"match_all\":{}}],\"must_not\":[],\"should\":[]}}")
    List<ElasticContents> findByOrderByIdAsc(Pageable pageable);
    @Query("{\"bool\":{\"must\":[{\"match_all\":{}}],\"must_not\":[],\"should\":[]}}")
    List<ElasticContents> findAllByOrderByIdAsc();
    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"prefix\":{\"title\":\"?0\"}},{\"prefix\":{\"status\":\"?0\"}},{\"prefix\":{\"no\":\"?0\"}},{\"prefix\":{\"date\":\"?0\"}},{\"prefix\":{\"description\":\"?0\"}}]}}}")
    Page<ElasticContents> findBySearchData(String data, Pageable pageable);

    @Query("{\"bool\":{\"must\":[{\"term\":{\"contentsId\":\"?0\"}}],\"must_not\":[],\"should\":[]}}")
    Optional<ElasticContents> findById(Integer contentsId);

    @Query("{\"bool\":{\"must\":[{\"match_all\":{}}],\"must_not\":[],\"should\":[]}}")
    List<ElasticContents> allContents();

}
