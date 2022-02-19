package kz.open.sankaz.pojo.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class RoomClassCreateFilter extends BaseFilter {
    @NotEmpty
    @Size(min = 3)
    private String name;
    private String description;
    @NotNull
    private Integer roomCount;
    @NotNull
    private Integer bedCount;
    @NotNull
    private BigDecimal price;
    @NotNull
    private Long sanId;
}
