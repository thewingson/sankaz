package kz.open.sankaz.pojo.dto.notifications;

import kz.open.sankaz.pojo.dto.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockDto extends AbstractDto {
    private String title;
    private String description;
    private String titleKz;
    private String descriptionKz;
    private int viewCount = 0;
    private Long sanId;
    private boolean active;
}
