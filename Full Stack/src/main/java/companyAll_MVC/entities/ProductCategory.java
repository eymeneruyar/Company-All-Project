package companyAll_MVC.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Entity
public class ProductCategory extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(unique = true)
    @Pattern(regexp="(^$|[0-9]{10})", message = "Invalid product category no!")
    private String no;

    @Column(unique = true)
    @NotNull(message = "Product category name is not null!")
    @NotEmpty(message = "Product category name is not empty!")
    @Length(min = 2,max = 255,message = "The product category name can have a minimum of 2, a maximum of 255 characters!")
    private String name;

    @Column(columnDefinition = "text")
    @NotNull(message = "Product category details is not null!")
    @NotEmpty(message = "Product category details is not empty!")
    @Length(min = 2,message = "The product category details can have a minimum of 2 characters!")
    private String details;


    @NotNull(message = "The product category status is not null!")
    @NotEmpty(message = "The product category status is not empty!")
    private String status;

    private String date;

}
