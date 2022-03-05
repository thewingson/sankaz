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
public class RoomClassModerCalendarDto extends AbstractDto {
    private String code;
    private String className;
    private List<RoomModerCalendarDto> rooms;
}
