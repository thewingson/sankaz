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
    private Long genderId;
    private String genderName;
    private String picUrl;
    private String userType;
    private Long cityId;
    private String cityName;
}
