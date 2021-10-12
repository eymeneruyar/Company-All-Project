package companyAll_MVC.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Data
public class Announcement extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @NotNull(message = "Invalid announcement no! (null)")
    @NotEmpty(message = "Invalid announcement no! (empty)")
    @Pattern(regexp="(^$|[0-9]{10})")
    private String no;

    @NotNull(message = "Invalid announcement date! (null)")
    @NotEmpty(message = "Invalid announcement date! (empty)")
    private String date;

    @NotNull(message = "Invalid announcement status! (null)")
    @NotEmpty(message = "Invalid announcement status! (empty)")
    @Pattern(regexp = "Active|Passive", message = "Announcement status must be either 'Active' or 'Passive'")
    private String status;

    @Size(max = 500, message = "Announcement detail's size can be 500 at max!")
    @NotNull(message = "Invalid announcement detail! (null)")
    @NotEmpty(message = "Invalid announcement detail! (empty)")
    private String detail;

}
