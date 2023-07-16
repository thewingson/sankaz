package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DictionaryLangDto extends AbstractDto {
    private String code;
    private String name;
    private String nameKz;
    private String description;
    private String descriptionKz;
}
