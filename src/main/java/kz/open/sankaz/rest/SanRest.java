package kz.open.sankaz.rest;

import kz.open.sankaz.dto.ReviewDto;
import kz.open.sankaz.dto.RoomDto;
import kz.open.sankaz.dto.SanDto;
import kz.open.sankaz.service.SanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sans")
public class SanRest {

    private final SanService sanService;

    @Autowired
    public SanRest(SanService sanService) {
        this.sanService = sanService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(sanService.getAllDto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(sanService.getOneDto(id));
    }

    @PostMapping
    public ResponseEntity<?> addOne(@RequestBody SanDto sanDto) {
        try {
            sanService.addOneDto(sanDto);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "id") Long id) {
        try {
            sanService.deleteOneById(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editOneById(@PathVariable(name = "id") Long id,
                                         @RequestBody SanDto sanDto) {
        try {
            sanService.updateOneDto(id, sanDto);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/rooms")
    public ResponseEntity<?> addRooms(@PathVariable(name = "id") Long id,
                                      @RequestBody List<RoomDto> roomDtos) {
        try {
            sanService.addRoomsDto(id, roomDtos);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<?> addReviews(@PathVariable(name = "id") Long id,
                                      @RequestBody List<ReviewDto> reviewDtos) {
        try {
            sanService.addReviewsDto(id, reviewDtos);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
