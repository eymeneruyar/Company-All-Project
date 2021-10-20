package companyAll_MVC.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "advertisements")
@Data
public class ElasticAdvertisement {

    @Id
    private String id;

    @Field(type = FieldType.Auto)
    private Integer aid;

    @Field(type = FieldType.Text)
    private String no;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Auto)
    private int view;

    @Field(type = FieldType.Auto)
    private int width;

    @Field(type = FieldType.Auto)
    private int height;

    @Field(type = FieldType.Auto)
    private Long click;

    @Field(type = FieldType.Text)
    private String status;
}
