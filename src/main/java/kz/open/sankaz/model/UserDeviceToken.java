package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "USER_DEVICE_TOKEN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDeviceToken extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "USER_DEVICE_TOKEN_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "USER_DEVICE_TOKEN_ID_SEQ", name = "USER_DEVICE_TOKEN_ID", allocationSize = 1)
    private Long id;

    @Column(name = "TOKEN", nullable = false, unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "TOKEN_USER_FK"), nullable = false)
    @JsonManagedReference
    private SecUser user;

}
