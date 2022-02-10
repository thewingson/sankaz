package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @ManyToOne
    @JoinColumn(name = "SAN_TYPE_ID", foreignKey = @ForeignKey(name = "SAN_TYPE_FK"), nullable = false)
    @JsonManagedReference
    private SanType sanType;

    @ManyToOne
    @JoinColumn(name = "PIC_ID", foreignKey = @ForeignKey(name = "SAN_PIC_FK"))
    @JsonManagedReference
    private SysFile pic;

    @OneToMany(mappedBy = "san", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<Review> reviews;

    @OneToMany(mappedBy = "san", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<Room> rooms;

    @ManyToOne
    @JoinColumn(name = "CITY_ID", foreignKey = @ForeignKey(name = "SAN_CITY_FK"), nullable = false)
    @JsonManagedReference
    private City city;

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
        return (float)getReviews().stream().mapToDouble(Review::getRating).average().orElse(0.0);
    }

    public Integer getReviewCount(){
        return (int)getReviews().stream().count();
    }

    public SysFile getMainPic(){
        return pic;
    }

    public String getMainPicUrl(){
        SysFile mainPic = getMainPic();
        if(mainPic != null){
            return mainPic.getFileName();
        } else{
            return null;
        }
    }
}
