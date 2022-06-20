package kz.open.sankaz.pojo.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthDto {
    private String  parent_login;
    private String subject_type;
    private int country;
    private long id;
    private String login;
    private int status;
    private String[] roles;
    private String assignments;
    private String token;
    private String email;
    private String created_at;
    private int identified;
    private boolean resident_kz;
}
