package companyAll_MVC.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "indents")
@Data
public class ElasticIndent {
    @Id
    private String id;

    @Field(type = FieldType.Auto)
    private Integer iid;

    @Field(type = FieldType.Auto)
    private Integer cid;

    @Field(type = FieldType.Auto)
    private Integer pid;

    @Field(type = FieldType.Text)
    private String no;

    @Field(type = FieldType.Text)
    private String cno;

    @Field(type = FieldType.Text)
    private String pno;

    @Field(type = FieldType.Text)
    private String cname;

    @Field(type = FieldType.Text)
    private String date;

    @Field(type = FieldType.Text)
    private String status;
}
