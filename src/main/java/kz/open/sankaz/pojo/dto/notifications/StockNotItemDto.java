package kz.open.sankaz.pojo.dto.notifications;

import kz.open.sankaz.pojo.dto.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockNotItemDto extends AbstractDto {
    private boolean viewed;
    private LocalDateTime notifyDate;
    private StockDto stock;
}
