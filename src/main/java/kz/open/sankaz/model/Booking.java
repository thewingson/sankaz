package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import kz.open.sankaz.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@DynamicUpdate
@Table(name = "BOOKING")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking extends BaseEntity {

    @Id
    @GeneratedValue(generator = "BOOKING_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "BOOKING_ID_SEQ", name = "BOOKING_ID", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "BOOKING_USER_FK"))
    @JsonManagedReference
    private SecUser user;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "TEL_NUMBER", nullable = false)
    private String telNumber;

    @Column(name = "ADULTS_COUNT")
    private Integer adultsCount;

    @Column(name = "CHILDREN_COUNT")
    private Integer childrenCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOM_ID", foreignKey = @ForeignKey(name = "BOOKING_ROOM_FK"), nullable = false)
    @JsonManagedReference
    private Room room;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private BookingStatus status;

    @Column(name = "START_DATE", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "END_DATE", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "BOOKING_DATE", nullable = false)
    private LocalDateTime bookingDate = LocalDateTime.now();

    @Column(name = "SUM_PRICE", nullable = false)
    private BigDecimal sumPrice;

    @Column(name = "APPROVED_DATE")
    private LocalDateTime approvedDate;
    @Column(name = "PAID_DATE")
    private LocalDateTime paidDate;
    @Column(name = "CANCELLED_DATE")
    private LocalDateTime cancelledDate;

    public boolean isBookedByUser(){
        return user != null;
    }

    public boolean isApproved(){
        return status.equals(BookingStatus.APPROVED);
    }

    public boolean isCancelled(){
        return status.equals(BookingStatus.CANCELLED);
    }

    public boolean isPaid(){
        return status.equals(BookingStatus.PAID);
    }

    public boolean isWaiting(){
        return status.equals(BookingStatus.WAITING);
    }

    public boolean isActive(){
        return !isCancelled() && !isPaid();
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }

}
