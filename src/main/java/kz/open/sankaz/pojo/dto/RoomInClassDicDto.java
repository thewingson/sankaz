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
public class RoomInClassDicDto extends BaseDto {
    private String name;
    private String mainPic;
    private BigDecimal price;
    private Integer roomCount;
    private Integer bedCount;
}