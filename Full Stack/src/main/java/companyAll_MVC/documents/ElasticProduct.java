package companyAll_MVC.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

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
    private String date;

    @Field(type = FieldType.Text)
    private String status;

}
