package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kz.open.sankaz.image.SanaTourImage;
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

    @OneToMany(mappedBy = "san", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JsonBackReference
    private List<Review> reviews;

    @OneToMany(mappedBy = "sanId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<SanaTourImage> sanaTourImages;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CITY_ID", foreignKey = @ForeignKey(name = "SAN_CITY_FK"), nullable = false)
    @JsonManagedReference(value = "city")
    private City city;

    @Column(name = "LONGITUDE")
    private Double longitude;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "ADDRESS")
    private String address;

    @OneToMany(mappedBy = "san", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Room> rooms;

    @OneToMany(mappedBy = "san", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "sanAdditionals")
    private List<SanAdditional> sanAdditionals;

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



    public String getMainPicUrl(){
     return null;
    }



    public void initPics(){
//        Hibernate.initialize(this.pics);
    }

    public San(Long id, String name,SanType sanType) {
        this.id = id;
        this.name = name;
        this.sanType = sanType;


    }
}
