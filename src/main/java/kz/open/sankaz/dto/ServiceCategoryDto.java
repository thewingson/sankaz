package kz.open.sankaz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServiceCategoryDto extends AbstractDictionaryDto {
    private String nameKz;
    private String descriptionKz;
}
