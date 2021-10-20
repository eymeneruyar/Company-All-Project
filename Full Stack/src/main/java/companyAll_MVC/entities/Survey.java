package companyAll_MVC.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(unique = true)
    private Long no;

    @Column(unique = true)
    @NotNull(message = "Please Enter Survey Title!")
    @NotEmpty(message = "Please Enter Survey Title!")
    @Length(min = 2,max = 50, message = "Title field must be between 2 characters and 50 characters!")
    private String title;

    @NotNull(message = "Please Enter Survey Detail!")
    @NotEmpty(message = "Please Enter Survey Detail!")
    @Length(min = 2,max = 50, message = "Title field must be between 2 characters and 50 characters!")
    private String detail;

    private String date;

    private String status="Active";

}
