package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SecUserOwnProfileDto extends AbstractDto {
    private String firstName;
    private String lastName;
    private String telNumber;
    private String username;
    private String password;
    private String email;
    private String genderId;
    private String genderName;
    private String picUrl;
    private String userType;
    private String cityId;
    private String cityName;
}
