package kz.open.sankaz.pojo.filter;

import com.fasterxml.jackson.annotation.JsonFormat;
import kz.open.sankaz.validator.anootation.ValidBookingUser;
import kz.open.sankaz.validator.anootation.ValidDateRange;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@ValidBookingUser
@ValidDateRange
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BookingCreateFilter extends BaseFilter {
    @NotNull
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime startDate;
    @NotNull
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime endDate;

    private Long userId;
    @NotEmpty
    @Size(min = 1)
    private String firstName;
    @NotEmpty
    @Size(min = 1)
    private String lastName;
    @NotEmpty
    @Size(min = 12, max = 12)
    private String telNumber;
}
