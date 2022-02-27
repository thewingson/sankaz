package kz.open.sankaz.rest.user;

import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.ServiceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@PreAuthorize("hasRole('ROLE_USER')")
@RestController
@RequestMapping("/users/serv-cat")
public class UserServiceCategoryRest {

    private final ServiceCategoryService serviceCategoryService;

    @Autowired
    public UserServiceCategoryRest(ServiceCategoryService serviceCategoryService) {
        this.serviceCategoryService = serviceCategoryService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            return ResponseModel.success(serviceCategoryService.getAll());
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{catId}")
    public ResponseEntity<?> getOne(@PathVariable("catId") Long catId) {
        try {
            return ResponseModel.success(serviceCategoryService.getOne(catId));
        } catch (RuntimeException e) {
            return ResponseModel.error(BAD_REQUEST, e.getMessage());
        }
    }

}
