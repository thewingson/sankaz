package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @SequenceGenerator(sequenceName = "JWT_BLACK_LIST_ID_SEQ", name = "JWT_BLACK_LIST_ID", allocationSize = 1)
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
