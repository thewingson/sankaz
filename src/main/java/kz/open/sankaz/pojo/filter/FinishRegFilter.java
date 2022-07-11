package kz.open.sankaz.pojo.filter;

import kz.open.sankaz.pojo.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class FinishRegFilter extends BaseDto {
    @NotEmpty(message = "{TelNumberFilter.telNumber.NotEmpty}")
    @Size(min = 12, max = 12, message = "{TelNumberFilter.telNumber.Size}")
    private String telNumber;

    @NotEmpty(message = "{FinishRegFilter.firstName.NotEmpty}")
    @Size(min = 2, message = "{FinishRegFilter.firstName.Size}")
    private String firstName;

    @NotEmpty(message = "{FinishRegFilter.lastName.NotEmpty}")
    @Size(min = 2, message = "{FinishRegFilter.lastName.Size}")
    private String lastName;

    private Long genderId;

    private Long cityId;

    @NotEmpty(message = "{FinishRegFilter.password.NotEmpty}")
    @Size(min = 8, message = "{FinishRegFilter.password.Size}")
    private String password;
}
