package kz.open.sankaz.pojo.dto;

import kz.open.sankaz.image.SanaTourImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SanForMainAdminDto extends BaseDto {
    private String name;
    private DictionaryLangDto sanType;
    private DictionaryLangDto city;
    private String address;
    private String description;
    private String mainPicUrl;
    private Float rating;
    private Integer reviewCount;
    private Double longitude;
    private Double latitude;
    private List<String> telNumbers;
    private String instagramLink;
    private String siteLink;
    private List<SanaTourImage> sanaTourImages;
    private boolean isFav = false;
}