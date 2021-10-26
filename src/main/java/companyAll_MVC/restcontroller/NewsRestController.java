package companyAll_MVC.restcontroller;

import companyAll_MVC.dto.NewsDto;
import companyAll_MVC.repositories._jpa.NewsCategoryRepository;
import companyAll_MVC.repositories._jpa.NewsRepository;
import companyAll_MVC.utils.Check;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/news")
//@Api(value = "NewRestController",authorizations = {@Authorization(value = "basicAuth")})
public class NewsRestController {

    final NewsDto newsDto;
    final NewsRepository newsRepository;
    final NewsCategoryRepository newsCategoryRepository;

    public NewsRestController(NewsDto newsDto, NewsRepository newsRepository, NewsCategoryRepository newsCategoryRepository) {
        this.newsDto = newsDto;
        this.newsRepository = newsRepository;
        this.newsCategoryRepository = newsCategoryRepository;
    }

    //news-category list
    @GetMapping("/listCategory")
    public Map<Check, Object> list() {
        return newsDto.list();
    }

    //newsListByCategoryId
    @GetMapping("/listNews/{id}")
    public Map<Check, Object> listByCategoryId(@PathVariable Integer id) {
        return newsDto.listByCategoryId(id);
    }


}
