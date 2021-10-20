package companyAll_MVC.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "customers_min")
@Data
public class ElasticCustomerMin {

    @Id
    private String id;

    @Field(type = FieldType.Auto)
    private Integer cid;

    @Field(type = FieldType.Text)
    private String no;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String surname;

    @Field(type = FieldType.Text)
    private String status;

}
