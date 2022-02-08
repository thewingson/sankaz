package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SanForMainDto extends BaseDto {
    private String name;
    private String description;
    private String picUrl;
    private Float rating;
    private Integer reviewCount;
}