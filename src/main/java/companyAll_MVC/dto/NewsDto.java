package companyAll_MVC.dto;

import companyAll_MVC.entities.Gallery;
import companyAll_MVC.entities.GalleryCategory;
import companyAll_MVC.entities.News;
import companyAll_MVC.entities.NewsCategory;
import companyAll_MVC.repositories._elastic.ElasticNewsCategoryRepository;
import companyAll_MVC.repositories._jpa.NewsCategoryRepository;
import companyAll_MVC.repositories._jpa.NewsRepository;
import companyAll_MVC.utils.Check;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewsDto {

    final ElasticNewsCategoryRepository elasticNewsCategoryRepository;
    final NewsCategoryRepository newsCategoryRepository;
    final NewsRepository newsRepository;


    public NewsDto(ElasticNewsCategoryRepository elasticNewsCategoryRepository, NewsCategoryRepository newsCategoryRepository, NewsRepository newsRepository) {
        this.elasticNewsCategoryRepository = elasticNewsCategoryRepository;
        this.newsCategoryRepository = newsCategoryRepository;
        this.newsRepository = newsRepository;
    }

    // news-category list
    public Map<Check, Object> list() {
        Map<Check, Object> hm = new LinkedHashMap<>();
        hm.put(Check.status, true);
        List<NewsCategory> ls = newsCategoryRepository.findAll();
        hm.put(Check.result, ls);
        return hm;
    }

    // newsListByCategoryId
    public Map<Check, Object> listByCategoryId(Integer id) {
        Map<Check, Object> hm = new LinkedHashMap<>();
        hm.put(Check.status, true);
        List<News> ls = newsRepository.findByNewsCategory_IdEquals(id);
        hm.put(Check.result, ls);
        return hm;
    }

}
