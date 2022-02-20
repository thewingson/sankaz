package kz.open.sankaz.pojo.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserCreateFilter extends BaseFilter {
    @NotEmpty
    private String userType;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    private String confirmPassword;
    @NotEmpty
    private String telNumber;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    private String email;
    private Long genderId;
    private Long cityId;
    private List<Long> roles;
//    private SysFile pic;






}
