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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
            @RequestParam(value="startDate", required = false) String startDate,
            @RequestParam(value="endDate", required = false) String endDate,
            @RequestParam(required = false) Integer adults,
            @RequestParam(required = false) Integer children,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        try{
            Long userId = authService.getUserId(request);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            SanForMainFilter filter = new SanForMainFilter(cityId, name, sanTypeCode,
                    startDate.isEmpty() ? null : LocalDateTime.parse(startDate, formatter),
                    endDate.isEmpty() ? null : LocalDateTime.parse(endDate, formatter), adults, children);
            return ResponseModel.success(sanService.getAllForMainAdmin(userId, filter, page, size));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
