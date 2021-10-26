package companyAll_MVC.repositories._jpa;

import companyAll_MVC.entities.SurveyOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyOptionRepository extends JpaRepository<SurveyOption,Integer > {

    List<SurveyOption> findBySurvey_IdEquals(Integer id);


}
