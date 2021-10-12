package companyAll_MVC.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Data
public class Advertisement extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @NotNull(message = "Invalid advertisement no! (null)")
    @NotEmpty(message = "Invalid advertisement no! (empty)")
    @Pattern(regexp="(^$|[0-9]{10})")
    private String no;

    @Size(max = 50, message = "Advertisement name's size can be 50 at max!")
    @NotNull(message = "Invalid advertisement name! (null)")
    @NotEmpty(message = "Invalid advertisement name! (empty)")
    private String name;

    @Positive(message = "Advertisement view should be positive number!")
    private int view;

    @NotNull(message = "Invalid advertisement start date! (null)")
    @NotEmpty(message = "Invalid advertisement start date! (empty)")
    private String startDate;

    @NotNull(message = "Invalid advertisement finish date! (null)")
    @NotEmpty(message = "Invalid advertisement finish date! (empty)")
    private String finishDate;

    @Positive(message = "Advertisement width should be positive number!")
    private int width;

    @Positive(message = "Advertisement height should be positive number!")
    private int height;

    @NotNull(message = "Invalid advertisement image name! (null)")
    @NotEmpty(message = "Invalid advertisement image name! (empty)")
    private String image;

    @NotNull(message = "Invalid advertisement link! (null)")
    @NotEmpty(message = "Invalid advertisement link! (empty)")
    private String link;

    private Long click;
}
