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
public class BookingUserHistorySingleDto extends AbstractDto {
    private Integer adults;
    private Integer children;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime bookingDate;
    private LocalDateTime approvedDate;
    private LocalDateTime cancelledDate;
    private LocalDateTime paidDate;

    private BigDecimal sumPrice;
    private String status;

    private String woopOrderId;
    private String paymentUrl;
}
