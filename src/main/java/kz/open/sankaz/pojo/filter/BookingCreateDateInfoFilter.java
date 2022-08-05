package kz.open.sankaz.pojo.filter;

import com.fasterxml.jackson.annotation.JsonFormat;
import kz.open.sankaz.validator.anootation.ValidDateRange;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ValidDateRange
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BookingCreateDateInfoFilter extends BaseFilter {
    @NotNull
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime startDate;
    @NotNull
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime endDate;

    public LocalDateTime getStartDate(){
        return LocalDateTime.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth(), 0, 0, 0);
    }

    public LocalDateTime getEndDate(){
        return LocalDateTime.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth(), 23, 0, 0);
    }

}
