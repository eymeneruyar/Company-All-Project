package companyAll_MVC.repositories._jpa;

import companyAll_MVC.entities.ReferenceHolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReferenceHolderRepository extends JpaRepository<ReferenceHolder, Integer> {
    Optional<ReferenceHolder> findByReference(String reference);
    boolean existsByUid(Integer uid);

    Optional<ReferenceHolder> findByUid(Integer uid);

}
