package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "SEC_USER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SecUser extends AbstractEntity implements UserDetails{

    @Id
    @GeneratedValue(generator = "SEC_USER_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "SEC_USER_ID_SEQ", name = "SEC_USER_ID", allocationSize = 1)
    protected Long id;

    @Column(name = "USERNAME", nullable = false, unique = true)
    protected String username;

    @Column(name = "PASSWORD", nullable = false)
    protected String password;

    @Column(name = "EMAIL", unique = true)
    protected String email;

    @Column(name = "FIRST_NAME", nullable = false)
    protected String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    protected String lastName;

    @Column(name = "ACTIVE", nullable = false)
    protected boolean active = false;

    @Column(name = "CONFIRMED_TS")
    protected LocalDateTime confirmedTs;

    @Column(name = "CONFIRMED_BY")
    protected String confirmedBy;

    @Column(name = "CONFIRMATION_ID", nullable = false, unique = true)
    protected UUID confirmationId = UUID.randomUUID();

    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "SEC_USER_ROLES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    protected Set<SecRole> roles;

    public SecUser(Long id,
                   String username,
                   String password,
                   boolean active,
                   Set<SecRole> roles,
                   String email,
                   String firstName,
                   String lastName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.active = active;
        this.roles = roles;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void addRole(SecRole role){
        roles.add(role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
