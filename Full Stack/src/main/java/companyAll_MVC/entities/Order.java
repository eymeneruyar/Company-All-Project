package companyAll_MVC.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Data
public class Order extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @NotNull(message = "Invalid order no! (null)")
    @NotEmpty(message = "Invalid order no! (empty)")
    @Pattern(regexp="(^$|[0-9]{10})")
    private String no;

    @OneToOne
    @Valid
    private Customer customer;

    @OneToOne
    @Valid
    private Product product;

    @NotNull(message = "Invalid order date! (null)")
    @NotEmpty(message = "Invalid order date! (empty)")
    private String date;

    @OneToOne
    @Valid
    private Address address;

    @NotNull(message = "Invalid customer status! (null)")
    @NotEmpty(message = "Invalid customer status! (empty)")
    @Pattern(regexp = "Active|Delivered", message = "Order status must be either 'Active' or 'Delivered'")
    private String status;

}
