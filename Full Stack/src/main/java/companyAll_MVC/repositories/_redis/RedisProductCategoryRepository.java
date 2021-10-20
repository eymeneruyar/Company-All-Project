package companyAll_MVC.repositories._redis;

import companyAll_MVC.model.RedisProductCategory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

@EnableRedisRepositories
public interface RedisProductCategoryRepository extends CrudRepository<RedisProductCategory,String> {
}
