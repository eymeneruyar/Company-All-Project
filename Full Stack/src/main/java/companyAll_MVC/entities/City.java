package companyAll_MVC.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @NotNull(message = "Invalid city name! (null)")
    @NotEmpty(message = "Invalid city name! (empty)")
    private String name;

    @OneToOne
    @Valid
    private Country country;

}
