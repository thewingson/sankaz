package kz.open.sankaz.pojo.dto;

import kz.open.sankaz.image.SanaTourImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomInSanByIdDto extends BaseDto {
    private Long roomClassDicId;
    private SanaTourImage sanaTourImage;
    private String name;
    private BigDecimal price;
    private BigDecimal priceChild;
    private Integer roomCount;
    private Integer bedCount;
}