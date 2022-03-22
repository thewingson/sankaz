package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "SAN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class San extends AbstractEntity{

    @Id
    @GeneratedValue(generator = "SAN_SEQ", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "SAN_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SAN_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "INSTAGRAM_LINK")
    private String instagramLink;

    @Column(name = "SITE_LINK")
    private String siteLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_ID", foreignKey = @ForeignKey(name = "SAN_ORG_FK"), nullable = false)
    @JsonManagedReference
    private Organization organization;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "SAN_TEL_NUMBERS",
            joinColumns = @JoinColumn(name = "SAN_ID", foreignKey = @ForeignKey(name = "SAN_TEL_NUMBERS_SAN_FK")),
            inverseJoinColumns = @JoinColumn(name = "TEL_NUMBER_ID", foreignKey = @ForeignKey(name = "SAN_TEL_NUMBERS_NUMBER_FK")))
    private List<TelNumber> telNumbers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SAN_TYPE_ID", foreignKey = @ForeignKey(name = "SAN_TYPE_FK"), nullable = false)
    @JsonManagedReference
    private SanType sanType;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "SAN_PICS",
            joinColumns = @JoinColumn(name = "SAN_ID", foreignKey = @ForeignKey(name = "SAN_PICS_SAN_FK")),
            inverseJoinColumns = @JoinColumn(name = "PIC_ID", foreignKey = @ForeignKey(name = "SAN_PICS_PIC_FK")))
    private List<SysFile> pics;

    @OneToMany(mappedBy = "san", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JsonBackReference
    private List<Review> reviews;

    @OneToMany(mappedBy = "san", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<RoomClassDic> roomClasses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CITY_ID", foreignKey = @ForeignKey(name = "SAN_CITY_FK"), nullable = false)
    @JsonManagedReference
    private City city;

    @Column(name = "LONGITUDE")
    private Double longitude;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "ADDRESS")
    private String address;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        San san = (San) o;
        return Objects.equals(id, san.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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

    public Float getRating(){
        return (float)getReviews().stream().filter(review -> review.getParentReview() == null).mapToDouble(Review::getRating).average().orElse(0.0);
    }

    public Integer getReviewCount(){
        return (int)getReviews().stream().count();
    }

    public SysFile getMainPic(){
        initPics();
        if(getPics() != null && !getPics().isEmpty()){
            return getPics().get(0);
        }
        return null;
    }

    public String getMainPicUrl(){
        SysFile mainPic = getMainPic();
        if(mainPic != null){
            return mainPic.getFileName();
        } else{
            return null;
        }
    }

    public List<Room> getRooms(){
        return getRoomClasses().stream().map(RoomClassDic::getRooms).collect(ArrayList::new, List::addAll, List::addAll);
    }

    public List<SysFile> getPices(){
        initPics();
        return getPics();
    }

    public void initPics(){
//        Hibernate.initialize(this.pics);
    }
}
