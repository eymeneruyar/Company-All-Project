package companyAll_MVC.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@ApiModel(value = "Likes Product Model", description = "Likes Product Model Variable Definitions")
public class LikesProduct extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @ApiModelProperty(value = "Likes Product Id",required = true)
    private Integer id;

    @ApiModelProperty(value = "Total like for each a product",required = true)
    private Integer totalLike;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "productId")
    private Product product;
}
