package companyAll_MVC.documents;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Data
@Document(indexName = "likes")
public class ElasticLikes {

    @Id
    private String id;

    private Integer likesId;

    @Field(type = FieldType.Text)
    private String customerName;

    @Field(type = FieldType.Text)
    private String productName;

    @Field(type = FieldType.Text)
    private Integer productRating;

    @Field(type = FieldType.Text)
    private String productDetail;

    @Field(type = FieldType.Text)
    private String productNo;




}
