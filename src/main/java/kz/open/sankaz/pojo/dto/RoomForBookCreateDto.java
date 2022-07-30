package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomForBookCreateDto extends AbstractDto {
    private String roomNumber;
    private Integer roomCount;
    private Integer bedCount;
    private BigDecimal price;
    private BigDecimal priceChild;
    private Long roomClassDicId;
    private String roomClassName;
}
