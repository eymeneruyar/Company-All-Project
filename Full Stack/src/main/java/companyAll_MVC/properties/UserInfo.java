package companyAll_MVC.properties;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserInfo {

    @NotNull(message = "Please Enter Your Name!")
    @NotEmpty(message = "Please Enter Your Name!")
    @Length(min = 2,max = 50, message = "Name field must be between 2 characters and 50 characters!")
    private String name;

    @NotNull(message = "Please Enter Your Surname!")
    @NotEmpty(message = "Please Enter Your Surname!")
    @Length(min = 2,max = 50, message = "Surname field must be between 2 characters and 50 characters!")
    private String surname;

    @NotNull(message = "Please Enter Your Email!")
    @NotEmpty(message = "Please Enter Your Email!")
    @Length(min = 11,max = 50, message = "Surname field must be between 2 characters and 50 characters!")
    private String email;

    private String bio;

    private String birthday;

}
