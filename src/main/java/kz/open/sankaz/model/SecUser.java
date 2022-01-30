package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
    private Long id;

    @Column(name = "USERNAME", unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "TEL_NUMBER", nullable = false, unique = true)
    private String telNumber;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "CITY")
    private String city;

    @Column(name = "ACTIVE", nullable = false)
    private boolean active = false;

    @Column(name = "LOGGED_OUT", nullable = false)
    private boolean loggedOut = false;

    @Column(name = "CONFIRMED_TS")
    private LocalDateTime confirmedTs;

    @Column(name = "CONFIRMED_BY")
    private String confirmedBy;

    @Column(name = "CONFIRMATION_NUMBER")
    private int confirmationNumber;

    @Column(name = "CONFIRMATION_STATUS")
    private String confirmationStatus = "NEW"; // NEW ON_CONFIRMATION CONFIRMED FINISHED

    @Column(name = "CONFIRMATION_NUMBER_CREATED_TS")
    private LocalDateTime confirmationNumberCreatedTs;

    @Column(name = "RESET_NUMBER")
    private int resetNumber;

    @Column(name = "RESET_NUMBER_CREATED_TS")
    private LocalDateTime resetNumberCreatedTs;

    @Column(name = "RESET_NUMBER_STATUS")
    private String resetNumberStatus; // EMPTY ON_RESET

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "SEC_USER_ROLES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private List<SecRole> roles;

    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @JoinColumn(name = "PIC_ID", foreignKey = @ForeignKey(name = "SEC_USER_PIC_FK"))
    @JsonManagedReference
    private ItemPic pic;

    public SecUser(Long id,
                   String username,
                   String password,
                   boolean active,
                   List<SecRole> roles,
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
        if(this.id == null){
            roles = new ArrayList<>();
        }
        roles.add(role);
    }

    public void clearRoles(){
        if(getRoles() != null){
            getRoles().clear();
        }
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
