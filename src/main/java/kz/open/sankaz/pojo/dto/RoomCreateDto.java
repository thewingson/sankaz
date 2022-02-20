package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomCreateDto extends AbstractDto {
    private String roomNumber;
    private Integer roomCount;
    private Integer bedCount;
    private BigDecimal price;
    private Long roomClassDicId;
}
