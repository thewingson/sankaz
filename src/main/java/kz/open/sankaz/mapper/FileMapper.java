package kz.open.sankaz.mapper;

import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.pojo.dto.FileUrlDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class FileMapper extends AbstractMapper {

    @Named("fileToFileUrlDto")
    @Mapping(target = "picUrl", expression = "java(getPicUrlFromSysFile(file))")
    abstract public FileUrlDto fileToFileUrlDto(SysFile file);
    @IterableMapping(qualifiedByName = "fileToFileUrlDto")
    abstract public List<FileUrlDto> fileToFileUrlDto(List<SysFile> files);

}
