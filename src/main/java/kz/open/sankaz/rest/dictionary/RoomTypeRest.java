package kz.open.sankaz.rest.dictionary;

import kz.open.sankaz.dto.RoomTypeDto;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room-types")
public class RoomTypeRest {
    @Autowired
    private RoomTypeService roomTypeService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        try{
            return ResponseModel.success(roomTypeService.getAll());
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneById(@PathVariable(name = "id") Long id) {
        try{
            return ResponseModel.success(roomTypeService.getOne(id));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@RequestBody RoomTypeDto dto) {
        try{
            return ResponseModel.success(roomTypeService.addOneDto(dto));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "id") Long id) {
        try{
            roomTypeService.deleteOneByIdSoft(id);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editOneById(@PathVariable(name = "id") Long id,
                                         @RequestBody RoomTypeDto dto) {
        try{
            roomTypeService.updateOneDto(id, dto);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
