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
@ApiModel(value = "News Category Model", description = "News Category Model Variable Definitions")
public class NewsCategory extends AuditEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @ApiModelProperty(value = "News Category Id",required = true)
    private Integer id;

    @Column(unique = true)
    @ApiModelProperty(value = "News Category no",required = true)
    private String no;

    @Column(unique = true)
    @NotNull(message = "Category name is not null!")
    @NotEmpty(message = "Category name is not empty!")
    @ApiModelProperty(value = "News Category name",required = true)
    @Length(min = 2, max = 255, message = "Category name length has min 2 and max 255 character!")
    private String name;

    @Length(max = 255, message = "Category detail length has min 2 and max 255 character!")
    @ApiModelProperty(value = "News Category detail",required = true)
    private String detail;

    @ApiModelProperty(value = "News Category status (Active or Passive)",required = true)
    private String status;

    @ApiModelProperty(value = "News Category save date",required = true)
    private String date;


}
