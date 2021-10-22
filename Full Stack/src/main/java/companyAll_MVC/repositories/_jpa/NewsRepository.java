package companyAll_MVC.repositories._jpa;

import companyAll_MVC.entities.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewsRepository extends JpaRepository<News,Integer > {

    @Procedure(procedureName = "NewsDeleteChoseImage")
    void deleteImageByFileName(@Param("filename") String filename);

    Page<News> findByNewsCategory_Id(Integer id, Pageable pageable);


}
