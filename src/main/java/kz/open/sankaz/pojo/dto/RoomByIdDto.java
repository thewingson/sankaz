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
public class RoomByIdDto extends BaseDto {
    private Long classId;
    private List<SanaTourImage> sanaTourImages;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal priceChild;
    private String roomNumber;
    private Integer roomCount;
    private Integer bedCount;
    private SanSimpleDto san;
}