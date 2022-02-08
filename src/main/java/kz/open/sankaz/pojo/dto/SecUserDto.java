package kz.open.sankaz.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SecUserDto extends AbstractDto {
    private String firstName;
    private String lastName;
    private String telNumber;
    private String username;
    private String password;
    private String city;
    private String email;
    private String gender;

    private LocalDateTime confirmedTs;
    private String confirmedBy;
    private int confirmationNumber;
    private LocalDateTime confirmationNumberCreatedTs;
    private int resetNumber;
    private LocalDateTime resetNumberCreatedTs;

    private List<SecRoleDto> roles;
}
