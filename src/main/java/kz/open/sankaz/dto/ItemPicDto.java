package kz.open.sankaz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemPicDto extends AbstractDto {
    private Long id;
    private String itemType;
    private Long itemId;
    private String fileName;
    private String extension;
    private String size;
}
