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
public class RoomInSanByIdDto extends BaseDto {
    private Long roomClassDicId;
    private String mainPicUrl;
    private String name;
    private BigDecimal price;
    private BigDecimal priceChild;
    private Integer roomCount;
    private Integer bedCount;
}