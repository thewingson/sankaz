package kz.open.sankaz.rest.dictionary;

import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.ServiceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dict/ser-cat")
public class ServiceCategoryRest {
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

}
