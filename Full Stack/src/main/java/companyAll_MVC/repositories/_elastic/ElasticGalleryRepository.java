package companyAll_MVC.repositories._elastic;

import companyAll_MVC.documents.ElasticGallery;
import companyAll_MVC.documents.ElasticNews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticGalleryRepository extends ElasticsearchRepository<ElasticGallery,String > {


    @Query("{\"bool\":{\"must\":[{\"match_all\":{}}],\"must_not\":[],\"should\":[]}}")
    Page<ElasticGallery> findByOrderByIdAsc(Pageable pageable);

    @Query("{\"bool\":{\"must\":[],\"must_not\":[],\"should\":[{\"prefix\":{\"categoryName\":\"?0\"}},{\"prefix\":{\"description\":\"?0\"}},{\"prefix\":{\"title\":\"?0\"}},{\"prefix\":{\"status\":\"?0\"}},{\"prefix\":{\"no\":\"?0\"}},{\"prefix\":{\"date\":\"?0\"}}]}}")
    Page<ElasticGallery> findBySearchData(String data, Pageable pageable);

    @Query("{\"bool\":{\"must\":[{\"match\":{\"categoryId\":\"?0\"}}],\"must_not\":[],\"should\":[]}}")
    Page<ElasticGallery> findByGalleryCategoryIdEquals(Integer categoryId,Pageable pageable);

}
