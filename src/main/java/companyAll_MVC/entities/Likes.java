package companyAll_MVC.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Entity
@ApiModel(value = "Likes Model", description = "Likes Model Variable Definitions")
public class Likes extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @ApiModelProperty(value = "Likes Id",required = true)
    private Integer id;

    @Min(value = 0,message = "Rating value has to be minimum zero!")
    @Max(value = 5,message = "Rating value has to be maximum five!")
    @ApiModelProperty(value = "Likes Rating (Must be zero to five)",required = true)
    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "indentId")
    private Indent indent;


}
