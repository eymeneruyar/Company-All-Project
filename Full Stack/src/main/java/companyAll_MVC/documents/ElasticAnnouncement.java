package companyAll_MVC.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "announcements")
@Data
public class ElasticAnnouncement {
    @Id
    private String id;

    @Field(type = FieldType.Auto)
    private Integer aid;

    @Field(type = FieldType.Text)
    private String no;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String date;

    @Field(type = FieldType.Text)
    private String detail;

    @Field(type = FieldType.Text)
    private String status;
}
