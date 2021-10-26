package companyAll_MVC.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
@ApiModel(value = "Country Model", description = "Country Model Variable Definitions")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "Country Id",required = true)
    private Integer id;

    @Column(unique = true)
    @NotNull(message = "Invalid country name! (null)")
    @NotEmpty(message = "Invalid country name! (empty)")
    @ApiModelProperty(value = "Country name",required = true)
    private String name;

}
