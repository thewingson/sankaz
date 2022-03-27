package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;
import java.util.Locale;

@SQLDelete(sql = "UPDATE ROOM_CLASS_DIC SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
@Table(name = "ROOM_CLASS_DIC",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "CODE" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomClassDic extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "ROOM_CLASS_DIC_SEQ", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "ROOM_CLASS_DIC_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ROOM_CLASS_DIC_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Column(name = "DELETED", nullable = false)
    private boolean deleted = Boolean.FALSE;

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

    @OneToMany(mappedBy = "roomClassDic", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Room> rooms;

    public String getLocaleName(Locale locale){
        return getName();
    }

    public String getLocaleDescription(Locale locale){
        return getDescription();
    }

    public String getCurrentLocaleName(){
        return getName();
    }

    public String getCurrentLocaleDescription(){
        return getDescription();
    }

}
