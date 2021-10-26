package companyAll_MVC.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@ApiModel(value = "Product Model", description = "Product Model Variable Definitions")
public class Product extends AuditEntity<String>{

    @Id
    @ApiModelProperty(value = "Product Id",required = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(unique = true)
    @Pattern(regexp="(^$|[0-9]{10})", message = "Invalid product category no!")
    @ApiModelProperty(value = "Product ten digit No",required = true)
    private String no;

    @NotNull(message = "Product name is not null!")
    @NotEmpty(message = "Product name is not empty!")
    @ApiModelProperty(value = "Product name",required = true)
    @Length(min = 2,max = 255,message = "The product name can have a minimum of 2, a maximum of 255 characters!")
    private String name;

    @NotNull(message = "Product description is not null!")
    @NotEmpty(message = "Product description is not empty!")
    @ApiModelProperty(value = "Product brief summary",required = true)
    @Length(min = 2,max = 255,message = "The product description can have a minimum of 2, a maximum of 255 characters!")
    private String description;

    @ApiModelProperty(value = "Product price",required = true)
    @DecimalMin(value = "0.0", inclusive = false, message = "Product price must be higher than 0.0")
    private BigDecimal price;

    @Column(columnDefinition = "text")
    @NotNull(message = "Product details is not null!")
    @NotEmpty(message = "Product details is not empty!")
    @ApiModelProperty(value = "Product details",required = true)
    @Length(min = 2,message = "The product details can have a minimum of 2 characters!")
    private String details;

    @ApiModelProperty(value = "Product save date",required = true)
    private String date;

    @NotNull(message = "Invalid product status! (null)")
    @NotEmpty(message = "Invalid product status! (empty)")
    @ApiModelProperty(value = "Product status",required = true)
    @Pattern(regexp = "Available|Unavailable", message = "Product status must be either 'Available' or 'Unavailable'")
    private String status;

    @ElementCollection(fetch = FetchType.EAGER)
    @ApiModelProperty(value = "Product image file name",required = true)
    private List<String> fileName;

    @Column(columnDefinition = "Double")
    @ApiModelProperty(value = "Product total like value",required = true)
    private Double totalLike;

    @NotNull(message = "Status is not null!")
    @NotEmpty(message = "Status is not empty!")
    @ApiModelProperty(value = "Product campaign status",required = true)
    private String campaignStatus; //yes or no

    @ApiModelProperty(value = "Product campaign name",required = true)
    private String campaignName;

    @ApiModelProperty(value = "Product campaign details",required = true)
    private String campaignDetails;

    @LazyCollection(LazyCollectionOption.FALSE) //Filename ve ManyToMany aynı fetch.EAGER yemiyor, bu yüzden buradaki fetch'i LazyCollection kullanarak iptal ettik.
    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "product_product_categories",joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "product_categories_id"))
    private List<ProductCategory> productCategories;


}
