package companyAll_MVC.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Entity
@ApiModel(value = "Product Category Model", description = "Product Category Model Variable Definitions")
public class ProductCategory extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @ApiModelProperty(value = "Product category Id",required = true)
    private Integer id;

    @Column(unique = true)
    @ApiModelProperty(value = "Product category ten digit No",required = true)
    @Pattern(regexp="(^$|[0-9]{10})", message = "Invalid product category no!")
    private String no;

    @Column(unique = true)
    @NotNull(message = "Product category name is not null!")
    @NotEmpty(message = "Product category name is not empty!")
    @ApiModelProperty(value = "Product category name",required = true)
    @Length(min = 2,max = 255,message = "The product category name can have a minimum of 2, a maximum of 255 characters!")
    private String name;

    @Column(columnDefinition = "text")
    @NotNull(message = "Product category details is not null!")
    @NotEmpty(message = "Product category details is not empty!")
    @ApiModelProperty(value = "Product category details",required = true)
    @Length(min = 2,message = "The product category details can have a minimum of 2 characters!")
    private String details;


    @NotNull(message = "The product category status is not null!")
    @NotEmpty(message = "The product category status is not empty!")
    @ApiModelProperty(value = "Product category status. (Active or Passive)",required = true)
    private String status;

    @ApiModelProperty(value = "Product category save date.",required = true)
    private String date;

}
