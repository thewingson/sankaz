package kz.open.sankaz.service;

import kz.open.sankaz.dto.ProgramDayDto;
import kz.open.sankaz.dto.ProgramServiceDto;
import kz.open.sankaz.dto.TourDto;
import kz.open.sankaz.dto.TourProgramDto;
import kz.open.sankaz.model.ProgramDay;
import kz.open.sankaz.model.ProgramService;
import kz.open.sankaz.model.Tour;
import kz.open.sankaz.model.TourProgram;

import java.util.List;

public interface TourService extends CommonService<Tour>, CommonDtoService<Tour, TourDto> {
    /***
     * for Entity
     */
    TourProgram add(TourProgram program);
    ProgramService add(ProgramService service);
    ProgramDay add(ProgramDay day);

    TourProgram editOne(TourProgram program);
    ProgramService editOne(ProgramService service);
    ProgramDay editOne(ProgramDay day);

    void deleteProgram(Long id);
    void deleteProgram(TourProgram program);
    void deleteService(Long id);
    void deleteService(ProgramService service);
    void deleteDay(Long id);
    void deleteDay(ProgramDay day);

    TourProgram getProgram(Long id);
    ProgramService getService(Long id);
    ProgramDay getDay(Long id);


    /***
     * for DTO
     */
    TourProgram addProgramDto(Tour tour, TourProgramDto programDto);
    ProgramService addServiceDto(TourProgram program, ProgramServiceDto serviceDto);
    ProgramDay addDayDto(TourProgram program, ProgramDayDto dayDto);

    List<TourProgram> addProgramDto(Long tourId, List<TourProgramDto> programDtos);
    TourProgram addProgramDto(Long tourId, TourProgramDto programDto);
    List<ProgramService> addServiceDto(Long programId, List<ProgramServiceDto> serviceDtos);
    ProgramService addServiceDto(Long programId, ProgramServiceDto serviceDto);
    List<ProgramDay> addDayDto(Long programId, List<ProgramDayDto> dayDtos);
    ProgramDay addDayDto(Long programId, ProgramDayDto dayDto);

    TourProgram updateProgramDto(Long programId, TourProgramDto programDto);
    ProgramService updateServiceDto(Long serviceId, ProgramServiceDto serviceDto);
    ProgramDay updateDayDto(Long dayId, ProgramDayDto dayDto);
}
