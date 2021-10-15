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

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"match\":{\"name\":\"?0\"}},{\"match\":{\"status\":\"?0\"}},{\"match\":{\"no\":\"?0\"}},{\"match\":{\"date\":\"?0\"}},{\"match\":{\"description\":\"?0\"}}]}}}")
    Page<ElasticProduct> findBySearchData(String data, Pageable pageable);

}
