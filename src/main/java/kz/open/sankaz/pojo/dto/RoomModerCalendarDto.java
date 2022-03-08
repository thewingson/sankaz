package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomModerCalendarDto extends AbstractDto {
    private String roomNumber;
    private Integer roomCount;
    private Integer bedCount;
    private List<BookingModerCalendarDto> bookings = new ArrayList<>();
}
