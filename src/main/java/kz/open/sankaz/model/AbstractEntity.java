package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractEntity extends BaseEntity {

    @Column(name = "CREATE_TS", nullable = false)
    protected LocalDateTime createTs = LocalDateTime.now();

    @Column(name = "CREATED_BY", nullable = false)
    protected String createdBy;

    @Column(name = "UPDATE_TS")
    protected LocalDateTime updateTs;

    @Column(name = "UPDATED_BY")
    protected String updatedBy;

    @Column(name = "DELETE_TS")
    protected LocalDateTime deleteTs;

    @Column(name = "DELETED_BY")
    protected String deletedBy;
}
