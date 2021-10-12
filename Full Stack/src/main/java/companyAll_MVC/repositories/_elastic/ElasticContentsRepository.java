package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticContents;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ElasticContentsRepository extends ElasticsearchRepository<ElasticContents,String> {

    @Query("{\"bool\":{\"must\":[{\"match_all\":{}}],\"must_not\":[],\"should\":[]}}")
    List<ElasticContents> findByOrderByIdAsc(Pageable pageable);
    @Query("{\"bool\":{\"must\":[{\"match_all\":{}}],\"must_not\":[],\"should\":[]}}")
    List<ElasticContents> findAllByOrderByIdAsc();

}
