package companyAll_MVC.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@Entity
@ApiModel(value = "Advertisement Model", description = "Advertisement Model Variable Definitions")
public class Advertisement extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "Advertisement Id",required = true)
    private Integer id;

    @Column(unique = true)
    @NotNull(message = "Invalid advertisement no! (null)")
    @NotEmpty(message = "Invalid advertisement no! (empty)")
    @ApiModelProperty(value = "Advertisement ten digits no",required = true)
    @Pattern(regexp="(^$|[0-9]{10})", message = "Invalid advertisement no!")
    private String no;

    @Column(unique = true)
    @Size(max = 50, message = "Advertisement name's size can be 50 at max!")
    @NotNull(message = "Invalid advertisement name! (null)")
    @NotEmpty(message = "Invalid advertisement name! (empty)")
    @ApiModelProperty(value = "Advertisement name",required = true)
    private String name;

    @Positive(message = "Advertisement view should be positive number!")
    @ApiModelProperty(value = "Advertisement number of view",required = true)
    private int view;

    @NotNull(message = "Invalid advertisement start date! (null)")
    @NotEmpty(message = "Invalid advertisement start date! (empty)")
    @ApiModelProperty(value = "Advertisement date of start",required = true)
    private String startDate;

    @NotNull(message = "Invalid advertisement finish date! (null)")
    @NotEmpty(message = "Invalid advertisement finish date! (empty)")
    @ApiModelProperty(value = "Advertisement date of finish",required = true)
    private String finishDate;

    @Positive(message = "Advertisement width should be positive number!")
    @ApiModelProperty(value = "Advertisement image width",required = true)
    private int width;

    @Positive(message = "Advertisement height should be positive number!")
    @ApiModelProperty(value = "Advertisement image height",required = true)
    private int height;

    @ApiModelProperty(value = "Advertisement image file name",required = true)
    private String image;

    @NotNull(message = "Invalid advertisement link! (null)")
    @NotEmpty(message = "Invalid advertisement link! (empty)")
    @ApiModelProperty(value = "Advertisement web link",required = true)
    private String link;

    @Min(value = 0L, message = "Click count should be min 0!")
    @ApiModelProperty(value = "Advertisement number of click",required = true)
    private Long click;

    @NotNull(message = "Invalid advertisement status! (null)")
    @NotEmpty(message = "Invalid advertisement status! (empty)")
    @ApiModelProperty(value = "Advertisement status (Active or Passive)",required = true)
    @Pattern(regexp = "Active|Passive", message = "Advertisement status must be either 'Active' or 'Passive'")
    private String status;
}
