package companyAll_MVC.repositories._jpa;

import companyAll_MVC.entities.Gallery;
import companyAll_MVC.entities.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface GalleryRepository extends JpaRepository<Gallery,Integer> {

    @Procedure(procedureName = "GalleryDeleteChoseImage")
    void deleteImageByFileName(@Param("filename") String filename);

    Page<Gallery> findByGalleryCategory_Id(Integer id, Pageable pageable);

}
