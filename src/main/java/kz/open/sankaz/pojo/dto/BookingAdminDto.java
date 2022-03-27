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
public class BookingAdminDto extends AbstractDto {
    private Long userId;
    private BigDecimal sumPrice;
    private String firstName;
    private String lastName;
    private String telNumber;
    private Integer adults;
    private Integer children;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private LocalDateTime approvedDate;
    private LocalDateTime cancelledDate;
    private LocalDateTime paidDate;
    private RoomByIdDto room;
}
