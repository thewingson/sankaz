package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResetPasswordDto extends BaseDto {
    private String telNumber;
    private String password;
    private String confirmPassword;
}
