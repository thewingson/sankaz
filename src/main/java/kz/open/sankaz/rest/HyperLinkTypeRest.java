package kz.open.sankaz.rest;

import kz.open.sankaz.pojo.dto.HyperLinkTypeDto;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.HyperLinkTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hyper-link-type")
public class HyperLinkTypeRest {

    @Autowired
    private HyperLinkTypeService linkTypeService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        try{
            return ResponseModel.success(linkTypeService.getAllDto());
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneById(@PathVariable(name = "id") Long id) {
        try{
            return ResponseModel.success(linkTypeService.getOneDto(id));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@RequestBody HyperLinkTypeDto linkTypeDto) {
        try{
            return ResponseModel.success(linkTypeService.addOneDto(linkTypeDto));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "id") Long id) {
        try{
            linkTypeService.deleteOneByIdSoft(id);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editOneById(@PathVariable(name = "id") Long id,
                                         @RequestBody HyperLinkTypeDto linkTypeDto) {
        try{
            linkTypeService.updateOneDto(id, linkTypeDto);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
