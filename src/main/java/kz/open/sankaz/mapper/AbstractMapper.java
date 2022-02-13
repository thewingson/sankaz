package kz.open.sankaz.mapper;

import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.model.TelNumber;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class AbstractMapper extends BaseMapper {
    @Value("${application.file.upload.path}")
    private String APPLICATION_UPLOAD_PATH;

    protected String getPicUrlFromSysFile(SysFile file){
        if(file != null){
            return APPLICATION_UPLOAD_PATH + file.getFileName();
        }
        return null;
    }

    protected List<String> getPicUrlsFromSysFiles(List<SysFile> files){
        return files.stream().map(sysFile -> APPLICATION_UPLOAD_PATH + sysFile.getFileName()).collect(Collectors.toList());
    }

    protected List<String> getTelNumberValuesFromEntity(List<TelNumber> telNumbers){
        return telNumbers.stream().map(TelNumber::getValue).collect(Collectors.toList());
    }

}
