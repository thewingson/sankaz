package kz.open.sankaz.pojo.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class ChangePasswordFilter extends BaseFilter {
    @NotEmpty
    @Size(min = 8)
    private String password;
    @NotEmpty
    @Size(min = 8)
    private String confirmPassword;
}
