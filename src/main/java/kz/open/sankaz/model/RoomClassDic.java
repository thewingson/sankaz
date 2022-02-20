package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ROOM_CLASS_DIC",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "CODE", "SAN_ID" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomClassDic extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "ROOM_CLASS_DIC_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "ROOM_CLASS_DIC_ID_SEQ", name = "ROOM_CLASS_DIC_ID", allocationSize = 1)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CODE", nullable = false)
    private String code;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "NAME_KZ")
    private String nameKz;

    @Column(name = "DESCRIPTION_KZ")
    private String descriptionKz;

    @ManyToOne
    @JoinColumn(name = "SAN_ID", foreignKey = @ForeignKey(name = "ROOM_SAN_FK"), nullable = false)
    @JsonManagedReference
    private San san;

    @OneToMany(mappedBy = "roomClassDic", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<Room> rooms;

}
