package kz.open.sankaz.mapper;

import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.pojo.dto.SecUserDto;
import kz.open.sankaz.pojo.dto.SecUserForNewOrgDto;
import kz.open.sankaz.pojo.dto.SecUserInOrgDto;
import kz.open.sankaz.pojo.dto.SecUserOwnProfileDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SecUserMapper extends AbstractMapper {
    @Named("userToDto")
    @Mapping(target = "confirmationStatus", source = "secUser.confirmationStatus")
    @Mapping(target = "userType", source = "secUser.userType")
    @Mapping(target = "city", expression = "java( dictionaryToDto(secUser.getCity()) )")
    @Mapping(target = "gender", expression = "java( dictionaryToDto(secUser.getGender()) )")
    //TODO  @Mapping(target = "pic", expression = "java( fileToDto(secUser.getPic()) )")
    abstract public SecUserDto userToDto(SecUser secUser);
    @IterableMapping(qualifiedByName = "userToDto")
    abstract public List<SecUserDto> userToDto(List<SecUser> secUsers);

    @Named("userToSecUserForNewOrgDto")
    abstract public SecUserForNewOrgDto userToSecUserForNewOrgDto(SecUser secUser);
    @IterableMapping(qualifiedByName = "userToSecUserForNewOrgDto")
    abstract public List<SecUserForNewOrgDto> userToSecUserForNewOrgDto(List<SecUser> secUsers);

    @Named("userToOwnProfileDto")
    //TODO  @Mapping(target = "picUrl", expression = "java(getPicUrlFromSysFile(secUser.getPic()))")
    @Mapping(target = "genderId", source = "secUser.gender.id")
    @Mapping(target = "genderName", source = "secUser.gender.name")
    @Mapping(target = "cityId", source = "secUser.city.id")
    @Mapping(target = "cityName", source = "secUser.city.name")
    abstract public SecUserOwnProfileDto userToOwnProfileDto(SecUser secUser);
    @IterableMapping(qualifiedByName = "userToOwnProfileDto")
    abstract public List<SecUserOwnProfileDto> userToOwnProfileDto(List<SecUser> secUsers);

    @Named("userToSecUserInOrgDto")
    abstract public SecUserInOrgDto userToSecUserInOrgDto(SecUser secUser);
    @IterableMapping(qualifiedByName = "userToSecUserInOrgDto")
    abstract public List<SecUserInOrgDto> userToSecUserInOrgDto(List<SecUser> secUsers);

}
