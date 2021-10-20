package companyAll_MVC.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class CustomerTrash extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String surname;
    private String phone;
    private String mail;

}
