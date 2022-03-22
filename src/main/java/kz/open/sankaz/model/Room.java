package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
                "left join booking b on b.room_id = :roomId and dat between b.start_date and b.end_date and b.status <> 'CANCELLED' and b.status <> 'WAITING';",
        resultSetMapping="getRoomAvailabilityForDateRangeMapping")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CLASS_ID", foreignKey = @ForeignKey(name = "ROOM_CLASS_FK"), nullable = false)
    @JsonManagedReference
    private RoomClassDic roomClassDic;

    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "ROOM_PICS",
            joinColumns = @JoinColumn(name = "ROOM_ID", foreignKey = @ForeignKey(name = "ROOM_PICS_ROOM_FK")),
            inverseJoinColumns = @JoinColumn(name = "FILE_ID", foreignKey = @ForeignKey(name = "ROOM_PICS_PIC_FK")))
    private List<SysFile> pics;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Booking> books;

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

    public SysFile getMainPic(){
        if(getPics() != null && !getPics().isEmpty()){
            return getPics().get(0);
        }
        return null;
    }

    public String getMainPicUrl(){
        SysFile mainPic = getMainPic();
        if(mainPic != null){
            return mainPic.getFileName();
        } else{
            return null;
        }
    }

}
