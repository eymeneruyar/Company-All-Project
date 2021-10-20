package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticContents;
import companyAll_MVC.documents.ElasticProduct;
import companyAll_MVC.documents.ElasticProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface ElasticProductRepository extends ElasticsearchRepository<ElasticProduct,String> {

    @Query("{\"bool\":{\"must\":[{\"term\":{\"productId\":\"?0\"}}],\"must_not\":[],\"should\":[]}}")
    Optional<ElasticProduct> findById(Integer productId);

    @Query("{\"bool\":{\"must\":[{\"match_all\":{}}],\"must_not\":[],\"should\":[]}}")
    Page<ElasticProduct> findByOrderByIdAsc(Pageable pageable);

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"prefix\":{\"name\":\"?0\"}},{\"prefix\":{\"status\":\"?0\"}},{\"prefix\":{\"no\":\"?0\"}},{\"prefix\":{\"date\":\"?0\"}},{\"prefix\":{\"description\":\"?0\"}}]}}}")
    Page<ElasticProduct> findBySearchData(String data, Pageable pageable);

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"match\":{\"name\":\"?0\"}},{\"match\":{\"status\":\"?0\"}},{\"match\":{\"no\":\"?0\"}},{\"match\":{\"date\":\"?0\"}},{\"match\":{\"description\":\"?0\"}}]}}}")
    Page<ElasticProduct> findBySearchDataMatch(String data, Pageable pageable);

    Page<ElasticProduct> findByProductCategoryIdEquals(Integer categoryId,Pageable pageable);

    @Query("{\"bool\":{\"must\":[{\"match_all\":{}}],\"must_not\":[],\"should\":[]}}")
    List<ElasticProduct> findAllProducts();

    //List<ElasticProduct> findElasticProductsBySizeBetween(Integer size1,Integer size2);

}
