package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticContents;
import companyAll_MVC.documents.ElasticProduct;
import companyAll_MVC.documents.ElasticProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface ElasticProductRepository extends ElasticsearchRepository<ElasticProduct,String> {

    @Query("{\"bool\":{\"must\":[{\"term\":{\"productId\":\"?0\"}}],\"must_not\":[],\"should\":[]}}")
    Optional<ElasticProduct> findById(Integer productId);

    @Query("{\"bool\":{\"must\":[{\"match_all\":{}}],\"must_not\":[],\"should\":[]}}")
    Page<ElasticProduct> findByOrderByIdAsc(Pageable pageable);

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"prefix\":{\"name\":\"?0\"}},{\"prefix\":{\"status\":\"?0\"}},{\"prefix\":{\"no\":\"?0\"}},{\"prefix\":{\"date\":\"?0\"}},{\"prefix\":{\"description\":\"?0\"}}]}}}")
    Page<ElasticProduct> findBySearchData(String data, Pageable pageable);

}
