package kz.open.sankaz.rest.admin.dictionary;

import kz.open.sankaz.pojo.filter.DictionaryLangFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.ServiceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/admin/dict/ser-cat")
public class AdminServiceCategoryRest {
    @Autowired
    private ServiceCategoryService serviceCategoryService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        try{
            return ResponseModel.success(serviceCategoryService.getAll());
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneById(@PathVariable(name = "id") Long id) {
        try{
            return ResponseModel.success(serviceCategoryService.getOne(id));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@Valid @RequestBody DictionaryLangFilter filter) {
        try{
            return ResponseModel.success(serviceCategoryService.addOne(filter));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "id") Long id) {
        try{
            serviceCategoryService.deleteOneById(id);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> editOneById(@PathVariable(name = "id") Long id,
                                         @Valid @RequestBody DictionaryLangFilter filter) {
        try{
            return ResponseModel.success(serviceCategoryService.updateOne(id, filter));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
