package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticLikes;
import companyAll_MVC.documents.ElasticLikesProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticLikeProductRepository extends ElasticsearchRepository<ElasticLikesProduct,String> {

    @Query("{\"bool\":{\"must\":[{\"match_all\":{}}],\"must_not\":[],\"should\":[]}}")
    Page<ElasticLikesProduct> findByOrderByIdAsc(Pageable pageable);

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"match\":{\"name\":\"?0\"}},{\"match\":{\"totalRating\":\"?0\"}},{\"prefix\":{\"name\":\"?0\"}},{\"prefix\":{\"totalRating\":\"?0\"}}]}}")
    Page<ElasticLikesProduct> findBySearchDataProductLike(String data, Pageable pageable);

}
