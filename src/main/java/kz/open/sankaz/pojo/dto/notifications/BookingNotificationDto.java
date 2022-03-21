package kz.open.sankaz.pojo.dto.notifications;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingNotificationDto {
    private Integer newNotifyCount;
    private List<BookNotItemDto> books;
}
