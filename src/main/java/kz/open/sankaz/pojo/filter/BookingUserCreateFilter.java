package kz.open.sankaz.pojo.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingUserCreateFilter extends BookingCreateDateInfoFilter {
    @NotNull
    private Long roomId;
    @NotNull
    private Integer adults;
    @NotNull
    private Integer children;
}
