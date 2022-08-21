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
public class BookingModerAllDto extends AbstractDto {
    private String firstName;
    private String lastName;
    private RoomClassDicSimpleDto roomClass;
    private RoomByIdDto room;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime paidDate;
    private BigDecimal sumPrice;
    private BigDecimal price;
    private String status;
    private Integer adults;
    private Integer children;
    private String telNumber;
    private String cancelReason;
}
