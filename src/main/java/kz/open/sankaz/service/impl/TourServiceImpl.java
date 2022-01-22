package kz.open.sankaz.service.impl;

import kz.open.sankaz.dto.ProgramDayDto;
import kz.open.sankaz.dto.ProgramServiceDto;
import kz.open.sankaz.dto.TourDto;
import kz.open.sankaz.dto.TourProgramDto;
import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.listener.event.CreateEvent;
import kz.open.sankaz.listener.event.DeleteEvent;
import kz.open.sankaz.listener.event.UpdateEvent;
import kz.open.sankaz.mapper.TourMapper;
import kz.open.sankaz.model.ProgramDay;
import kz.open.sankaz.model.ProgramService;
import kz.open.sankaz.model.Tour;
import kz.open.sankaz.model.TourProgram;
import kz.open.sankaz.repo.ProgramDayRepo;
import kz.open.sankaz.repo.ProgramServiceRepo;
import kz.open.sankaz.repo.TourProgramRepo;
import kz.open.sankaz.repo.TourRepo;
import kz.open.sankaz.service.TourService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class TourServiceImpl extends AbstractService<Tour, TourRepo> implements TourService {

    private final TourRepo tourRepo;

    @Autowired
    private TourProgramRepo programRepo;

    @Autowired
    private ProgramServiceRepo serviceRepo;

    @Autowired
    private ProgramDayRepo dayRepo;

    @Autowired
    private TourMapper tourMapper;


    public TourServiceImpl(TourRepo tourRepo) {
        super(tourRepo);
        this.tourRepo = tourRepo;
    }

    @Override
    public TourProgram getProgramOne(Long id) {
        Optional<TourProgram> entityById = programRepo.findById(id);
        if(!entityById.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("ID", id);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return entityById.get();
    }

    @Override
    public TourProgram addOne(TourProgram program) {
//        ApplicationEvent createEvent = getCreateEvent(entity);
//        if(createEvent != null){
//            applicationEventPublisher.publishEvent(createEvent);
//        }

        return programRepo.save(program);
    }

    @Override
    public ProgramService addOne(ProgramService service) {
//        ApplicationEvent createEvent = getCreateEvent(entity);
//        if(createEvent != null){
//            applicationEventPublisher.publishEvent(createEvent);
//        }

        return serviceRepo.save(service);
    }

    @Override
    public ProgramDay addOne(ProgramDay day) {
//        ApplicationEvent createEvent = getCreateEvent(entity);
//        if(createEvent != null){
//            applicationEventPublisher.publishEvent(createEvent);
//        }

        return dayRepo.save(day);
    }

    @Override
    public TourDto getOneDto(Long id) {
        Tour one = getOne(id);
        return tourMapper.tourToDtoWithAll(one);
    }

    @Override
    public List<TourDto> getAllDto() {
        return tourMapper.tourToDtoWithAll(getAll());
    }

    @Override
    public Tour addOneDto(TourDto tourDto) {
        log.info("SERVICE -> TourServiceImpl.addOneDto(TourDto)");
        Tour tour = new Tour();
        tour.setName(tourDto.getName());
        tour.setDescription(tourDto.getDescription());

//        addOne(tour);
        tourDto.getPrograms().forEach(programDto -> addProgramDto(tour, programDto));

        return addOne(tour);
//        return tour;
    }

    @Override
    public TourProgram addProgramDto(Tour tour, TourProgramDto programDto) {
        log.info("SERVICE -> TourServiceImpl.addOneDto(TourProgramDto)");
        TourProgram program = new TourProgram();
        program.setPrice(programDto.getPrice());
        program.setShortDescription(programDto.getShortDescription());
        program.setTour(tour);

//        addOne(program);
        programDto.getDays().forEach(dayDto -> addDayDto(program, dayDto));
        programDto.getServices().forEach(serviceDto -> addServiceDto(program, serviceDto));

        return addOne(program);
//        return program;
    }

    @Override
    public ProgramDay addDayDto(TourProgram program, ProgramDayDto dayDto) {
        log.info("SERVICE -> TourServiceImpl.addOneDto(ProgramDayDto)");
        ProgramDay day = new ProgramDay();
        day.setDescription(dayDto.getDescription());
        day.setProgram(program);

        return addOne(day);
    }

    @Override
    public ProgramService addServiceDto(TourProgram program, ProgramServiceDto serviceDto) {
        log.info("SERVICE -> TourServiceImpl.addOneDto(ProgramServiceDto)");
        ProgramService service = new ProgramService();
        service.setName(serviceDto.getName());
        service.setPrice(serviceDto.getPrice());
        service.setProgram(program);

        return addOne(service);
    }

    @Override
    public TourProgram addProgramDto(Long tourId, TourProgramDto programDto) {
        Tour tour = getOne(tourId);
        return addProgramDto(tour, programDto);
    }

    @Override
    public List<TourProgram> addProgramDto(Long tourId, List<TourProgramDto> programDtos) {
        return programDtos.stream().map(programDto -> addProgramDto(tourId, programDto)).collect(Collectors.toList());
    }

    @Override
    public ProgramService addServiceDto(Long programId, ProgramServiceDto serviceDto) {
        TourProgram program = getProgramOne(programId);
        return addServiceDto(program, serviceDto);
    }


    @Override
    public List<ProgramService> addServiceDto(Long programId, List<ProgramServiceDto> serviceDtos) {
        return serviceDtos.stream().map(serviceDto -> addServiceDto(programId, serviceDto)).collect(Collectors.toList());
    }

    @Override
    public ProgramDay addDayDto(Long programId, ProgramDayDto dayDto) {
        TourProgram program = getProgramOne(programId);
        return addDayDto(program, dayDto);
    }

    @Override
    public List<ProgramDay> addDayDto(Long programId, List<ProgramDayDto> dayDtos) {
        return dayDtos.stream().map(dayDto -> addDayDto(programId, dayDto)).collect(Collectors.toList());
    }

    @Override
    public Tour updateOneDto(Long id, TourDto tourDto) {
        log.info("SERVICE -> TourServiceImpl.updateOneDto()");
        Tour tour = getOne(id);
        if(tourDto.getName() != null){
            tour.setName(tourDto.getName());
        }
        if(tourDto.getDescription() != null){
            tour.setDescription(tourDto.getDescription());
        }
        return editOneById(tour);
    }

    @Override
    public Tour updateOneDto(Map<String, Object> params, TourDto tourDto) {
        // Backlog: потом, с помощью JOOQ
        return null;
    }

    @Override
    protected Class getCurrentClass() {
        return Tour.class;
    }

    @Override
    protected ApplicationEvent getCreateEvent(Tour tour) {
        return new CreateEvent(tour);
    }

    @Override
    protected ApplicationEvent getDeleteEvent(Tour tour) {
        return new DeleteEvent(tour);
    }

    @Override
    protected ApplicationEvent getUpdateEvent(Tour tour) {
        return new UpdateEvent(tour);
    }
}
