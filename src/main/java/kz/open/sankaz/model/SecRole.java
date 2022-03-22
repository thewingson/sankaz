package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "SEC_ROLE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SecRole extends AbstractEntity implements GrantedAuthority {
    @Id
    @GeneratedValue(generator = "SEC_ROLE_SEQ", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "SEC_ROLE_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "SEC_ROLE_SEQ"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Override
    public String getAuthority() {
        return getName();
    }
}
