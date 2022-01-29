package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.repo.SysFileRepo;
import kz.open.sankaz.service.SysFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@Transactional
public class SysFileServiceImpl extends AbstractService<SysFile, SysFileRepo> implements SysFileService {

    private final SysFileRepo sysFileRepo;

    public SysFileServiceImpl(SysFileRepo sysFileRepo) {
        super(sysFileRepo);
        this.sysFileRepo = sysFileRepo;
    }

    @Override
    protected Class getCurrentClass() {
        return SysFile.class;
    }

}
