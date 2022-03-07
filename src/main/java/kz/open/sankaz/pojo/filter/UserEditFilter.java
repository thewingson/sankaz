package kz.open.sankaz.pojo.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
public class UserEditFilter extends BaseFilter {
    @NotEmpty
    private String userType;
    @NotEmpty
    private String username;
    @NotEmpty
    private String telNumber;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    private String email;
    private Long genderId;
    private Long cityId;
    @NotEmpty
    private String confirmationStatus = "FINISHED";
}
