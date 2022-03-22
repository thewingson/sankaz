package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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
    @GenericGenerator(
            name = "USER_DEVICE_TOKEN_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "USER_DEVICE_TOKEN_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Column(name = "TOKEN", nullable = false, unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "TOKEN_USER_FK"), nullable = false)
    @JsonManagedReference
    private SecUser user;

}
