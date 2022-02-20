package kz.open.sankaz.rest.admin;

import kz.open.sankaz.mapper.SanMapper;
import kz.open.sankaz.pojo.filter.DeletePicsFilter;
import kz.open.sankaz.pojo.filter.SanCreateFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.SanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/admin/sans")
public class AdminSanRest {

    private final SanService sanService;

    @Autowired
    private SanMapper sanMapper;

    @Autowired
    public AdminSanRest(SanService sanService) {
        this.sanService = sanService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try{
            return ResponseModel.success(sanMapper.sanToAdminDto(sanService.getAll()));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{sanId}")
    public ResponseEntity<?> getOneById(@PathVariable(name = "sanId") Long sanId) {
        try{
            return ResponseModel.success(sanMapper.sanToAdminDto(sanService.getOne(sanId)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@Valid @RequestBody SanCreateFilter filter) {
        try{
            return ResponseModel.success(sanMapper.sanToDto(sanService.createSan(filter)));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{sanId}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "sanId") Long sanId) {
        try{
            sanService.deleteOneById(sanId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}")
    public ResponseEntity<?> editOneById(@PathVariable(name = "sanId") Long sanId,
                                         @Valid @RequestBody SanCreateFilter filter) {
        try{
            sanService.updateOneDto(sanId, filter);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{sanId}/pics/list")
    public ResponseEntity<?> addPics(@PathVariable(name = "sanId") Long sanId,
                                         @RequestParam("pics") MultipartFile[] pics) {
        try {
            return ResponseModel.success(sanMapper.fileToDto(sanService.addPics(sanId, pics)));
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("{sanId}/pics/list")
    public ResponseEntity<?> deletePics(@PathVariable(name = "sanId") Long sanId,
                                           @Valid @RequestBody DeletePicsFilter filter) {
        try {
            sanService.deletePics(sanId, filter.getPicIds());
            return ResponseModel.successPure();
        } catch (Exception e) {
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
