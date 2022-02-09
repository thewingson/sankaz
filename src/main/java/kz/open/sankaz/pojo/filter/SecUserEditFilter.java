package kz.open.sankaz.pojo.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class SecUserEditFilter extends BaseFilter {
    @NotEmpty
    @Size(min = 2)
    private String firstName;
    @NotEmpty
    @Size(min = 2)
    private String lastName;
    private Long genderId;
    @NotEmpty
    @Size(min = 8)
    private String email;
    private Long cityId;
}
