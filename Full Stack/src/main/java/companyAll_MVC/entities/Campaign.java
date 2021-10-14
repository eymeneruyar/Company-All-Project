package companyAll_MVC.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Campaign extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    private String name;

    private String details;

    private String date;

    private String status; //yes or no

}
