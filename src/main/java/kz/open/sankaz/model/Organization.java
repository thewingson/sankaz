package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kz.open.sankaz.model.enums.ConfirmationStatus;
import kz.open.sankaz.model.enums.OrganizationConfirmationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@DynamicUpdate
@Table(name = "ORGANIZATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Organization extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "ORGANIZATION_SEQ", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "ORGANIZATION_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ORGANIZATION_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
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

    @Enumerated(EnumType.STRING)
    @Column(name = "CONFIRMATION_STATUS")
    private OrganizationConfirmationStatus confirmationStatus = OrganizationConfirmationStatus.NEW;

    @Column(name = "APPROVED_DATE")
    private LocalDateTime approvedDate;

    @Column(name = "REJECTED_DATE")
    private LocalDateTime rejectedDate;

    @Column(name = "REQUEST_DATE")
    private LocalDateTime requestDate;

    @Column(name = "CONFIRMED_BY")
    private String confirmedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "ORGANIZATION_USER_FK"))
    @JsonManagedReference
    private SecUser user;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "INSTAGRAM_LINK")
    private String instagramLink;

    @Column(name = "SITE_LINK")
    private String siteLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_CATEGORY_ID", foreignKey = @ForeignKey(name = "ORGANIZATION_COMPANY_CATEGORY_FK"))
    @JsonManagedReference
    private CompanyCategory companyCategory;



    @Column(name = "REJECT_MESSAGE")
    private String rejectMessage;

    @OneToMany(mappedBy = "organization")
    @JsonBackReference
    private List<San> sans;

}
