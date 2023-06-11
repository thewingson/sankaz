package kz.open.sankaz.pojo.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomCreateFilter extends BaseFilter {
    @NotEmpty
    @Size(min = 1)
    private String roomNumber;
    @NotNull
    private Long roomClassDicId;
    @NotNull
    private Long sanId;
    @NotNull
    private Integer roomCount;
    @NotNull
    private Integer bedCount;
    @NotNull
    private BigDecimal price;
    @NotNull
    private BigDecimal childPrice;
    private String roomAdditionalDto;
    private List<byte[]> images;
    private Boolean isEnable=Boolean.TRUE;

}
