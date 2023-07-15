package kz.open.sankaz.mapper;

import kz.open.sankaz.model.AbstractDictionaryLangEntity;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.model.TelNumber;
import kz.open.sankaz.model.enums.UserType;
import kz.open.sankaz.pojo.dto.DictionaryLangDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class AbstractMapper extends BaseMapper {
    @Named("dictionaryToDto")
    @Mapping(target = "id", expression = "java( entity.getId() )")
    abstract public DictionaryLangDto dictionaryToDto(AbstractDictionaryLangEntity entity);
    @IterableMapping(qualifiedByName = "dictionaryToDto")
    abstract public List<DictionaryLangDto> dictionaryToDto(List<AbstractDictionaryLangEntity> entities);




    protected List<String> getTelNumberValuesFromEntity(List<TelNumber> telNumbers){
        return Arrays.asList("+7 775 355 3207");

    }
    protected String getInstaDefault(){
        return "https://instagram.com/sanatour.kz?igshid=NTc4MTIwNjQ2YQ==";

    }

    protected String getNameFromUser(SecUser user){
        if(user.getUserType().equals(UserType.USER)){
            return user.getFullName();
        } else {
            if(!user.getOrganizations().isEmpty()){
                if(!user.getOrganizations().get(0).getSans().isEmpty()){
                    return user.getOrganizations().get(0).getSans().get(0).getName();
                } else {
                    return user.getOrganizations().get(0).getName();
                }
            } else {
                return user.getUsername();
            }
        }
    }

}
