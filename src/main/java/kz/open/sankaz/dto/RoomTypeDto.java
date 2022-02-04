package kz.open.sankaz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoomTypeDto extends AbstractDictionaryDto {
    private String nameKz;
    private String descriptionKz;
}
