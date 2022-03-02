package kz.open.sankaz.rest.dictionary;

import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/dict/cities")
public class CityRest {

    private final CityService cityService;

    @Autowired
    public CityRest(CityService cityService) {
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

}
