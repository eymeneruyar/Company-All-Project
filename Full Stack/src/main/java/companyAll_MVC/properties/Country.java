package companyAll_MVC.properties;

import lombok.Data;

import java.util.List;

@Data
public class Country {
    private String country;
    private List<String> states;
}
