package kz.open.sankaz.image;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kz.open.sankaz.model.BaseEntity;
import kz.open.sankaz.model.Room;
import kz.open.sankaz.model.San;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "sanatour_image")
@Data
public class SanaTourImage extends BaseEntity {
    @Id
    @GeneratedValue(generator = "sanatour_image_seq", strategy = GenerationType.SEQUENCE)
    @GenericGenerator( name = "sanatour_image_seq",
                       strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
                       parameters = {@Parameter(name = "sequence_name", value = "sanatour_image_seq"),
                                     @Parameter(name = "initial_value",value = "1"),
                                     @Parameter(name = "increment_size",value = "1")
    })
    private Long id;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "san_id",foreignKey = @ForeignKey(name = "sanatour_image_san_fk"))
    private San sanId;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "room_id",foreignKey = @ForeignKey(name = "sanatour_image_room_fk"))
    private Room roomId;
    @Column
    private String type;
    @Column(name = "base64_original",columnDefinition = "TEXT")
    private String base64Original;
    @Column(name = "base64_scaled",columnDefinition = "TEXT")
    private String base64Scaled;
}
