package kz.open.sankaz.pojo.dto;

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
    private String name;
    private String description;
    private BigDecimal price;

    private Long sanId;
    private List<String> picUrls;
}
