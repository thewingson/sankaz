package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingUserHistoryDto extends AbstractDto {
    private boolean isActive;
    private Long userId;

    private Long sanId;
    private String sanName;
    private String sanTypeName;
    private String sanAddress;
    private List<String> sanTelNumber;
    private String sanPicUrl;

    private Integer adults;
    private Integer children;
    private Integer roomCount;
    private Integer bedCount;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime bookingDate;
    private LocalDateTime approvedDate;
    private LocalDateTime cancelledDate;
    private LocalDateTime paidDate;

    private BigDecimal sumPrice;
    private String status;
}
