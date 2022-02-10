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
    private String mainPicUrl;
    private String name;
    private String description;
    private BigDecimal price;
}