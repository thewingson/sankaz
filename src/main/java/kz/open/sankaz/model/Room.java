package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

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

}
