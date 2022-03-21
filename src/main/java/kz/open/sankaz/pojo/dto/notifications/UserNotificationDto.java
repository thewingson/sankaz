package kz.open.sankaz.pojo.dto.notifications;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserNotificationDto {
    private Integer newNotifyCount;
    private StockNotificationDto stockNotification;
    private BookingNotificationDto bookingNotification;
    private PaymentNotificationDto paymentNotification;
}
