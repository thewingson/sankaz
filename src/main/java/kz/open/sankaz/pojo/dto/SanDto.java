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
public class SanDto extends AbstractDto {
    private String name;
    private String description;
    private String instagramLink;
    private String siteLink;
    private Long sanTypeId;
    private List<RoomDto> rooms;
    private List<ReviewDto> reviews;
    private List<ItemPicDto> pics;
}