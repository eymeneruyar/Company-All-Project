package companyAll_MVC.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Entity
@ApiModel(value = "Customer Model", description = "Customer Model Variable Definitions")
public class Customer extends AuditEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "Customer Id",required = true)
    private Integer id;

    @Column(unique = true)
    @NotNull(message = "Invalid customer no! (null)")
    @NotEmpty(message = "Invalid customer no! (empty)")
    @Pattern(regexp="(^$|[0-9]{10})")
    @ApiModelProperty(value = "Customer ten digits no",required = true)
    private String no;

    @Size(max = 50, message = "Customer name's size can be 50 at max!")
    @NotNull(message = "Invalid customer name! (null)")
    @NotEmpty(message = "Invalid customer name! (empty)")
    @ApiModelProperty(value = "Customer name",required = true)
    private String name;

    @Size(max = 50, message = "Customer surname's size can be 50 at max!")
    @NotNull(message = "Invalid customer surname! (null)")
    @NotEmpty(message = "Invalid customer surname! (empty)")
    @ApiModelProperty(value = "Customer surname",required = true)
    private String surname;

    @Column(unique = true)
    @NotNull(message = "Invalid customer phone number(phone1)! (null)")
    @NotEmpty(message = "Invalid customer phone number(phone1)! (empty)")
    @Pattern(regexp="(^$|[0-9]{10})")
    @ApiModelProperty(value = "Customer phone1 (###-###-####)",required = true)
    private String phone1;

    @Pattern(regexp="(^$|[0-9]{10})")
    @ApiModelProperty(value = "Customer phone2 (###-###-####)",required = true)
    private String phone2;

    @Column(unique = true)
    @Size(max = 50)
    @NotNull(message = "Invalid customer mail! (null)")
    @NotEmpty(message = "Invalid customer mail! (empty)")
    @Pattern(regexp="(^(.+)@(.+)$)")
    @ApiModelProperty(value = "Customer email",required = true)
    private String mail;

    @Column(unique = true)
    @NotNull(message = "Invalid customer tax no! (null)")
    @NotEmpty(message = "Invalid customer tax no! (empty)")
    @ApiModelProperty(value = "Customer tax no",required = true)
    @Pattern(regexp="(^$|[0-9]{11})")
    private String taxno;

    @NotNull(message = "Invalid customer country! (null)")
    @NotEmpty(message = "Invalid customer country! (empty)")
    @ApiModelProperty(value = "Customer country",required = true)
    private String country;

    @NotNull(message = "Invalid customer city! (null)")
    @NotEmpty(message = "Invalid customer city! (empty)")
    @ApiModelProperty(value = "Customer city",required = true)
    private String city;

    @OneToMany
    private List<@Valid Address> addresses;

    @NotNull(message = "Invalid customer status! (null)")
    @NotEmpty(message = "Invalid customer status! (empty)")
    @ApiModelProperty(value = "Customer status (Active or Passive)",required = true)
    @Pattern(regexp = "Active|Passive", message = "Customer status must be either 'Active' or 'Passive'")
    private String status;

}

