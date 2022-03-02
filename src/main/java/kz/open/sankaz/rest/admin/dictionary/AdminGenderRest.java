package kz.open.sankaz.rest.admin.dictionary;

import kz.open.sankaz.pojo.filter.DictionaryLangFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.GenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/admin/dict/genders")
public class AdminGenderRest {

    private final GenderService genderService;

    @Autowired
    public AdminGenderRest(GenderService genderService) {
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

    @PostMapping
    public ResponseEntity<?> addOne(@Valid @RequestBody DictionaryLangFilter filter) {
        try{
            return ResponseModel.success(genderService.addOne(filter));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{genderId}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "genderId") Long genderId) {
        try{
            genderService.deleteOneById(genderId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{genderId}")
    public ResponseEntity<?> editOneById(@PathVariable(name = "genderId") Long genderId,
                                         @Valid @RequestBody DictionaryLangFilter filter) {
        try{
            return ResponseModel.success(genderService.updateOne(genderId, filter));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
