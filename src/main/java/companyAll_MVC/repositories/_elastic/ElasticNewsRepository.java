package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticNews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ElasticNewsRepository extends ElasticsearchRepository<ElasticNews,String > {

    @Query("{\"bool\":{\"must\":[{\"match_all\":{}}],\"must_not\":[],\"should\":[]}}")
    Page<ElasticNews> findByOrderByIdAsc(Pageable pageable);

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"prefix\":{\"categoryName\":\"?0\"}},{\"prefix\":{\"status\":\"?0\"}},{\"prefix\":{\"description\":\"?0\"}},{\"prefix\":{\"no\":\"?0\"}},{\"prefix\":{\"summary\":\"?0\"}},{\"prefix\":{\"title\":\"?0\"}}]}}")
    Page<ElasticNews> findBySearchData(String data, Pageable pageable);

    @Query("{\"bool\":{\"must\":[{\"match\":{\"categoryId\":\"?0\"}}],\"must_not\":[],\"should\":[]}}")
    Page<ElasticNews> findByNewsCategoryIdEquals(Integer categoryId,Pageable pageable);

    //Number of news by news status
    long countByStatusAllIgnoreCase(String status);

    //All news
    @Query("{\"bool\":{\"must\":[{\"match_all\":{}}],\"must_not\":[],\"should\":[]}},\"aggs\":{}")
    List<ElasticNews> allNews();

}
