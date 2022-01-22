package kz.open.sankaz.rest;

import kz.open.sankaz.dto.ProgramDayDto;
import kz.open.sankaz.dto.ProgramServiceDto;
import kz.open.sankaz.dto.TourDto;
import kz.open.sankaz.dto.TourProgramDto;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, Object> params = new HashMap<>();
        params.put("deleted", false);
        try{
            return ResponseModel.success(tourService.getAllDto(params));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneById(@PathVariable(name = "id") Long id) {
        try{
            return ResponseModel.success(tourService.getOneDto(id));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@RequestBody TourDto tourDto) {
        try{
            return ResponseModel.success(tourService.addOneDto(tourDto));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "id") Long id) {
        try{
            tourService.deleteOneById(id);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editOneById(@PathVariable(name = "id") Long id,
                                         @RequestBody TourDto tourDto) {
        try{
            tourService.updateOneDto(id, tourDto);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{id}/programs")
    public ResponseEntity<?> addPrograms(@PathVariable(name = "id") Long id,
                                      @RequestBody List<TourProgramDto> tourProgramDtos) {
        try{
            tourService.addProgramDto(id, tourProgramDtos);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/programs/{id}/days")
    public ResponseEntity<?> addDays(@PathVariable(name = "id") Long id,
                                         @RequestBody List<ProgramDayDto> dayDtos) {
        try{
            tourService.addDayDto(id, dayDtos);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/programs/{id}/services")
    public ResponseEntity<?> addServices(@PathVariable(name = "id") Long id,
                                     @RequestBody List<ProgramServiceDto> serviceDtos) {
        try{
            tourService.addServiceDto(id, serviceDtos);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/programs/{programId}")
    public ResponseEntity<?> editProgram(@PathVariable(name = "programId") Long programId,
                                         @RequestBody TourProgramDto programDto) {
        try{
            tourService.updateProgramDto(programId, programDto);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/programs/services/{serviceId}")
    public ResponseEntity<?> editService(@PathVariable(name = "serviceId") Long serviceId,
                                         @RequestBody ProgramServiceDto serviceDto) {
        try{
            tourService.updateServiceDto(serviceId, serviceDto);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/programs/days/{dayId}")
    public ResponseEntity<?> editDay(@PathVariable(name = "dayId") Long dayId,
                                         @RequestBody ProgramDayDto dayDto) {
        try{
            tourService.updateDayDto(dayId, dayDto);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/programs/{programId}")
    public ResponseEntity<?> deleteProgram(@PathVariable(name = "programId") Long programId) {
        try{
            tourService.deleteProgram(programId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/programs/services/{serviceId}")
    public ResponseEntity<?> deleteService(@PathVariable(name = "serviceId") Long serviceId) {
        try{
            tourService.deleteService(serviceId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/programs/days/{dayId}")
    public ResponseEntity<?> deleteDay(@PathVariable(name = "dayId") Long dayId) {
        try{
            tourService.deleteDay(dayId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
