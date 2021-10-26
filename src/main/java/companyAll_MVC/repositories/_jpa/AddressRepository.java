package companyAll_MVC.repositories._jpa;

import companyAll_MVC.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
