package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import kz.open.sankaz.model.enums.UserNotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER_NOTIFICATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserNotification extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "USER_NOTIFICATION_SEQ", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "USER_NOTIFICATION_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "USER_NOTIFICATION_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "NOTIFICATION_TYPE", nullable = false)
    private UserNotificationType notificationType;

    @Column(name = "NOTIFY_DATE")
    private LocalDateTime notifyDate;

    @Column(name = "VIEWED", nullable = false)
    private boolean viewed = false;

    @ManyToOne
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "NOTIFICATION_USER_FK"))
    @JsonManagedReference
    private SecUser user;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID", foreignKey = @ForeignKey(name = "NOTIFICATION_BOOK_FK"))
    @JsonManagedReference
    private BookingHistory bookingHistory;

    @ManyToOne
    @JoinColumn(name = "STOCK_ID", foreignKey = @ForeignKey(name = "NOTIFICATION_STOCK_FK"))
    @JsonManagedReference
    private Stock stock;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TITLE_KZ")
    private String titleKz;

    @Column(name = "DESCRIPTION_KZ")
    private String descriptionKz;

}
