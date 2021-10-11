package companyAll_MVC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "companyAll_MVC.repositories._elastic")
public class CompanyAllMvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompanyAllMvcApplication.class, args);
    }

}
