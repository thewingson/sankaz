package kz.open.sankaz.rest.admin;

import kz.open.sankaz.mapper.SanMapper;
import kz.open.sankaz.pojo.filter.SanForMainFilter;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.AuthService;
import kz.open.sankaz.service.SanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/admin/sans")
public class AdminSanRest {

    private final SanService sanService;

    @Autowired
    private AuthService authService;

    @Autowired
    private SanMapper sanMapper;

    @Autowired
    public AdminSanRest(SanService sanService) {
        this.sanService = sanService;
    }

    @PostMapping
    public ResponseEntity<?> getAll(
            HttpServletRequest request,
            @RequestParam(required = false) Long cityId,
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String sanTypeCode,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        try{
            Long userId = authService.getUserId(request);
            SanForMainFilter filter =
                    new SanForMainFilter(cityId, name, sanTypeCode, null, null, null, null);
            return ResponseModel.success(sanService.getAllForMainAdmin(userId, filter, page, size));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
