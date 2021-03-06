package companyAll_MVC.repositories._jpa;

import companyAll_MVC.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmailIgnoreCase(String email);

}
