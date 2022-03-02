package kz.open.sankaz.rest.admin.dictionary;

import kz.open.sankaz.pojo.filter.DictionaryLangFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/admin/dict/cities")
public class AdminCityRest {

    private final CityService cityService;

    @Autowired
    public AdminCityRest(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            return ResponseModel.success(cityService.getAll());
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<?> getOne(@PathVariable("cityId") Long cityId) {
        try {
            return ResponseModel.success(cityService.getOne(cityId));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@Valid @RequestBody DictionaryLangFilter filter) {
        try{
            return ResponseModel.success(cityService.addOne(filter));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{cityId}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "cityId") Long cityId) {
        try{
            cityService.deleteOneById(cityId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{cityId}")
    public ResponseEntity<?> editOneById(@PathVariable(name = "cityId") Long cityId,
                                         @Valid @RequestBody DictionaryLangFilter filter) {
        try{
            return ResponseModel.success(cityService.updateOne(cityId, filter));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
