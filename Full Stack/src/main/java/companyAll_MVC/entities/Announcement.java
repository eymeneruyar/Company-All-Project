package companyAll_MVC.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Entity
@ApiModel(value = "Announcment Model", description = "Announcment Model Variable Definitions")
public class Announcement extends AuditEntity<String>{

    @Id
    @ApiModelProperty(value = "Announcment Id",required = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @NotNull(message = "Invalid announcement no! (null)")
    @NotEmpty(message = "Invalid announcement no! (empty)")
    @ApiModelProperty(value = "Announcment ten digits no",required = true)
    @Pattern(regexp="(^$|[0-9]{10})", message = "Invalid announcement no!")
    private String no;

    @Column(unique = true)
    @Size(max = 50, message = "Announcement title's size can be 50 at max!")
    @NotNull(message = "Invalid announcement title! (null)")
    @NotEmpty(message = "Invalid announcement title! (empty)")
    @ApiModelProperty(value = "Announcment title",required = true)
    private String title;

    @ApiModelProperty(value = "Announcment save date",required = true)
    private String date;

    @NotNull(message = "Invalid announcement status! (null)")
    @NotEmpty(message = "Invalid announcement status! (empty)")
    @ApiModelProperty(value = "Announcment status (Active or Passive)",required = true)
    @Pattern(regexp = "Active|Passive", message = "Announcement status must be either 'Active' or 'Passive'")
    private String status;

    @Size(max = 500, message = "Announcement detail's size can be 500 at max!")
    @NotNull(message = "Invalid announcement detail! (null)")
    @NotEmpty(message = "Invalid announcement detail! (empty)")
    @ApiModelProperty(value = "Announcment detail",required = true)
    private String detail;

}
