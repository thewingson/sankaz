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
public class BookingByIdUserDto extends AbstractDto {
    private Long userId;
    private String orgName;
    private String sanName;
    private String sanType;
    private Integer roomCount;
    private Integer bedCount;
    private String status;
    private BigDecimal sumPrice;
    private LocalDateTime bookingDate;
    private LocalDateTime approvedDate;
    private LocalDateTime cancelledDate;
    private LocalDateTime paidDate;
}
