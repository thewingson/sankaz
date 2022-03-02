package kz.open.sankaz.rest.admin.dictionary;

import kz.open.sankaz.pojo.filter.DictionaryLangFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.SanTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/admin/dict/san-types")
public class AdminSanTypeRest {
    @Autowired
    private SanTypeService sanTypeService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        try{
            return ResponseModel.success(sanTypeService.getAll());
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneById(@PathVariable(name = "id") Long id) {
        try{
            return ResponseModel.success(sanTypeService.getOne(id));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@Valid @RequestBody DictionaryLangFilter filter) {
        try{
            return ResponseModel.success(sanTypeService.addOne(filter));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "id") Long id) {
        try{
            sanTypeService.deleteOneById(id);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> editOneById(@PathVariable(name = "id") Long id,
                                         @Valid @RequestBody DictionaryLangFilter filter) {
        try{

            return ResponseModel.success(sanTypeService.updateOne(id, filter));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
