package kz.open.sankaz.pojo.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockCreateFilter {
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @NotEmpty
    private String titleKz;
    @NotEmpty
    private String descriptionKz;
}
