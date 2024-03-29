package kz.open.sankaz.pojo.dto;

import kz.open.sankaz.image.SanaTourImage;
import kz.open.sankaz.model.SanAdditional;
import kz.open.sankaz.model.SanType;
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
    private String mainImageBase64;
    private Float rating;
    private Integer reviewCount;
    private Double longitude;
    private Double latitude;
    private List<String> telNumbers;
    private String instagramLink;
    private String siteLink;
    private String address;
    private List<SanaTourImage> sanaTourImages;
    private boolean isFav = false;
    private List<SanAdditional> sanAdditionals;

}