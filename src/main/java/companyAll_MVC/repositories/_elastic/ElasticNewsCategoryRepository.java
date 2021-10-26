package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticNewsCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface ElasticNewsCategoryRepository extends ElasticsearchRepository<ElasticNewsCategory,String> {

    @Query("{\"bool\":{\"must\":[{\"term\":{\"categoryId\":\"?0\"}}],\"must_not\":[],\"should\":[]}}")
    Optional<ElasticNewsCategory> findById(Integer categoryId);

    @Query("{\"bool\":{\"must\":[{\"match_all\":{}}],\"must_not\":[],\"should\":[]}}")
    Page<ElasticNewsCategory> findByOrderByIdAsc(Pageable pageable);

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"prefix\":{\"date\":\"?0\"}},{\"prefix\":{\"detail\":\"?0\"}},{\"prefix\":{\"name\":\"?0\"}},{\"prefix\":{\"no\":\"?0\"}},{\"prefix\":{\"status\":\"?0\"}},{\"prefix\":{\"subname\":\"?0\"}}]}}")
    Page<ElasticNewsCategory> findBySearchData(String data, Pageable pageable);
}
