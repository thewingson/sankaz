package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kz.open.sankaz.image.SanaTourImage;
import kz.open.sankaz.pojo.dto.DatesDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SqlResultSetMapping(
        name="getRoomAvailabilityForDateRangeMapping",
        classes={
                @ConstructorResult(
                        targetClass=DatesDto.class,
                        columns={
                                @ColumnResult(name="checkdate", type = LocalDate.class),
                                @ColumnResult(name="isfree", type = boolean.class)
                        }
                )
        }
)
@NamedNativeQuery(name="Room.getRoomAvailabilityForDateRange",
        query="SELECT " +
                "cast(dat as DATE) as checkdate, " +
                "case when b.id is null " +
                "then true " +
                "    else false " +
                "    end as isfree " +
                "FROM " +
                "    generate_series(cast(:startDate as DATE), cast(:endDate as DATE), cast('1 day' as interval)) dat " +
                "left join booking b on b.room_id = :roomId and dat between b.start_date and b.end_date " +
                "and ( (b.status = 'PAID') or (b.status = 'TRANSFERRED') " +
                "or ((b.status = 'APPROVED' ) and (b.approved_date + (10 * interval '1 minute')) >= current_timestamp ) );",
        resultSetMapping="getRoomAvailabilityForDateRangeMapping")
@Entity
@Table(name = "ROOM",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "ROOM_NUMBER", "SAN_ID" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "ROOM_SEQ", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "ROOM_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ROOM_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Column(name = "ROOM_NUMBER", nullable = false)
    private String roomNumber;

    @Column(name = "ROOM_COUNT")
    private Integer roomCount;

    @Column(name = "BED_COUNT")
    private Integer bedCount;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "PRICE_CHILD")
    private BigDecimal priceChild;

    @Column
    private String additionals;
    @Column(columnDefinition = "boolean default true")
    private boolean isEnable=true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CLASS_ID", foreignKey = @ForeignKey(name = "ROOM_CLASS_FK"), nullable = false)
    @JsonManagedReference
    private RoomClassDic roomClassDic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SAN_ID", foreignKey = @ForeignKey(name = "ROOM_SAN_FK"), nullable = false)
    @JsonManagedReference
    private San san;

    @OneToMany(mappedBy = "roomId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<SanaTourImage> sanaTourImages;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Booking> books;

}
