package kz.open.sankaz.pojo.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingModerCreateFilter extends BookingCreateFilter {
    @NotNull
    private Long roomId;
    @NotNull
    private Integer adults;
    @NotNull
    private Integer children;
    @NotNull
    private String status;
    @NotNull
    private BigDecimal price;
}
