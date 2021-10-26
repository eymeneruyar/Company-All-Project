package companyAll_MVC.repositories._jpa;

import companyAll_MVC.entities.Indent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndentRepository extends JpaRepository<Indent,Integer> {
    List<Indent> findByCustomer_IdEquals(Integer id);

    List<Indent> findByProduct_IdEquals(Integer id);
}
