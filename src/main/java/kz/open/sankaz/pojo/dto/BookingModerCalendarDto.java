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
public class BookingModerCalendarDto extends AbstractDto {
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
}
