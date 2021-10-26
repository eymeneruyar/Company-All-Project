package companyAll_MVC.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@ApiModel(value = "Customer Trash", description = "Customer Trash Model Variable Definitions")
public class CustomerTrash extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "Customer Trash Id",required = true)
    private Integer id;

    @ApiModelProperty(value = "Customer name",required = true)
    private String name;

    @ApiModelProperty(value = "Customer surname",required = true)
    private String surname;

    @ApiModelProperty(value = "Customer phone",required = true)
    private String phone;

    @ApiModelProperty(value = "Customer mail",required = true)
    private String mail;

}
