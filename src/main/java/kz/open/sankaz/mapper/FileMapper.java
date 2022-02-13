package kz.open.sankaz.mapper;

import kz.open.sankaz.pojo.dto.FileUrlDto;
import kz.open.sankaz.model.SysFile;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class FileMapper extends AbstractMapper {

    @Value("${application.file.upload.path.image}")
    private String APPLICATION_UPLOAD_PATH_IMAGE;

    @Value("${application.file.download.path.image}")
    private String APPLICATION_DOWNLOAD_PATH_IMAGE;

    @Named("fileToFileUrlDto")
    @Mapping(target = "picUrl", expression = "java(getPicUrlFromSysFile(file))")
    abstract public FileUrlDto fileToFileUrlDto(SysFile file);
    @IterableMapping(qualifiedByName = "fileToFileUrlDto")
    abstract public List<FileUrlDto> fileToFileUrlDto(List<SysFile> files);

}
