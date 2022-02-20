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
public class SanAdminDto extends AbstractDto {
    private String name;
    private String description;
    private String instagramLink;
    private String siteLink;
    private Long sanTypeId;
    private List<String> telNumbers;
    private FileDto pic;
    private Long cityId;
    private Long orgId;
    private Double longitude;
    private Double latitude;
    private List<FileDto> pics;
}