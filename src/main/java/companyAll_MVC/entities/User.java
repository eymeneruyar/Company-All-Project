package companyAll_MVC.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@ApiModel(value = "User Model", description = "User Model Variable Definitions")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @ApiModelProperty(value = "User Id",required = true)
    private Integer id;

    @Column(unique = true)
    @ApiModelProperty(value = "User no",required = true)
    private Long no;

    @NotNull(message = "Please Enter Your Name!")
    @NotEmpty(message = "Please Enter Your Name!")
    @ApiModelProperty(value = "User name",required = true)
    @Length(min = 2,max = 50, message = "Name field must be between 2 characters and 50 characters!")
    private String name;

    @NotNull(message = "Please Enter Your Surname!")
    @NotEmpty(message = "Please Enter Your Surname!")
    @ApiModelProperty(value = "User surname",required = true)
    @Length(min = 2,max = 50, message = "Surname field must be between 2 characters and 50 characters!")
    private String surname;

    @Column(unique = true)
    @NotNull(message = "Please Enter Your Email!")
    @NotEmpty(message = "Please Enter Your Email!")
    @ApiModelProperty(value = "User email",required = true)
    @Length(min = 11,max = 50, message = "Surname field must be between 2 characters and 50 characters!")
    private String email;

    @NotNull(message = "Please Enter Your Password!")
    @NotEmpty(message = "Please Enter Your Password!")
    @ApiModelProperty(value = "User password",required = true)
    private String password;

    @Column(unique = true)
    @NotNull(message = "Please Enter Your Company Name!")
    @NotEmpty(message = "Please Enter Your Company Name!")
    @ApiModelProperty(value = "User company name",required = true)
    @Length(min = 2,max = 100, message = "Company Name field must be between 2 characters and 100 characters!")
    private String companyName;

    @NotNull(message = "Please Enter Your Company Address!")
    @NotEmpty(message = "Please Enter Your Company Address!")
    @ApiModelProperty(value = "User company address",required = true)
    @Length(min = 10,max = 200, message = "Company Address field must be between 10 characters and 200 characters!")
    private String companyAddress;

    @ApiModelProperty(value = "User biography",required = true)
    private String bio;

    @ApiModelProperty(value = "User birth date",required = true)
    private String birthday;

    @ApiModelProperty(value = "User company sector",required = true)
    private String companySector;

    @ApiModelProperty(value = "User profile image",required = true)
    private String profileImage;

    @Column(unique = true)
    @NotNull(message = "Please Enter Your Company Phone!")
    @NotEmpty(message = "Please Enter Your Company Phone!")
    @ApiModelProperty(value = "User company phone",required = true)
    @Length(min = 1,max = 13, message = "Company Phone must be in the format +90(xxx)-xxx-xx-xx!")
    private String companyPhone;

    private boolean enabled;
    private boolean tokenExpired;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private List<Role> roles;

}
