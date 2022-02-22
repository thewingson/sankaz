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
public class RoomClassDicDto extends AbstractDictionaryLangDto {
    private Long id;
    private Long sanId;
    private List<RoomInClassDicDto> rooms;

}
