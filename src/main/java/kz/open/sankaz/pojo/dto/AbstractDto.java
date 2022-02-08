package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractDto extends BaseDto {
    protected LocalDateTime createTs;
    protected String createdBy;
    protected LocalDateTime updateTs;
    protected String updatedBy;
    protected LocalDateTime deleteTs;
    protected String deletedBy;
}
