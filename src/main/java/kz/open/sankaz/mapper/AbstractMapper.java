package kz.open.sankaz.mapper;

import kz.open.sankaz.model.AbstractDictionaryEntity;
import kz.open.sankaz.model.AbstractDictionaryLangEntity;
import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.model.TelNumber;
import kz.open.sankaz.pojo.dto.DictionaryLangDto;
import kz.open.sankaz.pojo.dto.FileDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class AbstractMapper extends BaseMapper {
    @Value("${application.file.upload.path.image}")
    private String APPLICATION_UPLOAD_PATH_IMAGE;

    @Value("${application.file.download.path.image}")
    private String APPLICATION_DOWNLOAD_PATH_IMAGE;

    @Named("fileToDto")
    @Mapping(target = "url", expression = "java( getPicUrlFromSysFile(file) )")
    abstract public FileDto fileToDto(SysFile file);
    @IterableMapping(qualifiedByName = "fileToDto")
    abstract public List<FileDto> fileToDto(List<SysFile> files);

    @Named("dictionaryToDto")
    @Mapping(target = "id", expression = "java( entity.getId() )")
    abstract public DictionaryLangDto dictionaryToDto(AbstractDictionaryLangEntity entity);
    @IterableMapping(qualifiedByName = "dictionaryToDto")
    abstract public List<DictionaryLangDto> dictionaryToDto(List<AbstractDictionaryLangEntity> entities);

    protected String getPicUrlFromSysFile(SysFile file){
        if(file != null){
            return APPLICATION_DOWNLOAD_PATH_IMAGE + file.getFileName();
        }
        return null;
    }

    protected List<String> getPicUrlsFromSysFiles(List<SysFile> files) {
        if (files != null) {
            return files.stream().map(sysFile -> APPLICATION_DOWNLOAD_PATH_IMAGE + sysFile.getFileName()).collect(Collectors.toList());
        }
        return null;
    }

    protected List<String> getTelNumberValuesFromEntity(List<TelNumber> telNumbers){
        return telNumbers.stream().map(TelNumber::getValue).collect(Collectors.toList());
    }

}
