package companyAll_MVC.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
public class Product extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @NotNull(message = "Invalid product no! (null)")
    @NotEmpty(message = "Invalid product no! (empty)")
    @Pattern(regexp="(^$|[0-9]{10})")
    private String no;

    @Size(max = 50, message = "Product name's size can be 50 at max!")
    @NotNull(message = "Invalid product name! (null)")
    @NotEmpty(message = "Invalid product name! (empty)")
    private String name;

    @Size(max = 50, message = "Product description's size can be 50 at max!")
    @NotNull(message = "Invalid product description! (null)")
    @NotEmpty(message = "Invalid product description! (empty)")
    private String description;

    @Size(max = 500, message = "Product detail's size can be 500 at max!")
    @NotNull(message = "Invalid product detail! (null)")
    @NotEmpty(message = "Invalid product detail! (empty)")
    private String detail;

    @DecimalMin(value = "0.0", inclusive = false, message = "Product price must be higher than 0.0")
    private BigDecimal price;

    @ElementCollection
    @NotNull(message = "Invalid product images! (null)")
    @NotEmpty(message = "Invalid product images! (empty)")
    private List<String> images;

    @NotNull(message = "Invalid product date! (null)")
    @NotEmpty(message = "Invalid product date! (empty)")
    private String date;

    @NotNull(message = "Invalid product status! (null)")
    @NotEmpty(message = "Invalid product status! (empty)")
    @Pattern(regexp = "Available|Unavailable", message = "Product status must be either 'Available' or 'Unavailable'")
    private String status;

    @ManyToMany
    private List<@Valid ProductCategory> categories;
}
