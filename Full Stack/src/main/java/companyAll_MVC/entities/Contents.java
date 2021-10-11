package companyAll_MVC.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Contents extends AuditEntity<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(unique = true)
    private String no;

    @Column(unique = true)
    private String title;

    private String description;

    private String status;

    private String details;

    private String date;

}
