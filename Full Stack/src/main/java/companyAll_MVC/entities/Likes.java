package companyAll_MVC.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Likes extends AuditEntity<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    private Integer rating;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "indentId")
    private Indent indent;


}
