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
public class OrganizationRegisterFilter extends BaseFilter {
    @NotNull(message = "{RegisterFilter.confirmationNumber.NotNull}")
    private Integer confirmationNumber;
    @NotEmpty(message = "{TelNumberFilter.telNumber.NotEmpty}")
    @Size(min = 12, max = 12, message = "{TelNumberFilter.telNumber.Size}")
    private String telNumber;
    @NotEmpty(message = "{FinishRegFilter.password.NotEmpty}")
    @Size(min = 8, message = "{FinishRegFilter.password.Size}")
    private String password;
}
