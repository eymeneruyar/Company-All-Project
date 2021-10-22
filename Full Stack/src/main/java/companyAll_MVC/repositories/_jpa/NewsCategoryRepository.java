package companyAll_MVC.repositories._jpa;

import companyAll_MVC.entities.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsCategoryRepository extends JpaRepository<NewsCategory,Integer > {
}
