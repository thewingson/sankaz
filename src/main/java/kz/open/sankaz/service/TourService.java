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
    TourProgram addOne(TourProgram program);
    ProgramService addOne(ProgramService service);
    ProgramDay addOne(ProgramDay day);

    TourProgram getProgramOne(Long id);


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
}
