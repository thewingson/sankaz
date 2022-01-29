package kz.open.sankaz.service.impl;

import kz.open.sankaz.dto.HyperLinkDto;
import kz.open.sankaz.mapper.HyperLinkMapper;
import kz.open.sankaz.model.HyperLink;
import kz.open.sankaz.model.HyperLinkType;
import kz.open.sankaz.repo.HyperLinkRepo;
import kz.open.sankaz.service.HyperLinkService;
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
public class HyperLinkServiceImpl extends AbstractService<HyperLink, HyperLinkRepo> implements HyperLinkService {

    private final HyperLinkRepo hyperLinkRepo;

    @Autowired
    private HyperLinkMapper hyperLinkMapper;

    @Autowired
    private HyperLinkTypeService linkTypeService;


    public HyperLinkServiceImpl(HyperLinkRepo hyperLinkRepo) {
        super(hyperLinkRepo);
        this.hyperLinkRepo = hyperLinkRepo;
    }

    @Override
    public HyperLinkDto getOneDto(Long id) {
        HyperLink one = getOne(id);
        return hyperLinkMapper.hyperLinkToDtoWithType(one);
    }

    @Override
    public List<HyperLinkDto> getAllDto() {
        return hyperLinkMapper.hyperLinkToDtoWithType(getAll());
    }

    @Override
    public List<HyperLinkDto> getAllDto(Map<String, Object> params) {
        return hyperLinkMapper.hyperLinkToDtoWithType(getAll(params));
    }

    @Override
    public HyperLink addOneDto(HyperLinkDto hyperLinkDto) {
        log.info("SERVICE -> HyperLinkServiceImpl.addOneDto()");
        HyperLink hyperLink = new HyperLink();
        hyperLink.setValue(hyperLinkDto.getValue());

        HyperLinkType linkType = linkTypeService.getOneByCode(hyperLinkDto.getLinkTypeCode());
        hyperLink.setLinkType(linkType);

        return addOne(hyperLink);
    }

    @Override
    public HyperLink updateOneDto(Long id, HyperLinkDto hyperLinkDto) {
        log.info("SERVICE -> HyperLinkServiceImpl.updateOneDto()");
        HyperLink hyperLink = getOne(id);
        if(hyperLinkDto.getValue() != null && !hyperLink.getValue().equals(hyperLinkDto.getValue())){
            hyperLink.setValue(hyperLinkDto.getValue());
        }
        if(hyperLinkDto.getLinkTypeCode() != null && !hyperLink.getLinkType().getCode().equals(hyperLinkDto.getLinkTypeCode())){
            HyperLinkType linkType = linkTypeService.getOneByCode(hyperLinkDto.getLinkTypeCode());
            hyperLink.setLinkType(linkType);
        }
        return editOneById(hyperLink);
    }

    @Override
    public HyperLink updateOneDto(Map<String, Object> params, HyperLinkDto hyperLinkDto) {
        // Backlog: потом, с помощью JOOQ
        return null;
    }

    @Override
    protected Class getCurrentClass() {
        return HyperLink.class;
    }

}
