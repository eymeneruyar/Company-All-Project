package companyAll_MVC.repositories._jpa;

import companyAll_MVC.entities.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes,Integer> {

    List<Likes> findByIndent_IdEquals(Integer id);

    List<Likes> findByIndent_Customer_IdEquals(Integer id);

    List<Likes> findByIndent_Product_IdEquals(Integer id);

}
