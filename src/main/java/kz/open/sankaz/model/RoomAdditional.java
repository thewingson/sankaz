package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Table(name = "room_additional")
@Entity
@Data
public class RoomAdditional extends BaseEntity {
    @Id
    @GeneratedValue(generator = "ROOM_ADDITIONAL_SEQ", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(name = "ROOM_ADDITIONAL_SEQ",strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ROOM_ADDITIONAL_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")})
    private Long id;
    @Column
    private String descKz;
    @Column
    private String descRu;


}
