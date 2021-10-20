package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticLikes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticLikesRepository extends ElasticsearchRepository<ElasticLikes,String> {

    @Query("{\"bool\":{\"must\":[{\"match_all\":{}}],\"must_not\":[],\"should\":[]}}")
    Page<ElasticLikes> findByOrderByIdAsc(Pageable pageable);

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"prefix\":{\"customerName\":\"?0\"}},{\"prefix\":{\"productName\":\"?0\"}}]}}")
    Page<ElasticLikes> findBySearchData(String data, Pageable pageable);

}
