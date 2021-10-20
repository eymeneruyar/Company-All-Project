package companyAll_MVC.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Entity
@Data
public class Indent extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @NotNull(message = "Invalid indent no! (null)")
    @NotEmpty(message = "Invalid indent no! (empty)")
    @Pattern(regexp="(^$|[0-9]{10})", message = "Invalid indent no!")
    private String no;

    @OneToOne
    @Valid
    private Customer customer;

    @OneToOne
    @Valid
    private Product product;

    @NotNull(message = "Invalid indent date! (null)")
    @NotEmpty(message = "Invalid indent date! (empty)")
    private String date;

    @Positive(message = "Address index should be positive number!")
    @NotNull(message = "Invalid address index! (null)")
    private int adressIndex;

    @NotNull(message = "Invalid indent status! (null)")
    @NotEmpty(message = "Invalid indent status! (empty)")
    @Pattern(regexp = "Active|Delivered", message = "Indent status must be either 'Active' or 'Delivered'")
    private String status;

    private boolean orderStatus=false;

}
