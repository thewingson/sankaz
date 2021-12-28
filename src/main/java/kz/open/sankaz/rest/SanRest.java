package kz.open.sankaz.rest;

import kz.open.sankaz.model.San;
import kz.open.sankaz.repo.SanRepo;
import kz.open.sankaz.service.AuthService;
import kz.open.sankaz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/sans")
public class SanRest {

    private final SanRepo sanRepo;

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @Autowired
    public SanRest(SanRepo sanRepo) {
        this.sanRepo = sanRepo;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(sanRepo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(sanRepo.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> addOne(@RequestBody San san) {
        try {
            san.setCreatedBy(authService.getCurrentUser().getUsername());
            sanRepo.save(san);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editOneById(@PathVariable(name = "id") Long id,
                                         @RequestBody San san) {
        try {
            Optional<San> sanById = sanRepo.findById(id);
            if(sanById.isPresent()){
                San sanToEdit = sanById.get();
                sanToEdit.setName(san.getName());
                sanToEdit.setDescription(san.getDescription());
                sanToEdit.setUpdateTs(LocalDateTime.now());
                sanToEdit.setUpdatedBy("admin");
                sanRepo.save(sanToEdit);
            } else{
                sanRepo.save(san);
            }
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "id") Long id) {
        try {
            sanRepo.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
