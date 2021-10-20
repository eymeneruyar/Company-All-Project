package companyAll_MVC.documents;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Data
@Document(indexName = "surveysoption")
public class ElasticSurveyOption {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String no;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String vote;

    @Field(type = FieldType.Text)
    private String date;

}
