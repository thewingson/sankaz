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
public class RoomCreateDto extends AbstractDto {
    private String roomNumber;
    private Integer roomCount;
    private Integer bedCount;
    private BigDecimal price;
    private BigDecimal priceChild;
    private Long roomClassDicId;
    private List <SanaTourImage> sanaTourImages;
    private Long sanId;
    private boolean enable;
    private String additionals;

}
