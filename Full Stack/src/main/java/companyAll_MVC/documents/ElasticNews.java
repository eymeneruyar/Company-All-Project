package companyAll_MVC.documents;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.util.List;

@Data
@Document(indexName = "news")
public class ElasticNews {

    @Id
    private String id;

    private String categoryId;

    @Field(type = FieldType.Text)
    private String no;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String summary;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
    private String status;

    @Field(type = FieldType.Text)
    private String categoryName;

    @Field(type = FieldType.Text)
    private String date;

    private List<String> fileName;

    private List<Integer> newsCategoryId;

}
