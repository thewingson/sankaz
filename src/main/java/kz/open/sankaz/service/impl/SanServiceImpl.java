package kz.open.sankaz.service.impl;

import kz.open.sankaz.dto.*;
import kz.open.sankaz.listener.event.CreateEvent;
import kz.open.sankaz.listener.event.DeleteEvent;
import kz.open.sankaz.listener.event.UpdateEvent;
import kz.open.sankaz.mapper.ReviewMapper;
import kz.open.sankaz.mapper.SanMapper;
import kz.open.sankaz.model.Category;
import kz.open.sankaz.model.San;
import kz.open.sankaz.repo.SanRepo;
import kz.open.sankaz.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class SanServiceImpl extends AbstractService<San, SanRepo> implements SanService {

    private final SanRepo sanRepo;

    @Lazy
    @Autowired
    private CategoryService categoryService;

    @Lazy
    @Autowired
    private RoomService roomService;

    @Lazy
    @Autowired
    private ReviewService reviewService;

    @Lazy
    @Autowired
    private HyperLinkService hyperLinkService;

    @Autowired
    private SanMapper sanMapper;

    @Autowired
    private ReviewMapper reviewMapper;


    public SanServiceImpl(SanRepo sanRepo) {
        super(sanRepo);
        this.sanRepo = sanRepo;
    }

    @Override
    public SanDto getOneDto(Long id) {
        San one = getOne(id);
        return sanMapper.sanToDto(one);
    }

    @Override
    public List<SanDto> getAllDto() {
//        return sanMapper.sanToDto(getAll());
        return sanMapper.sanToDtoWithAll(getAll());
    }

    @Override
    public List<SanDto> getAllDto(Map<String, Object> params) {
        return sanMapper.sanToDto(getAll(params));
    }

    @Override
    public San addOneDto(SanDto sanDto) {
        log.info("SERVICE -> SanServiceImpl.addOneDto()");
        San san = new San();
        san.setDescription(sanDto.getDescription());
        san.setName(sanDto.getName());

        List<Category> categoriesByCode = categoryService
                .getAllByCodeIn(sanDto.getCategories().stream().map(CategoryDto::getCode).collect(Collectors.toList()));
        san.setCategories(categoriesByCode);

        return addOne(san);
    }

    @Override
    public San updateOneDto(Long id, SanDto sanDto) {
        log.info("SERVICE -> SanServiceImpl.updateOneDto()");
        San san = getOne(id);
        if(sanDto.getName() != null){
            san.setName(sanDto.getName());
        }
        if(sanDto.getDescription() != null){
            san.setDescription(sanDto.getDescription());
        }

        return editOneById(san);
    }

    @Override
    public San updateOneDto(Map<String, Object> params, SanDto sanDto) {
        // Backlog: потом, с помощью JOOQ
        return null;
    }

    @Override
    public San addRoomsDto(Long id, List<RoomDto> roomDtos) {
        roomDtos.forEach(roomDto -> {
            SanDto sanDto = new SanDto();
            sanDto.setId(id);
            roomDto.setSan(sanDto);
            roomService.addOneDto(roomDto);
        });

        return null;
    }

    @Override
    public San addReviewsDto(Long id, List<ReviewDto> reviewDtos) {
        reviewDtos.forEach(reviewDto -> {
            SanDto sanDto = new SanDto();
            sanDto.setId(id);
            reviewDto.setSan(sanDto);
            reviewService.addOneDto(reviewDto);
        });

        return null;
    }

    @Override
    public San addHyperLinksDto(Long id, List<HyperLinkDto> hyperLinkDtos) {
        hyperLinkDtos.forEach(hyperLinkDto -> {
            hyperLinkDto.setItemType(San.class.toString());
            hyperLinkDto.setId(id);
            hyperLinkService.addOneDto(hyperLinkDto);
        });

        return null;
    }

    @Override
    protected Class getCurrentClass() {
        return Category.class;
    }

    @Override
    protected ApplicationEvent getCreateEvent(San san) {
        return new CreateEvent(san);
    }

    @Override
    protected ApplicationEvent getDeleteEvent(San san) {
        return new DeleteEvent(san);
    }

    @Override
    protected ApplicationEvent getUpdateEvent(San san) {
        return new UpdateEvent(san);
    }
}
