package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ROOM_CLASS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomClass extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "ROOM_CLASS_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "ROOM_CLASS_ID_SEQ", name = "ROOM_CLASS_ID", allocationSize = 1)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ROOM_COUNT")
    private Integer roomCount;

    @Column(name = "BED_COUNT")
    private Integer bedCount;

    @Column(name = "PRICE")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "SAN_ID", foreignKey = @ForeignKey(name = "ROOM_SAN_FK"), nullable = false)
    @JsonManagedReference
    private San san;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "ROOM_CLASS_PICS",
            joinColumns = @JoinColumn(name = "CLASS_ID", foreignKey = @ForeignKey(name = "ROOM_CLASS_PICS_CLASS_FK")),
            inverseJoinColumns = @JoinColumn(name = "FILE_ID", foreignKey = @ForeignKey(name = "ROOM_CLASS_PICS_PIC_FK")))
    private List<SysFile> pics;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomClass roomClass = (RoomClass) o;
        return id.equals(roomClass.id);
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

}
