package kz.open.sankaz.rest.dictionary;

import kz.open.sankaz.pojo.dto.CompanyCategoryDto;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.CompanyCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comp-categories")
public class CompanyCategoryRest {
    @Autowired
    private CompanyCategoryService companyCategoryService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        try{
            return ResponseModel.success(companyCategoryService.getAll());
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneById(@PathVariable(name = "id") Long id) {
        try{
            return ResponseModel.success(companyCategoryService.getOne(id));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@RequestBody CompanyCategoryDto categoryDto) {
        try{
            return ResponseModel.success(companyCategoryService.addOneDto(categoryDto));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "id") Long id) {
        try{
            companyCategoryService.deleteOneByIdSoft(id);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editOneById(@PathVariable(name = "id") Long id,
                                         @RequestBody CompanyCategoryDto categoryDto) {
        try{
            companyCategoryService.updateOneDto(id, categoryDto);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
