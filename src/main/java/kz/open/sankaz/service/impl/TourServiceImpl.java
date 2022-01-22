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
    public ProgramService getServiceOne(Long id) {
        Optional<ProgramService> entityById = serviceRepo.findById(id);
        if(!entityById.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("ID", id);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return entityById.get();
    }

    @Override
    public ProgramDay getDayOne(Long id) {
        Optional<ProgramDay> entityById = dayRepo.findById(id);
        if(!entityById.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("ID", id);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return entityById.get();
    }

    @Override
    public TourProgram addOne(TourProgram program) {
        ApplicationEvent createEvent = getCreateEvent(program);
        if(createEvent != null){
            applicationEventPublisher.publishEvent(createEvent);
        }

        return programRepo.save(program);
    }

    @Override
    public ProgramService addOne(ProgramService service) {
        ApplicationEvent createEvent = getCreateEvent(service);
        if(createEvent != null){
            applicationEventPublisher.publishEvent(createEvent);
        }

        return serviceRepo.save(service);
    }

    @Override
    public ProgramDay addOne(ProgramDay day) {
        ApplicationEvent createEvent = getCreateEvent(day);
        if(createEvent != null){
            applicationEventPublisher.publishEvent(createEvent);
        }

        return dayRepo.save(day);
    }

    @Override
    public TourProgram editOne(TourProgram program) {
        ApplicationEvent updateEvent = getUpdateEvent(program);
        if(updateEvent != null){
            applicationEventPublisher.publishEvent(updateEvent);
        }

        return programRepo.save(program);
    }

    @Override
    public ProgramService editOne(ProgramService service) {
        ApplicationEvent updateEvent = getUpdateEvent(service);
        if(updateEvent != null){
            applicationEventPublisher.publishEvent(updateEvent);
        }

        return serviceRepo.save(service);
    }

    @Override
    public ProgramDay editOne(ProgramDay day) {
        ApplicationEvent updateEvent = getUpdateEvent(day);
        if(updateEvent != null){
            applicationEventPublisher.publishEvent(updateEvent);
        }

        return dayRepo.save(day);
    }

    @Override
    public void deleteProgram(Long id) {
        TourProgram program = getProgramOne(id);
        deleteProgram(program);
    }

    @Override
    public void deleteProgram(TourProgram program) {
        ApplicationEvent deleteEvent = getDeleteEvent(program);
        if(deleteEvent != null){
            applicationEventPublisher.publishEvent(deleteEvent);
        }

        programRepo.save(program);

        program.getDays().forEach(this::deleteDay);
        program.getServices().forEach(this::deleteService);
    }

    @Override
    public void deleteService(Long id) {
        ProgramService service = getServiceOne(id);
        deleteService(service);
    }

    @Override
    public void deleteService(ProgramService service) {
        ApplicationEvent deleteEvent = getDeleteEvent(service);
        if(deleteEvent != null){
            applicationEventPublisher.publishEvent(deleteEvent);
        }

        serviceRepo.save(service);
    }

    @Override
    public void deleteDay(Long id) {
        ProgramDay day = getDayOne(id);
        deleteDay(day);
    }

    @Override
    public void deleteDay(ProgramDay day) {
        ApplicationEvent deleteEvent = getDeleteEvent(day);
        if(deleteEvent != null){
            applicationEventPublisher.publishEvent(deleteEvent);
        }

        dayRepo.save(day);
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
    public List<TourDto> getAllDto(Map<String, Object> params) {
        return tourMapper.tourToDtoWithAll(getAll(params));
    }

    @Override
    public Tour addOneDto(TourDto tourDto) {
        log.info("SERVICE -> TourServiceImpl.addOneDto(TourDto)");
        Tour tour = new Tour();
        tour.setName(tourDto.getName());
        tour.setDescription(tourDto.getDescription());

        addOne(tour);
        if(tourDto.getPrograms() != null){
            tourDto.getPrograms().forEach(programDto -> addProgramDto(tour, programDto));
        }

        return tour;
    }

    @Override
    public TourProgram addProgramDto(Tour tour, TourProgramDto programDto) {
        log.info("SERVICE -> TourServiceImpl.addOneDto(TourProgramDto)");
        TourProgram program = new TourProgram();
        program.setPrice(programDto.getPrice());
        program.setShortDescription(programDto.getShortDescription());
        program.setTour(tour);

        addOne(program);
        if(programDto.getDays() != null){
            programDto.getDays().forEach(dayDto -> addDayDto(program, dayDto));
        }
        if(programDto.getServices() != null){
            programDto.getServices().forEach(serviceDto -> addServiceDto(program, serviceDto));
        }

        return program;
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
    public TourProgram updateProgramDto(Long programId, TourProgramDto programDto) {
        log.info("SERVICE -> TourServiceImpl.updateProgramDto()");
        TourProgram program = getProgramOne(programId);
        boolean updated = false;
        if(programDto.getShortDescription() != null && !programDto.getShortDescription().equals(program.getShortDescription())){
            program.setShortDescription(programDto.getShortDescription());
            updated = true;
        }
        if(programDto.getPrice() != null && !programDto.getPrice().equals(program.getPrice())){
            program.setPrice(programDto.getPrice());
            updated = true;
        }
        if(programDto.getTour() != null && !programDto.getTour().getId().equals(program.getTour().getId())){
            Tour tour = getOne(programDto.getTour().getId());
            program.setTour(tour);
            updated = true;
        }
        if(updated){
            program = editOne(program);
        }
        return program;
    }

    @Override
    public ProgramService updateServiceDto(Long serviceId, ProgramServiceDto serviceDto) {
        log.info("SERVICE -> TourServiceImpl.updateServiceDto()");
        ProgramService programService = getServiceOne(serviceId);
        boolean updated = false;
        if(serviceDto.getName() != null && !serviceDto.getName().equals(programService.getName())){
            programService.setName(serviceDto.getName());
            updated = true;
        }
        if(serviceDto.getPrice() != null && !serviceDto.getPrice().equals(programService.getPrice())){
            programService.setPrice(serviceDto.getPrice());
            updated = true;
        }
        if(serviceDto.getProgram() != null && !serviceDto.getProgram().getId().equals(programService.getProgram().getId())){
            TourProgram program = getProgramOne(serviceDto.getProgram().getId());
            programService.setProgram(program);
            updated = true;
        }
        if(updated){
            programService = editOne(programService);
        }
        return programService;
    }

    @Override
    public ProgramDay updateDayDto(Long dayId, ProgramDayDto dayDto) {
        log.info("SERVICE -> TourServiceImpl.updateDayDto()");
        ProgramDay day = getDayOne(dayId);
        boolean updated = false;
        if(dayDto.getDescription() != null && !dayDto.getDescription().equals(day.getDescription())){
            day.setDescription(dayDto.getDescription());
            updated = true;
        }
        if(dayDto.getProgram() != null && !dayDto.getProgram().getId().equals(day.getProgram().getId())){
            TourProgram program = getProgramOne(dayDto.getProgram().getId());
            day.setProgram(program);
            updated = true;
        }

        if(updated){
            day = editOne(day);
        }
        return day;
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

    protected ApplicationEvent getCreateEvent(TourProgram program) {
        return new CreateEvent(program);
    }

    protected ApplicationEvent getDeleteEvent(TourProgram program) {
        return new DeleteEvent(program);
    }

    protected ApplicationEvent getUpdateEvent(TourProgram program) {
        return new UpdateEvent(program);
    }

    protected ApplicationEvent getCreateEvent(ProgramService service) {
        return new CreateEvent(service);
    }

    protected ApplicationEvent getDeleteEvent(ProgramService service) {
        return new DeleteEvent(service);
    }

    protected ApplicationEvent getUpdateEvent(ProgramService service) {
        return new UpdateEvent(service);
    }

    protected ApplicationEvent getCreateEvent(ProgramDay day) {
        return new CreateEvent(day);
    }

    protected ApplicationEvent getDeleteEvent(ProgramDay day) {
        return new DeleteEvent(day);
    }

    protected ApplicationEvent getUpdateEvent(ProgramDay day) {
        return new UpdateEvent(day);
    }
}
