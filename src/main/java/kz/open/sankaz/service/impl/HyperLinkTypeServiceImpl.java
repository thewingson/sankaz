package kz.open.sankaz.service.impl;

import kz.open.sankaz.dto.HyperLinkTypeDto;
import kz.open.sankaz.mapper.HyperLinkMapper;
import kz.open.sankaz.model.HyperLinkType;
import kz.open.sankaz.repo.HyperLinkTypeRepo;
import kz.open.sankaz.service.HyperLinkTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class HyperLinkTypeServiceImpl extends AbstractDictionaryService<HyperLinkType, HyperLinkTypeRepo> implements HyperLinkTypeService {

    private final HyperLinkTypeRepo HyperLinkTypeRepo;

    @Autowired
    private HyperLinkMapper hyperLinkMapper;


    public HyperLinkTypeServiceImpl(HyperLinkTypeRepo HyperLinkTypeRepo) {
        super(HyperLinkTypeRepo);
        this.HyperLinkTypeRepo = HyperLinkTypeRepo;
    }

    @Override
    public HyperLinkTypeDto getOneDto(Long id) {
        HyperLinkType one = getOne(id);
        return hyperLinkMapper.hyperLinkTypeToDto(one);
    }

    @Override
    public List<HyperLinkTypeDto> getAllDto() {
        return hyperLinkMapper.hyperLinkTypeToDto(getAll());
    }

    @Override
    public List<HyperLinkTypeDto> getAllDto(Map<String, Object> params) {
        return hyperLinkMapper.hyperLinkTypeToDto(getAll(params));
    }

    @Override
    public HyperLinkType addOneDto(HyperLinkTypeDto linkTypeDto) {
        log.info("SERVICE -> HyperLinkTypeServiceImpl.addOneDto()");
        HyperLinkType linkType = new HyperLinkType();
        linkType.setCode(linkTypeDto.getCode());
        linkType.setName(linkTypeDto.getName());
        linkType.setDescription(linkTypeDto.getDescription());

        return addOne(linkType);
    }

    @Override
    public HyperLinkType updateOneDto(Long id, HyperLinkTypeDto linkTypeDto) {
        log.info("SERVICE -> HyperLinkTypeServiceImpl.updateOneDto()");
        HyperLinkType linkType = getOne(id);
        if(linkTypeDto.getCode() != null && !linkType.getCode().equals(linkTypeDto.getCode())){
            linkType.setCode(linkTypeDto.getCode());
        }
        if(linkTypeDto.getName() != null && !linkType.getName().equals(linkTypeDto.getName())){
            linkType.setName(linkTypeDto.getName());
        }
        if(linkTypeDto.getDescription() != null && !linkType.getDescription().equals(linkTypeDto.getDescription())){
            linkType.setDescription(linkTypeDto.getDescription());
        }
        return editOneById(linkType);
    }

    @Override
    public HyperLinkType updateOneDto(Map<String, Object> params, HyperLinkTypeDto linkTypeDto) {
        // Backlog: потом, с помощью JOOQ
        return null;
    }

    @Override
    protected Class getCurrentClass() {
        return HyperLinkType.class;
    }

}
