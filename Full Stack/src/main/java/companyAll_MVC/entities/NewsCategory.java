package companyAll_MVC.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class NewsCategory extends AuditEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(unique = true)
    private String no;

    @Column(unique = true)
    @NotNull(message = "Category name is not null!")
    @NotEmpty(message = "Category name is not empty!")
    @Length(min = 2, max = 255, message = "Category name length has min 2 and max 255 character!")
    private String name;

    @Length(max = 255, message = "Category detail length has min 2 and max 255 character!")
    private String detail;

    private String status;

    private String date;


}
