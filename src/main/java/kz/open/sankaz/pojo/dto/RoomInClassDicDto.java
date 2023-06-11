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
public class RoomInClassDicDto extends BaseDto {
    private String roomNumber;
    //TODO private SanaTourImage mainPic;
    private BigDecimal price;
    private BigDecimal priceChild;
    private Integer roomCount;
    private Integer bedCount;
    private String additionals;
    private List<SanaTourImage> sanaTourImages;
    private boolean isEnable;
}