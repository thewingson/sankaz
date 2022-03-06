package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SanForMainDto extends BaseDto {
    private String name;
    private DictionaryLangDto sanType;
    private String description;
    private String mainPicUrl;
    private Float rating;
    private Integer reviewCount;
    private Double longitude;
    private Double latitude;
    private List<FileDto> picUrls;
    private List<String> telNumbers;
    private String instagramLink;
    private String siteLink;
}