package companyAll_MVC.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "addresses")
@Data
public class ElasticAddress {

    @Id
    private String id;

    @Field(type = FieldType.Auto)
    private Integer aid;

    @Field(type = FieldType.Text)
    private String type;

    @Field(type = FieldType.Text)
    private String detail;

}
