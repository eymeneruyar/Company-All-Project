package companyAll_MVC.repositories._redis;

import companyAll_MVC.model.RedisCountry;
import org.springframework.data.repository.CrudRepository;

public interface RedisCountryRepository extends CrudRepository<RedisCountry, String> {
}
