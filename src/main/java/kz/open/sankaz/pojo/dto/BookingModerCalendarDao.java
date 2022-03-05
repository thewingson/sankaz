package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingModerCalendarDao {
    private Long bookingId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime bookingDate;
    private LocalDateTime approvedDate;
    private LocalDateTime cancelledDate;
    private LocalDateTime paidDate;
    private Long userId;
    private String firstName;
    private String lastName;
    private String telNumber;
    private String status;
    private BigDecimal sumPrice;
    private Integer adults;
    private Integer children;

    private Long roomId;
    private String roomNumber;
    private Integer roomCount;
    private Integer bedCount;

    private Long classId;
    private String code;
    private String className;
}
