package companyAll_MVC.documents;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Data
@Document(indexName = "surveys")
public class ElasticSurvey {

    @Id
    private String id;

    private Integer surveyId;

    @Field(type = FieldType.Text)
    private String no;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String detail;

    @Field(type = FieldType.Text)
    private String status;

    @Field(type = FieldType.Text)
    private String date;
}
