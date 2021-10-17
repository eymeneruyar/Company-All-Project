package companyAll_MVC.repositories._jpa;

import companyAll_MVC.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    @Procedure(procedureName = "ProcDeleteChosenImage")
    void deleteImageByFileName(@Param("filename") String filename);

    Page<Product> findByProductCategories_Id(Integer id, Pageable pageable);

}
