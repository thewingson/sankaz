package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

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
    @SequenceGenerator(sequenceName = "SEC_ROLE_ID_SEQ", name = "SEC_ROLE_ID", allocationSize = 1)
    protected Long id;

    @Column(name = "NAME", nullable = false, unique = true)
    protected String name;

    @Override
    public String getAuthority() {
        return getName();
    }
}
