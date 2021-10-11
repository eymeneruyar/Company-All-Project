package companyAll_MVC.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditEntity<T> {

    @CreatedBy
    @Column(name = "created_By")
    private T createdBy;

    @CreatedDate
    @Column(name = "created_Date")
    private Date createdDate;

    @LastModifiedBy
    @Column(name = "last_Modified_By")
    private T lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_Modified_Date")
    private Date lastModifiedDate;

}
