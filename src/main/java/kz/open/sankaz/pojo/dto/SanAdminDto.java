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
public class SanAdminDto extends AbstractDto {
    private String name;
    private String description;
    private String instagramLink;
    private String siteLink;
    private DictionaryLangDto sanType;
    private List<String> telNumbers;
    private SanaTourImage sanaTourImage;
    private DictionaryLangDto city;
    private Long orgId;
    private List<SanaTourImage> sanaTourImages;
}