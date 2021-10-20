package companyAll_MVC.repositories._redis;

import companyAll_MVC.model.RedisProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableRedisRepositories
public interface RedisProductRepository extends CrudRepository<RedisProduct,String> {

    Page<RedisProduct> findByProductCategoryIdEquals(Integer productCategoryId, Pageable pageable);

}
