package kz.open.sankaz.rest.admin;

import kz.open.sankaz.mapper.CategoryMapper;
import kz.open.sankaz.pojo.dto.SanTypeDto;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.SanTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/admin/san-types")
public class AdminSanTypeRest {
    @Autowired
    private SanTypeService sanTypeService;

    @Autowired
    private CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<?> getAll() {
        try{
            return ResponseModel.success(categoryMapper.sanTypeToDto(sanTypeService.getAll()));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneById(@PathVariable(name = "id") Long id) {
        try{
            return ResponseModel.success(categoryMapper.sanTypeToDto(sanTypeService.getOne(id)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@RequestBody SanTypeDto sanTypeDto) {
        try{
            return ResponseModel.success(sanTypeService.addOneDto(sanTypeDto));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "id") Long id) {
        try{
            sanTypeService.deleteOneByIdSoft(id);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editOneById(@PathVariable(name = "id") Long id,
                                         @RequestBody SanTypeDto sanTypeDto) {
        try{
            sanTypeService.updateOneDto(id, sanTypeDto);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
