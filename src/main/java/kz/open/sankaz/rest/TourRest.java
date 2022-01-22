package kz.open.sankaz.rest;

import kz.open.sankaz.dto.ProgramDayDto;
import kz.open.sankaz.dto.ProgramServiceDto;
import kz.open.sankaz.dto.TourDto;
import kz.open.sankaz.dto.TourProgramDto;
import kz.open.sankaz.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tours")
public class TourRest {

    private final TourService tourService;

    @Autowired
    public TourRest(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(tourService.getAllDto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(tourService.getOneDto(id));
    }

    @PostMapping
    public ResponseEntity<?> addOne(@RequestBody TourDto tourDto) {
        try {
            tourService.addOneDto(tourDto);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "id") Long id) {
        try {
            tourService.deleteOneById(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editOneById(@PathVariable(name = "id") Long id,
                                         @RequestBody TourDto tourDto) {
        try {
            tourService.updateOneDto(id, tourDto);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/programs")
    public ResponseEntity<?> addPrograms(@PathVariable(name = "id") Long id,
                                      @RequestBody List<TourProgramDto> tourProgramDtos) {
        try {
            tourService.addProgramDto(id, tourProgramDtos);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/programs/{id}/days")
    public ResponseEntity<?> addDays(@PathVariable(name = "id") Long id,
                                         @RequestBody List<ProgramDayDto> dayDtos) {
        try {
            tourService.addDayDto(id, dayDtos);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/programs/{id}/services")
    public ResponseEntity<?> addServices(@PathVariable(name = "id") Long id,
                                     @RequestBody List<ProgramServiceDto> serviceDtos) {
        try {
            tourService.addServiceDto(id, serviceDtos);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
