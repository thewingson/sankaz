package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SAN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class San extends AbstractEntity{

    @Id
    @GeneratedValue(generator = "SAN_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "SAN_ID_SEQ", name = "SAN_ID", allocationSize = 1)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "INSTAGRAM_LINK")
    private String instagramLink;

    @Column(name = "SITE_LINK")
    private String siteLink;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "SAN_TEL_NUMBERS",
            joinColumns = @JoinColumn(name = "SAN_ID", foreignKey = @ForeignKey(name = "SAN_TEL_NUMBERS_SAN_FK")),
            inverseJoinColumns = @JoinColumn(name = "TEL_NUMBER_ID", foreignKey = @ForeignKey(name = "SAN_TEL_NUMBERS_NUMBER_FK")))
    private List<TelNumber> telNumbers;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "SAN_TYPES",
            joinColumns = @JoinColumn(name = "SAN_ID", foreignKey = @ForeignKey(name = "SAN_TYPES_SAN_FK")),
            inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID", foreignKey = @ForeignKey(name = "SAN_TYPES_TYPE_FK")))
    private List<SanType> sanTypes;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "SAN_PICS",
            joinColumns = @JoinColumn(name = "SAN_ID", foreignKey = @ForeignKey(name = "SAN_PICS_SAN_FK")),
            inverseJoinColumns = @JoinColumn(name = "PIC_ID", foreignKey = @ForeignKey(name = "SAN_PICS_PIC_FK")))
    private List<SysFile> pics;

    @OneToMany(mappedBy = "san", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<Review> reviews;

    @OneToMany(mappedBy = "san", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<Room> rooms;

    public void addSanType(SanType sanType){
        if(getSanTypes() == null){
            this.sanTypes = new ArrayList<>();
        }
        sanTypes.add(sanType);
    }

    public void deleteSanType(SanType sanType){
        sanTypes.remove(sanType);
    }

    public void addTelNumber(TelNumber telNumber){
        if(getTelNumbers() == null){
            this.telNumbers = new ArrayList<>();
        }
        telNumbers.add(telNumber);
    }

    public void addTelNumbers(List<TelNumber> telNumbers){
        if(getTelNumbers() == null){
            this.telNumbers = new ArrayList<>();
        }
        this.telNumbers.addAll(telNumbers);
    }

    public void deleteTelNumber(TelNumber telNumber){
        if(!getTelNumbers().isEmpty()){
            getTelNumbers().remove(telNumber);
        }
    }

    public void deleteTelNumbers(List<TelNumber> telNumbers){
        if(!getTelNumbers().isEmpty()){
            this.getTelNumbers().removeAll(telNumbers);
        }
    }

    public void addPic(SysFile pic){
        if(getPics() == null){
            this.pics = new ArrayList<>();
        }
        pics.add(pic);
    }

    public void addPics(List<SysFile> pics){
        if(getPics() == null){
            this.pics = new ArrayList<>();
        }
        this.pics.addAll(pics);
    }

    public void deletePic(SysFile pic){
        if(!getPics().isEmpty()){
            getPics().remove(pic);
        }
    }

    public void deletePics(List<SysFile> pics){
        if(!getPics().isEmpty()){
            this.getPics().removeAll(pics);
        }
    }
}
