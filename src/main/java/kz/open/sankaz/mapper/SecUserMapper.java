package kz.open.sankaz.mapper;

import kz.open.sankaz.pojo.dto.SecRoleDto;
import kz.open.sankaz.pojo.dto.SecUserDto;
import kz.open.sankaz.model.SecRole;
import kz.open.sankaz.model.SecUser;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SecUserMapper {
    @Named("userToDto")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", expression = "java(roleToDto(secUser.getRoles()))")
    abstract public SecUserDto userToDto(SecUser secUser);
    @IterableMapping(qualifiedByName = "userToDto")
    abstract public List<SecUserDto> userToDto(List<SecUser> secUsers);

    @Named("roleToDto")
    abstract public SecRoleDto roleToDto(SecRole role);
    @IterableMapping(qualifiedByName = "roleToDto")
    abstract public List<SecRoleDto> roleToDto(List<SecRole> roles);

    @Named("dtoToUser")
//    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", expression = "java(dtoToRole(secUserDto.getRoles()))")
    abstract public SecUser dtoToUser(SecUserDto secUserDto);
    @IterableMapping(qualifiedByName = "dtoToUser")
    abstract public List<SecUser> dtoToUser(List<SecUserDto> secUserDtos);

    @Named("dtoToRole")
    abstract public SecRole dtoToRole(SecRoleDto roleDto);
    @IterableMapping(qualifiedByName = "dtoToRole")
    abstract public List<SecRole> dtoToRole(List<SecRoleDto> roleDtos);

}
