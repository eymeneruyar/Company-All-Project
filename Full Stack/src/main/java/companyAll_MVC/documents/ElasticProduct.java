package companyAll_MVC.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.List;

@Data
@Document(indexName = "product")
public class ElasticProduct {

    @Id
    private String id;

    private Integer productId;

    @Field(type = FieldType.Text)
    private String no;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String description;

    private BigDecimal price;

    @Field(type = FieldType.Text)
    private String details;

    @Field(type = FieldType.Text)
    private String date;

    @Field(type = FieldType.Text)
    private String status;

    private List<String> fileName;

    private Double totalLike;

    @Field(type = FieldType.Text)
    private String campaignStatus; //yes or no

    @Field(type = FieldType.Text)
    private String campaignName;

    @Field(type = FieldType.Text)
    private String campaignDetails;

    private List<Integer> productCategoryId;

}
