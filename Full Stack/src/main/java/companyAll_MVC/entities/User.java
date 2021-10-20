package companyAll_MVC.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(unique = true)
    private Long no;

    @NotNull(message = "Please Enter Your Name!")
    @NotEmpty(message = "Please Enter Your Name!")
    @Length(min = 2,max = 50, message = "Name field must be between 2 characters and 50 characters!")
    private String name;

    @NotNull(message = "Please Enter Your Surname!")
    @NotEmpty(message = "Please Enter Your Surname!")
    @Length(min = 2,max = 50, message = "Surname field must be between 2 characters and 50 characters!")
    private String surname;

    @Column(unique = true)
    @NotNull(message = "Please Enter Your Email!")
    @NotEmpty(message = "Please Enter Your Email!")
    @Length(min = 11,max = 50, message = "Surname field must be between 2 characters and 50 characters!")
    private String email;

    @NotNull(message = "Please Enter Your Password!")
    @NotEmpty(message = "Please Enter Your Password!")
    private String password;

    @Column(unique = true)
    @NotNull(message = "Please Enter Your Company Name!")
    @NotEmpty(message = "Please Enter Your Company Name!")
    @Length(min = 2,max = 100, message = "Company Name field must be between 2 characters and 100 characters!")
    private String companyName;

    @NotNull(message = "Please Enter Your Company Address!")
    @NotEmpty(message = "Please Enter Your Company Address!")
    @Length(min = 10,max = 200, message = "Company Address field must be between 10 characters and 200 characters!")
    private String companyAddress;

    private String bio;

    private String birthday;

    private String companySector;

    private String profileImage;

    @Column(unique = true)
    @NotNull(message = "Please Enter Your Company Phone!")
    @NotEmpty(message = "Please Enter Your Company Phone!")
    @Length(min = 1,max = 13, message = "Company Phone must be in the format +90(xxx)-xxx-xx-xx!")
    private String companyPhone;

    private boolean enabled;
    private boolean tokenExpired;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private List<Role> roles;

}
