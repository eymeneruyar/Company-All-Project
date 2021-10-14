package companyAll_MVC.repositories._jpa;

import companyAll_MVC.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
}
