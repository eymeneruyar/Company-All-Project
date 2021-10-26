package companyAll_MVC.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "productcategory")
public class ElasticProductCategory {

    @Id
    private String id;

    private Integer categoryId;

    @Field(type = FieldType.Text)
    private String no;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String details;

    @Field(type = FieldType.Text)
    private String date;

    @Field(type = FieldType.Text)
    private String status;

}
