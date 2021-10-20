package companyAll_MVC.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("productsCategory")
public class RedisProductCategory {

    @Id
    private String id;

    private Integer categoryId;
    private String no;
    private String name;
    private String details;
    private String status;
    private String date;

}
