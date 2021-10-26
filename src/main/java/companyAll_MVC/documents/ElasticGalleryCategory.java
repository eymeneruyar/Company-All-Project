package companyAll_MVC.documents;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Data
@Document(indexName = "gallerycategory")
public class ElasticGalleryCategory {

    @Id
    private String id;

    private Integer categoryId;

    @Field(type = FieldType.Text)
    private String no;

    @Field(type = FieldType.Text)
    private  String name;

    @Field(type = FieldType.Text)
    private String status;

}
