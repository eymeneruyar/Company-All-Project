package companyAll_MVC.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
@ApiModel(value = "Survey Model", description = "Survey Model Variable Definitions")
public class Survey {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "Survey Id",required = true)
    private Integer id;

    @Column(unique = true)
    @ApiModelProperty(value = "Survey no",required = true)
    private Long no;

    @Column(unique = true)
    @NotNull(message = "Please Enter Survey Title!")
    @NotEmpty(message = "Please Enter Survey Title!")
    @ApiModelProperty(value = "Survey title",required = true)
    @Length(min = 2,max = 50, message = "Title field must be between 2 characters and 50 characters!")
    private String title;

    @NotNull(message = "Please Enter Survey Detail!")
    @NotEmpty(message = "Please Enter Survey Detail!")
    @ApiModelProperty(value = "Survey details",required = true)
    @Length(min = 2,max = 50, message = "Title field must be between 2 characters and 50 characters!")
    private String detail;

    @ApiModelProperty(value = "Survey save date",required = true)
    private String date;

    @ApiModelProperty(value = "Survey status. (Active or Passive)",required = true)
    private String status="Active";

}
