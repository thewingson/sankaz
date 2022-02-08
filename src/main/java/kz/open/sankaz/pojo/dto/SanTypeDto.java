package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SanTypeDto extends AbstractDictionaryDto {
    private String nameKz;
    private String descriptionKz;
}
