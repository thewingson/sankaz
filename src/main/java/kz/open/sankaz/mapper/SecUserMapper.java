package kz.open.sankaz.mapper;

import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.pojo.dto.SecUserDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SecUserMapper {
    @Named("userToDto")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "roles", ignore = true)
    abstract public SecUserDto userToDto(SecUser secUser);
    @IterableMapping(qualifiedByName = "userToDto")
    abstract public List<SecUserDto> userToDto(List<SecUser> secUsers);

}
