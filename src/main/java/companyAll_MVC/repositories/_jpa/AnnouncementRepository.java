package companyAll_MVC.repositories._jpa;

import companyAll_MVC.entities.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {

    Optional<Announcement> findByTitle(String title);

    Page<Announcement> findByDateContainsIgnoreCaseOrderByIdAsc(String date, Pageable pageable);


}
