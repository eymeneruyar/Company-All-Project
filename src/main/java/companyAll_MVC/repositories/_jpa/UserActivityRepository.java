package companyAll_MVC.repositories._jpa;

import companyAll_MVC.entities.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityRepository extends JpaRepository<UserActivity,Integer> {
}
