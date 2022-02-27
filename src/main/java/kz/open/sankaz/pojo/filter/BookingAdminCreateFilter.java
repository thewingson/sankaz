package kz.open.sankaz.pojo.filter;

import com.fasterxml.jackson.annotation.JsonFormat;
import kz.open.sankaz.validator.anootation.ValidBookingStatusDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ValidBookingStatusDate
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingAdminCreateFilter extends BookingCreateFilter {
    @NotNull
    private Integer adults;
    @NotNull
    private Integer children;
    @NotNull
    private Long roomId;

    @NotNull
    private String status;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cancelledDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paidDate;
}
