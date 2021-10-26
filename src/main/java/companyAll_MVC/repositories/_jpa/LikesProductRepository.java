package companyAll_MVC.repositories._jpa;

import companyAll_MVC.entities.LikesProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesProductRepository extends JpaRepository<LikesProduct,Integer> {
    List<LikesProduct> findByProduct_IdEquals(Integer id);

    long countByIdIsNotNull();

}
