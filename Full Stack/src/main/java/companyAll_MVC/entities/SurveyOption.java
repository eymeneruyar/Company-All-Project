package companyAll_MVC.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@ApiModel(value = "Survey Option Model", description = "Survey Option Model Variable Definitions")
public class SurveyOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @ApiModelProperty(value = "Survey Option Id",required = true)
    private Integer id;

    @Column(unique = true)
    @ApiModelProperty(value = "Survey Option No",required = true)
    private Long no;


    @NotEmpty(message = "Please Enter Title!")
    @NotEmpty(message = "Please Enter Title!")
    @ApiModelProperty(value = "Survey Option title",required = true)
    @Length(min = 3, max = 50, message = "Title field must be between 2 characters and 50 characters!")
    private String title;

    @ApiModelProperty(value = "Survey Option vote",required = true)
    private Integer vote=0;

    @ApiModelProperty(value = "Survey Option save date",required = true)
    private String date;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="surveyId")
    private Survey survey;

}
