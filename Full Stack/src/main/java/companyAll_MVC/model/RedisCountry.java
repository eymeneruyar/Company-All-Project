package companyAll_MVC.model;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@RedisHash("country")
@Data
public class RedisCountry {
    private String id;
    private String name;
    private List<String> cities;
}
