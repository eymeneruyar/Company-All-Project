package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticContents;
import companyAll_MVC.documents.ElasticProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface ElasticProductCategoryRepository extends ElasticsearchRepository<ElasticProductCategory,String> {

    @Query("{\"bool\":{\"must\":[{\"term\":{\"categoryId\":\"?0\"}}],\"must_not\":[],\"should\":[]}}")
    Optional<ElasticProductCategory> findById(Integer categoryId);

    @Query("{\"bool\":{\"must\":[{\"match_all\":{}}],\"must_not\":[],\"should\":[]}}")
    Page<ElasticProductCategory> findByOrderByIdAsc(Pageable pageable);

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"prefix\":{\"name\":\"?0\"}},{\"prefix\":{\"status\":\"?0\"}},{\"prefix\":{\"no\":\"?0\"}},{\"prefix\":{\"date\":\"?0\"}}]}}}")
    Page<ElasticProductCategory> findBySearchData(String data, Pageable pageable);

}
