package companyAll_MVC.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Data
public class ProductCategory extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @NotNull(message = "Invalid category no! (null)")
    @NotEmpty(message = "Invalid category no! (empty)")
    @Pattern(regexp="(^$|[0-9]{10})")
    private String no;

    @Size(max = 50, message = "Product category name's size can be 50 at max!")
    @NotNull(message = "Invalid product category name! (null)")
    @NotEmpty(message = "Invalid product category name! (empty)")
    private String name;

    @Size(max = 500, message = "Product category detail's size can be 500 at max!")
    @NotNull(message = "Invalid product category detail! (null)")
    @NotEmpty(message = "Invalid product category detail! (empty)")
    private String detail;

    @NotNull(message = "Invalid product category status! (null)")
    @NotEmpty(message = "Invalid product category status! (empty)")
    @Pattern(regexp = "Active|Passive", message = "Product category status must be either 'Active' or 'Passive'")
    private String status;

}
