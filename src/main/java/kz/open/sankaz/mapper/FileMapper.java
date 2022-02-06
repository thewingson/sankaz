package kz.open.sankaz.mapper;

import kz.open.sankaz.dto.FileUrlDto;
import kz.open.sankaz.model.SysFile;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class FileMapper {

    @Value("${application.file.upload.path}")
    private String APPLICATION_UPLOAD_PATH;

    @Named("fileToFileUrlDto")
    @Mapping(target = "picUrl", expression = "java(getUrlFromSysFile(file))")
    abstract public FileUrlDto fileToFileUrlDto(SysFile file);
    @IterableMapping(qualifiedByName = "fileToFileUrlDto")
    abstract public List<FileUrlDto> fileToFileUrlDto(List<SysFile> files);

    public String getUrlFromSysFile(SysFile file){
        return APPLICATION_UPLOAD_PATH + "/" + file.getFileName();
    }

}
