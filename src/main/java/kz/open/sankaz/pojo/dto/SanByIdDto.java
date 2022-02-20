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
public class SanByIdDto extends AbstractDto {
    private List<FileDto> pics;
    private String name;
    private Float rating;
    private String sanType;
    private String description;
    private List<String> telNumbers;
    private String instagramLink;
    private String siteLink;
    private Integer reviewCount;
    private List<RoomInSanByIdDto> rooms;
    private Double longitude;
    private Double latitude;
}