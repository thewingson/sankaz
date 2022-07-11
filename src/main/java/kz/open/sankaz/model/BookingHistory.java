package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import kz.open.sankaz.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "BOOKING_HISTORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingHistory extends BaseEntity {

    @Id
    @GeneratedValue(generator = "BOOKING_HISTORY_SEQ", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "BOOKING_HISTORY_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "BOOKING_HISTORY_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOKING_ID", foreignKey = @ForeignKey(name = "BOOKING_HISTORY_BOOKING_FK"), nullable = false)
    @JsonManagedReference
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private BookingStatus status;

    @Column(name = "HISTORY_DATE", nullable = false)
    private LocalDateTime historyDate;

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

}
