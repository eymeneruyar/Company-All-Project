package companyAll_MVC.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Data
public class Address extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Invalid address type! (null)")
    @NotEmpty(message = "Invalid address type! (empty)")
    @Pattern(regexp = "Home|Work", message = "Address type must be either 'Home' or 'Work'")
    private String type;

    @Size(max = 200, message = "Address detail's size can be 200 at max!")
    @NotNull(message = "Invalid address detail! (null)")
    @NotEmpty(message = "Invalid address detail! (empty)")
    private String detail;
}

