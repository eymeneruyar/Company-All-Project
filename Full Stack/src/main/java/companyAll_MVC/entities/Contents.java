package companyAll_MVC.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Contents extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(unique = true)
    private String no;

    @Column(unique = true)
    @NotNull(message = "Contents title is not null!")
    @NotEmpty(message = "Contents title is not empty!")
    @Length(min = 2,max = 255,message = "Contents title length has min 2 and max 255 character!")
    private String title;

    @NotNull(message = "Contents description is not null!")
    @NotEmpty(message = "Contents description is not empty!")
    @Length(min = 2,max = 255,message = "Contents description length has min 2 and max 255 character!")
    private String description;

    @NotNull(message = "Contents status is not null!")
    @NotEmpty(message = "Contents status is not empty!")
    private String status;

    @Column(columnDefinition = "text")
    @NotNull(message = "Contents details is not null!")
    @NotEmpty(message = "Contents details is not empty!")
    @Length(min = 2,message = "Contents details length has min 2 character!")
    private String details;

    private String date;

}
