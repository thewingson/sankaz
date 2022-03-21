package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kz.open.sankaz.model.enums.ConfirmationStatus;
import kz.open.sankaz.model.enums.ResetNumberStatus;
import kz.open.sankaz.model.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@DynamicUpdate
@Table(name = "SEC_USER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class  SecUser extends AbstractEntity implements UserDetails{

    @Id
    @GeneratedValue(generator = "SEC_USER_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "SEC_USER_ID_SEQ", name = "SEC_USER_ID", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_TYPE", nullable = false)
    private UserType userType;

    @Column(name = "USERNAME", unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "GENDER_ID", foreignKey = @ForeignKey(name = "SEC_USER_GENDER_FK"))
    @JsonManagedReference
    private Gender gender;

    @Column(name = "TEL_NUMBER", nullable = false, unique = true)
    private String telNumber;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "CITY_ID", foreignKey = @ForeignKey(name = "SEC_USER_CITY_FK"))
    @JsonManagedReference
    private City city;

    @Column(name = "ACTIVE", nullable = false)
    private boolean active = false;

    @Column(name = "LOGGED_OUT", nullable = false)
    private boolean loggedOut = false;

    @Column(name = "CONFIRMED_DATE")
    private LocalDateTime confirmedDate;

    @Column(name = "CONFIRMED_BY")
    private String confirmedBy;

    @Column(name = "CONFIRMATION_NUMBER")
    private int confirmationNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "CONFIRMATION_STATUS")
    private ConfirmationStatus confirmationStatus = ConfirmationStatus.NEW;

    @Column(name = "CONFIRMATION_NUMBER_CREATED_DATE")
    private LocalDateTime confirmationNumberCreatedDate;

    @Column(name = "RESET_NUMBER")
    private int resetNumber;

    @Column(name = "RESET_NUMBER_CREATED_DATE")
    private LocalDateTime resetNumberCreatedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "RESET_NUMBER_STATUS")
    private ResetNumberStatus resetNumberStatus = ResetNumberStatus.EMPTY;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "SEC_USER_ROLES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    private List<SecRole> roles;

    @ManyToOne
    @JoinColumn(name = "PIC_ID", foreignKey = @ForeignKey(name = "SEC_USER_PIC_FK"))
    @JsonManagedReference
    private SysFile pic;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<UserDeviceToken> deviceTokens;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<Organization> organizations;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "USER_FAVORITES",
            joinColumns = @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FAV_USER_FK")),
            inverseJoinColumns = @JoinColumn(name = "SAN_ID", foreignKey = @ForeignKey(name = "FAV_SAN_FK")),
            uniqueConstraints = @UniqueConstraint(name = "UK_USER_ID_AND_SAN_ID",
                    columnNames = {"USER_ID", "SAN_ID"})
    )
    private List<San> favorites;

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

    public void addFav(San san){
        if(getFavorites() == null){
            this.favorites = new ArrayList<>();
        }
        favorites.add(san);
    }

    public void deleteFav(San san){
        if(!getFavorites().isEmpty()){
            favorites.remove(san);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecUser secUser = (SecUser) o;
        return Objects.equals(id, secUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
