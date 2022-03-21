package kz.open.sankaz.pojo.dto.notifications;

import kz.open.sankaz.pojo.dto.AbstractDto;
import kz.open.sankaz.pojo.dto.BookingByIdUserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookNotItemDto extends AbstractDto {
    private boolean viewed;
    private LocalDateTime notifyDate;
    private BookingByIdUserDto booking;
    private String title;
    private String titleKz;

}
