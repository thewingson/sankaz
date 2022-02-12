package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class Message extends AbstractEntity {

    @Column(name = "TEXT", nullable = false)
    protected String text;

    @Column(name = "MESSAGE_DATE", nullable = false)
    protected LocalDateTime messageDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "MESSAGE_USER_FK"), nullable = false)
    @JsonManagedReference
    protected SecUser user;

}
