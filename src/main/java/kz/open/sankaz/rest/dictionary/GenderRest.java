package kz.open.sankaz.rest.dictionary;

import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/dict/genders")
public class GenderRest {

    private final GenderService genderService;

    @Autowired
    public GenderRest(GenderService genderService) {
        this.genderService = genderService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            return ResponseModel.success(genderService.getAll());
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{genderId}")
    public ResponseEntity<?> getOne(@PathVariable("genderId") Long genderId) {
        try {
            return ResponseModel.success(genderService.getOne(genderId));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

}
