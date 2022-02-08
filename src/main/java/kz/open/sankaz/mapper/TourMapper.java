package kz.open.sankaz.mapper;

import kz.open.sankaz.pojo.dto.*;
import kz.open.sankaz.model.*;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TourMapper {

    @Named("tourToDto")
    @Mapping(target = "programs", ignore = true)
    abstract public TourDto tourToDto(Tour tour);
    @IterableMapping(qualifiedByName = "tourToDto")
    abstract public List<TourDto> tourToDto(List<Tour> tours);

    @Named("tourProgramToDto")
    @Mapping(target = "tour", ignore = true)
    @Mapping(target = "days", ignore = true)
    @Mapping(target = "services", ignore = true)
    abstract public TourProgramDto tourProgramToDto(TourProgram program);
    @IterableMapping(qualifiedByName = "tourProgramToDto")
    abstract public List<TourProgramDto> tourProgramToDto(List<TourProgram> programs);

    @Named("tourProgramToDtoWithAllAndNoTour")
    @Mapping(target = "tour", ignore = true)
    @Mapping(target = "days", expression = "java(programDayToDto(program.getDays()))")
    @Mapping(target = "services", expression = "java(programServiceToDto(program.getServices()))")
    abstract public TourProgramDto tourProgramToDtoWithAllAndNoTour(TourProgram program);
    @IterableMapping(qualifiedByName = "tourProgramToDtoWithAllAndNoTour")
    abstract public List<TourProgramDto> tourProgramToDtoWithAllAndNoTour(List<TourProgram> programs);

    @Named("tourProgramToDtoWithAll")
    @Mapping(target = "tour", expression = "java(tourToDto(program.getTour()))")
    @Mapping(target = "days", expression = "java(programDayToDto(program.getDays()))")
    @Mapping(target = "services", expression = "java(programServiceToDto(program.getServices()))")
    abstract public TourProgramDto tourProgramToDtoWithAll(TourProgram program);
    @IterableMapping(qualifiedByName = "tourProgramToDtoWithAll")
    abstract public List<TourProgramDto> tourProgramToDtoWithAll(List<TourProgram> programs);

    @Named("programDayToDto")
    @Mapping(target = "program", ignore = true)
    abstract public ProgramDayDto programDayToDto(ProgramDay day);
    @IterableMapping(qualifiedByName = "programDayToDto")
    abstract public List<ProgramDayDto> programDayToDto(List<ProgramDay> days);

    @Named("programServiceToDto")
    @Mapping(target = "program", ignore = true)
    abstract public ProgramServiceDto programServiceToDto(ProgramService service);
    @IterableMapping(qualifiedByName = "programServiceToDto")
    abstract public List<ProgramServiceDto> programServiceToDto(List<ProgramService> services);

    @Named("tourToDtoWithAll")
    @Mapping(target = "programs", expression = "java(tourProgramToDtoWithAllAndNoTour(tour.getPrograms()))")
    abstract public TourDto tourToDtoWithAll(Tour tour);
    @IterableMapping(qualifiedByName = "tourToDtoWithAll")
    abstract public List<TourDto> tourToDtoWithAll(List<Tour> tours);

}
