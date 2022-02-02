package kz.open.sankaz.service;

import kz.open.sankaz.dto.ChangePasswordDto;
import kz.open.sankaz.dto.PictureDto;
import kz.open.sankaz.dto.SecUserDto;
import kz.open.sankaz.dto.SecUserEditDto;
import kz.open.sankaz.model.SecUser;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService extends UserDetailsManager, CommonService<SecUser>, CommonDtoService<SecUser, SecUserDto> {
    void deleteOneByUsernameSoft(String username);

    SecUser getUserByTelNumber(String telNumber);

    SecUserDto updateOneDto(Long id, SecUserEditDto dto);
    SecUserDto changePassword(Long id, ChangePasswordDto dto);

    PictureDto changePicture(Long userId, MultipartFile file) throws IOException;
    void deletePicture(Long userId);

    SecUser getUserByEmail(String email);
}
