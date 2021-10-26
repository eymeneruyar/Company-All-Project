package companyAll_MVC.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.math.BigDecimal;
import java.util.List;

@Data
@RedisHash("products")
public class RedisProduct {

    @Id
    private String id;
    private Integer productId;
    private String no;
    private String name;
    private String description;
    private BigDecimal price;
    private String details;
    private String date;
    private String status;
    private List<String> fileName;
    private String campaignStatus; //yes or no
    private String campaignName;
    private String campaignDetails;
    @Indexed
    private List<Integer> productCategoryId;

}
