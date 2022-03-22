package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SEC_USER_TOKEN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SecUserToken extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "JWT_BLACK_LIST_SEQ", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "JWT_BLACK_LIST_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "JWT_BLACK_LIST_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Column(name = "ACCESS_TOKEN", nullable = false)
    private String accessToken;

    @Column(name = "IS_BLOCKED", nullable = false)
    private Boolean isBlocked = false;

    @Column(name = "BLOCK_DATE")
    private LocalDateTime blockDate;

    @Column(name = "EXPIRE_DATE", nullable = false)
    private LocalDateTime expireDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "TOKEN_USER_FK"))
    @JsonManagedReference
    private SecUser user;

}
