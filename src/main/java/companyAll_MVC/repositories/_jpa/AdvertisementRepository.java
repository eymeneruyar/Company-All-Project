package companyAll_MVC.repositories._jpa;

import companyAll_MVC.entities.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {
    Optional<Advertisement> findByName(String name);
}
