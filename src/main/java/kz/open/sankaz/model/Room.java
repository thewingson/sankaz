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
@Table(name = "ROOM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "ROOM_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "ROOM_ID_SEQ", name = "ROOM_ID", allocationSize = 1)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PRICE")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "SAN_ID", foreignKey = @ForeignKey(name = "ROOM_SAN_FK"), nullable = false)
    @JsonManagedReference
    private San san;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "ROOM_PICS",
            joinColumns = @JoinColumn(name = "ROOM_ID", foreignKey = @ForeignKey(name = "ROOM_PICS_ROOM_FK")),
            inverseJoinColumns = @JoinColumn(name = "PIC_ID", foreignKey = @ForeignKey(name = "ROOM_PICS_PIC_FK")))
    private List<SysFile> pics;

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
