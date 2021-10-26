package companyAll_MVC.controllers;

import com.sun.org.apache.xpath.internal.operations.Mod;
import companyAll_MVC.entities.News;
import companyAll_MVC.repositories._elastic.ElasticNewsCategoryRepository;
import companyAll_MVC.repositories._elastic.ElasticNewsRepository;
import companyAll_MVC.repositories._jpa.NewsCategoryRepository;
import companyAll_MVC.repositories._jpa.NewsRepository;
import companyAll_MVC.utils.Util;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

@Controller
@RequestMapping("/newsDetail")
public class NewsDetailController {

    final NewsRepository newsRepository;
    final NewsCategoryRepository newsCategoryRepository;
    final ElasticNewsRepository  elasticNewsRepository;
    final ElasticNewsCategoryRepository elasticNewsCategoryRepository;

    public NewsDetailController(NewsRepository newsRepository, NewsCategoryRepository newsCategoryRepository, ElasticNewsRepository elasticNewsRepository, ElasticNewsCategoryRepository elasticNewsCategoryRepository) {
        this.newsRepository = newsRepository;
        this.newsCategoryRepository = newsCategoryRepository;
        this.elasticNewsRepository = elasticNewsRepository;
        this.elasticNewsCategoryRepository = elasticNewsCategoryRepository;
    }

    @GetMapping("")
    public String newsDetail(){
        return "newsDetail";
    }

    @GetMapping("/{stId}")
    public String newsDetails(@PathVariable String stId, Model model){

        try {
            int id = Integer.parseInt(stId);
            News news = newsRepository.findById(id).get();
            model.addAttribute("detail",news);
            //System.out.println(product);
        } catch (Exception e) {
            System.err.println(e);
            return "redirect:/newsList";
        }
        return "newsDetail";
    }

    @GetMapping("/get_image/id={id}name={name}")
    public void getImage(@PathVariable Integer id, @PathVariable String name, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("image/jpeg; image/jpg; image/png");
        File file = new File(Util.UPLOAD_DIR_NEWS + id + "/" + name);
        if(file.exists()){
            byte[] fileContent = FileUtils.readFileToByteArray(file);
            response.getOutputStream().write(fileContent);
        }else{
            File defaultFile = new File(Util.UPLOAD_DIR_PRODUCTS + "default_image.jpg");
            byte[] defaultFileContent = FileUtils.readFileToByteArray(defaultFile);
            response.getOutputStream().write(defaultFileContent);
        }
        response.getOutputStream().close();
    }

}
