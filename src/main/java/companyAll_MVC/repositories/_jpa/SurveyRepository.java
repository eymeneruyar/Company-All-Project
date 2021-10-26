package companyAll_MVC.repositories._jpa;

import companyAll_MVC.entities.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey,Integer> {
}
