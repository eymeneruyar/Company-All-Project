package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticContents;
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
    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"match\":{\"title\":\"?0\"}},{\"match\":{\"status\":\"?0\"}},{\"match\":{\"no\":\"?0\"}},{\"match\":{\"date\":\"?0\"}},{\"match\":{\"description\":\"?0\"}}]}}}")
    Page<ElasticContents> findBySearchData(String data, Pageable pageable);

    @Query("{\"bool\":{\"must\":[{\"term\":{\"contentsId\":\"?0\"}}],\"must_not\":[],\"should\":[]}}")
    Optional<ElasticContents> findById(Integer contentsId);

}
