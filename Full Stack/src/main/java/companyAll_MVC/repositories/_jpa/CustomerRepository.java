package companyAll_MVC.repositories._jpa;

import companyAll_MVC.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByMail(String mail);

    Optional<Customer> findByPhone1(String phone1);

    Optional<Customer> findByTaxno(String taxno);

}
