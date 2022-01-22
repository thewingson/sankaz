package kz.open.sankaz.service.impl;

import kz.open.sankaz.dto.HyperLinkDto;
import kz.open.sankaz.listener.event.CreateEvent;
import kz.open.sankaz.listener.event.DeleteEvent;
import kz.open.sankaz.listener.event.UpdateEvent;
import kz.open.sankaz.mapper.HyperLinkMapper;
import kz.open.sankaz.model.HyperLink;
import kz.open.sankaz.model.HyperLinkType;
import kz.open.sankaz.repo.HyperLinkRepo;
import kz.open.sankaz.repo.HyperLinkTypeRepo;
import kz.open.sankaz.service.HyperLinkService;
import kz.open.sankaz.service.SanService;
import kz.open.sankaz.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
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
    private HyperLinkTypeRepo hyperLinkTypeRepo;

    @Autowired
    private HyperLinkMapper hyperLinkMapper;

    @Autowired
    private SanService sanService;

    @Autowired
    private UserService userService;


    public HyperLinkServiceImpl(HyperLinkRepo hyperLinkRepo) {
        super(hyperLinkRepo);
        this.hyperLinkRepo = hyperLinkRepo;
    }

    @Override
    public HyperLinkDto getOneDto(Long id) {
        HyperLink one = getOne(id);
        return hyperLinkMapper.hyperLinkToDto(one);
    }

    @Override
    public List<HyperLinkDto> getAllDto() {
        return hyperLinkMapper.hyperLinkToDto(getAll());
    }

    @Override
    public List<HyperLinkDto> getAllDto(Map<String, Object> params) {
        return hyperLinkMapper.hyperLinkToDto(getAll(params));
    }

    @Override
    public HyperLink addOneDto(HyperLinkDto hyperLinkDto) {
        log.info("SERVICE -> HyperLinkServiceImpl.addOneDto()");
        HyperLink hyperLink = new HyperLink();
        hyperLink.setValue(hyperLinkDto.getValue());
        hyperLink.setItemType(hyperLinkDto.getItemType());
        hyperLink.setItemId(hyperLinkDto.getItemId());

        HyperLinkType linkType = hyperLinkTypeRepo.getByCode(hyperLinkDto.getLinkTypeCode());// TODO: Exception or validation
        hyperLink.setLinkType(linkType);

        return addOne(hyperLink);
    }

    @Override
    public HyperLink updateOneDto(Long id, HyperLinkDto hyperLinkDto) {
        log.info("SERVICE -> HyperLinkServiceImpl.updateOneDto()");
        HyperLink hyperLink = getOne(id);
        if(hyperLinkDto.getItemType() != null && !hyperLink.getItemType().equals(hyperLinkDto.getItemType())){
            hyperLink.setItemType(hyperLinkDto.getItemType());
        }
        if(hyperLinkDto.getItemId() != null && !hyperLink.getItemId().equals(hyperLinkDto.getItemId())){
            hyperLink.setItemId(hyperLinkDto.getItemId());
        }
        if(hyperLinkDto.getValue() != null && !hyperLink.getValue().equals(hyperLinkDto.getValue())){
            hyperLink.setValue(hyperLinkDto.getValue());
        }
        if(hyperLinkDto.getLinkTypeCode() != null && !hyperLink.getLinkType().getCode().equals(hyperLinkDto.getLinkTypeCode())){
            HyperLinkType linkType = hyperLinkTypeRepo.getByCode(hyperLinkDto.getLinkTypeCode());
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

    @Override
    protected ApplicationEvent getCreateEvent(HyperLink hyperLink) {
        return new CreateEvent(hyperLink);
    }

    @Override
    protected ApplicationEvent getDeleteEvent(HyperLink hyperLink) {
        return new DeleteEvent(hyperLink);
    }

    @Override
    protected ApplicationEvent getUpdateEvent(HyperLink hyperLink) {
        return new UpdateEvent(hyperLink);
    }
}
