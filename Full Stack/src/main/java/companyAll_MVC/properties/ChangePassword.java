package companyAll_MVC.properties;

import lombok.Data;

@Data
public class ChangePassword {

    private String oldPassword;
    private String newPassword;
    private String reNewPassword;

}
