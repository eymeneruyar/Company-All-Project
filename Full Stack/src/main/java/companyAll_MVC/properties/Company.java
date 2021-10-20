package companyAll_MVC.properties;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Company {

    @NotNull(message = "Please Enter Your Company Name!")
    @NotEmpty(message = "Please Enter Your Company Name!")
    @Length(min = 2,max = 100, message = "Company Name field must be between 2 characters and 100 characters!")
    private String companyName;

    @NotNull(message = "Please Enter Your Company Address!")
    @NotEmpty(message = "Please Enter Your Company Address!")
    @Length(min = 10,max = 200, message = "Company Address field must be between 10 characters and 200 characters!")
    private String companyAddress;

    private String companySector;

    @NotNull(message = "Please Enter Your Company Phone!")
    @NotEmpty(message = "Please Enter Your Company Phone!")
    @Length(min = 1,max = 13, message = "Company Phone must be in the format +90(xxx)-xxx-xx-xx!")
    private String companyPhone;

}
