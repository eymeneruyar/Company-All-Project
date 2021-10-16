package companyAll_MVC.repositories._jpa;

import companyAll_MVC.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    @Procedure(procedureName = "ProcDeleteChosenImage")
    void deleteImageByFileName(@Param("filename") String filename);

}
