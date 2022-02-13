package kz.open.sankaz.mapper;

import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.model.TelNumber;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class AbstractMapper extends BaseMapper {
    @Value("${application.file.upload.path.image}")
    private String APPLICATION_UPLOAD_PATH_IMAGE;

    @Value("${application.file.download.path.image}")
    private String APPLICATION_DOWNLOAD_PATH_IMAGE;

    protected String getPicUrlFromSysFile(SysFile file){
        if(file != null){
            return APPLICATION_DOWNLOAD_PATH_IMAGE + file.getFileName();
        }
        return null;
    }

    protected List<String> getPicUrlsFromSysFiles(List<SysFile> files){
        return files.stream().map(sysFile -> APPLICATION_DOWNLOAD_PATH_IMAGE + sysFile.getFileName()).collect(Collectors.toList());
    }

    protected List<String> getTelNumberValuesFromEntity(List<TelNumber> telNumbers){
        return telNumbers.stream().map(TelNumber::getValue).collect(Collectors.toList());
    }

}
