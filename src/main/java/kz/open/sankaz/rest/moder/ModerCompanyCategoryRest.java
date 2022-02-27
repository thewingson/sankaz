package kz.open.sankaz.rest.moder;

import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.CompanyCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_MODERATOR')")
@RestController
@RequestMapping("/moders/comp-cat")
public class ModerCompanyCategoryRest {

    private final CompanyCategoryService companyCategoryService;

    @Autowired
    public ModerCompanyCategoryRest(CompanyCategoryService companyCategoryService) {
        this.companyCategoryService = companyCategoryService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            return ResponseModel.success(companyCategoryService.getAll());
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{catId}")
    public ResponseEntity<?> getOne(@PathVariable("catId") Long catId) {
        try {
            return ResponseModel.success(companyCategoryService.getOne(catId));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

}
