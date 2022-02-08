package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CompanyCategoryDto extends AbstractDictionaryDto {
    private String nameKz;
    private String descriptionKz;
}
