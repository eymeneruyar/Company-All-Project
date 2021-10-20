package companyAll_MVC.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
public class SurveyOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(unique = true)
    private Long no;


    @NotEmpty(message = "Please Enter Title!")
    @NotEmpty(message = "Please Enter Title!")
    @Length(min = 3, max = 50, message = "Title field must be between 2 characters and 50 characters!")
    private String title;

    private Integer vote=0;

    private String date;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="surveyId")
    private Survey survey;
}
