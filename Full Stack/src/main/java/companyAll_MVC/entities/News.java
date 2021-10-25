package companyAll_MVC.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Locale;

@Data
@Entity
@ApiModel(value = "News Model", description = "News Model Variable Definitions")
public class News extends AuditEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @ApiModelProperty(value = "News Id",required = true)
    private Integer id;

    @Column(unique = true)
    @ApiModelProperty(value = "News no",required = true)
    private Long no;

    @Column(unique = true)
    @NotNull(message = "News title is not null!")
    @NotEmpty(message = "News title is not empty!")
    @ApiModelProperty(value = "News title",required = true)
    @Length(min = 2, max = 255, message = "News title length has min 2 and max 255 character!")
    private String title;

    @NotNull(message = "News summary is not null!")
    @NotEmpty(message = "News summary is not empty!")
    @ApiModelProperty(value = "News summary",required = true)
    @Length(min = 2, max = 255, message = "News summary length has min 2 and max 255 character!")
    private String summary;

    @Column(columnDefinition = "text")
    @NotNull(message = "News description is not null!")
    @NotEmpty(message = "News description is not empty!")
    @ApiModelProperty(value = "News details",required = true)
    @Length(min = 2, message = "News description length has min 2 character!")
    private String description;

    @ApiModelProperty(value = "News status (Active or Passive)",required = true)
    private String status;

    @ApiModelProperty(value = "News save date",required = true)
    private String date;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> fileName;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="categoryId")
    private NewsCategory newsCategory;

}
