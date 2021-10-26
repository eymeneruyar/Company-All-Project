package companyAll_MVC.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Entity
public class Gallery extends AuditEntity<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(unique = true)
    private Long no;

    @Column(unique = true)
    @NotNull(message = "Invalid Gallery name! (null)")
    @NotEmpty(message = "Invalid Gallery name! (empty)")
    @Length(min = 2, max = 255, message = "Gallery title length has min 2 and max 50 character!")
    private String title;

    @Length(min = 2, max = 255, message = "Gallery description length has min 2 and max 200 character!")
    private String description;

    private String status;

    private String date;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> fileName;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="categoryId")
    private GalleryCategory galleryCategory;
}
