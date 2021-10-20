package companyAll_MVC.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class LikesProduct extends AuditEntity<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    private Integer totalLike;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "productId")
    private Product product;
}
