package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ORGANIZATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Organization extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "ORGANIZATION_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "ORGANIZATION_ID_SEQ", name = "ORGANIZATION_ID", allocationSize = 1)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "MANAGER_FULL_NAME", nullable = false)
    private String managerFullName;

    @Column(name = "IIN", nullable = false, unique = true)
    private String iin;

    @Column(name = "IBAN", nullable = false, unique = true)
    private String iban;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "TEL_NUMBER", nullable = false, unique = true)
    private String telNumber;

    @Column(name = "CONFIRMATION_STATUS")
    private String confirmationStatus = "NEW"; // NEW ON_CONFIRMATION CONFIRMED REJECTED

    @Column(name = "CONFIRMED_TS")
    private LocalDateTime confirmedTs;

    @Column(name = "CONFIRMED_BY")
    private String confirmedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "ORGANIZATION_USER_FK"), nullable = false)
    @JsonManagedReference
    private SecUser user;

}
