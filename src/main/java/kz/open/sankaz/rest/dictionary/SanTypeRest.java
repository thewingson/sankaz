package kz.open.sankaz.rest.dictionary;

import kz.open.sankaz.mapper.CategoryMapper;
import kz.open.sankaz.pojo.dto.SanTypeDto;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.SanTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dict/san-types")
public class SanTypeRest {
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

}
