package kz.open.sankaz.mapper;

import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.pojo.dto.SecUserDto;
import kz.open.sankaz.pojo.dto.SecUserOwnProfileDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SecUserMapper extends AbstractMapper {
    @Named("userToDto")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "roles", ignore = true)
    abstract public SecUserDto userToDto(SecUser secUser);
    @IterableMapping(qualifiedByName = "userToDto")
    abstract public List<SecUserDto> userToDto(List<SecUser> secUsers);


    @Named("userToOwnProfileDto")
    @Mapping(target = "picUrl", expression = "java(getPicUrlFromSysFile(secUser.getPic()))")
    @Mapping(target = "genderId", source = "secUser.gender.id")
    @Mapping(target = "genderName", source = "secUser.gender.name")
    @Mapping(target = "cityId", source = "secUser.city.id")
    @Mapping(target = "cityName", source = "secUser.city.name")
    abstract public SecUserOwnProfileDto userToOwnProfileDto(SecUser secUser);
    @IterableMapping(qualifiedByName = "userToOwnProfileDto")
    abstract public List<SecUserOwnProfileDto> userToOwnProfileDto(List<SecUser> secUsers);

}
