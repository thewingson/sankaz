package kz.open.sankaz.pojo.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomCreateFilter extends BaseFilter {
    @NotEmpty
    @Size(min = 5)
    private String name;
    @NotEmpty
    @Size(min = 10)
    private String description;
    @NotNull
    private BigDecimal price;
}
