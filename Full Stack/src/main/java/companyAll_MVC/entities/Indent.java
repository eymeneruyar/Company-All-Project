package companyAll_MVC.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
@Entity
@ApiModel(value = "Indent Model", description = "Indent Model Variable Definitions")
public class Indent extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "Indent Id",required = true)
    private Integer id;

    @Column(unique = true)
    @NotNull(message = "Invalid indent no! (null)")
    @NotEmpty(message = "Invalid indent no! (empty)")
    @Pattern(regexp="(^$|[0-9]{10})", message = "Invalid indent no!")
    @ApiModelProperty(value = "Indent ten digits no",required = true)
    private String no;

    @OneToOne
    private Customer customer;

    @OneToOne
    private Product product;

    @NotNull(message = "Invalid indent date! (null)")
    @NotEmpty(message = "Invalid indent date! (empty)")
    @ApiModelProperty(value = "Indent save date",required = true)
    private String date;

    @NotNull(message = "Invalid address index! (null)")
    @ApiModelProperty(value = "Indent address index",required = true)
    private int adressIndex;

    @NotNull(message = "Invalid indent status! (null)")
    @NotEmpty(message = "Invalid indent status! (empty)")
    @ApiModelProperty(value = "Indent status (Active or Passive)",required = true)
    @Pattern(regexp = "Active|Delivered", message = "Indent status must be either 'Active' or 'Delivered'")
    private String status;

    @ApiModelProperty(value = "Indent indent status (true or false)",required = true)
    private boolean orderStatus=false;

}
