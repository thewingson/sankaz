package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

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

    @Column(name = "CONFIRMED_DATE")
    private LocalDateTime confirmedDate;

    @Column(name = "CONFIRMED_BY")
    private String confirmedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "ORGANIZATION_USER_FK"), nullable = false)
    @JsonManagedReference
    private SecUser user;

    @Column(name = "COMPANY_NAME")
    private String companyName;

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

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "ORGANIZATION_PICS",
            joinColumns = @JoinColumn(name = "ORGANIZATION_ID", foreignKey = @ForeignKey(name = "ORGANIZATION_PICS_ORGANIZATION_FK")),
            inverseJoinColumns = @JoinColumn(name = "FILE_ID", foreignKey = @ForeignKey(name = "ORGANIZATION_PICS_FILE_FK")))
    private List<SysFile> pics;

    public void addPic(SysFile pic){
        getPics().add(pic);
    }

}
