package companyAll_MVC.repositories._jpa;

import companyAll_MVC.entities.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign,Integer> {
}
