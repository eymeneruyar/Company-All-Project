package companyAll_MVC.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
@ApiModel(value = "City Model", description = "City Model Variable Definitions")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "City Id",required = true)
    private Integer id;

    @Column(unique = true)
    @NotNull(message = "Invalid city name! (null)")
    @NotEmpty(message = "Invalid city name! (empty)")
    @ApiModelProperty(value = "City name",required = true)
    private String name;

    @OneToOne
    @Valid
    private Country country;

}
