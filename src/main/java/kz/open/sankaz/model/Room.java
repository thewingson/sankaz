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

@Entity
@Table(name = "ROOM",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "ROOM_NUMBER", "CLASS_ID" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "ROOM_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "ROOM_ID_SEQ", name = "ROOM_ID", allocationSize = 1)
    private Long id;

    @Column(name = "ROOM_NUMBER", nullable = false)
    private String roomNumber;

    @Column(name = "ROOM_COUNT")
    private Integer roomCount;

    @Column(name = "BED_COUNT")
    private Integer bedCount;

    @Column(name = "PRICE")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "CLASS_ID", foreignKey = @ForeignKey(name = "ROOM_CLASS_FK"), nullable = false)
    @JsonManagedReference
    private RoomClassDic roomClassDic;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "ROOM_PICS",
            joinColumns = @JoinColumn(name = "ROOM_ID", foreignKey = @ForeignKey(name = "ROOM_PICS_ROOM_FK")),
            inverseJoinColumns = @JoinColumn(name = "FILE_ID", foreignKey = @ForeignKey(name = "ROOM_PICS_PIC_FK")))
    private List<SysFile> pics;

//    @ManyToOne
//    @JoinColumn(name = "CLASS_ID", foreignKey = @ForeignKey(name = "ROOM_CLASS_FK"), nullable = false)
//    @JsonManagedReference
//    private RoomClass roomClass;

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
