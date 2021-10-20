package companyAll_MVC.repositories._jpa;

import companyAll_MVC.entities.CustomerTrash;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerTrashRepository extends JpaRepository<CustomerTrash, Integer> {
}
