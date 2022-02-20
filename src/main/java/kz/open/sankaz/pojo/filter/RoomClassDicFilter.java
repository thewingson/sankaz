package kz.open.sankaz.pojo.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomClassDicFilter extends BaseFilter {
    @NotEmpty
    @Size(min = 2)
    protected String code;
    @NotEmpty
    @Size(min = 3)
    protected String name;
    protected String description;
    protected String nameKz;
    protected String descriptionKz;
    @NotNull
    protected Long sanId;
}
