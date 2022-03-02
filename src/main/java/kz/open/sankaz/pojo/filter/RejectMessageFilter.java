package kz.open.sankaz.pojo.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class RejectMessageFilter extends BaseFilter {
    @NotEmpty
    @Size(min = 10)
    private String rejectMessage;
}
