package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DictionaryLangDto extends AbstractDto {
    private String code;
    private String name;
    private String nameKz;
    private String description;
    private String descriptionKz;
}
