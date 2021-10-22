package companyAll_MVC.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    private String name;
    private String surname;
    private String email;
    private String sessionId;
    private String ip;
    private String role;
    private String url;
    private String image;
    private LocalDateTime date;

}
