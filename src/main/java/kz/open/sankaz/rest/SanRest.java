package kz.open.sankaz.rest;

import kz.open.sankaz.dto.*;
import kz.open.sankaz.response.ResponseModel;
import kz.open.sankaz.service.HyperLinkService;
import kz.open.sankaz.service.ReviewService;
import kz.open.sankaz.service.RoomService;
import kz.open.sankaz.service.SanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/sans")
public class SanRest {

    private final SanService sanService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private HyperLinkService linkService;

    @Autowired
    public SanRest(SanService sanService) {
        this.sanService = sanService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try{
            return ResponseModel.success(sanService.getAllDto());
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneById(@PathVariable(name = "id") Long id) {
        try{
            return ResponseModel.success(sanService.getOneDto(id));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOne(@RequestBody(required = false) SanDto sanDto) {
        try{
            return ResponseModel.success(sanService.addOneDto(sanDto));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOneById(@PathVariable(name = "id") Long id) {
        try{
            sanService.deleteOneByIdSoft(id);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editOneById(@PathVariable(name = "id") Long id,
                                         @RequestBody SanDto sanDto) {
        try{
            sanService.updateOneDto(id, sanDto);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{id}/rooms")
    public ResponseEntity<?> addRoom(@PathVariable(name = "id") Long id,
                                      @RequestBody RoomDto roomDto) {
        try{
            return ResponseModel.success(roomService.addDto(id, roomDto));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{id}/rooms/list")
    public ResponseEntity<?> addRooms(@PathVariable(name = "id") Long id,
                                     @RequestBody List<RoomDto> roomDtos) {
        try{
            return ResponseModel.success(roomService.addDto(id, roomDtos));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/rooms/{roomId}")
    public ResponseEntity<?> updateRoom(@PathVariable(name = "roomId") Long roomId,
                                         @RequestBody RoomDto roomDto) {
        try{
            roomService.updateOneDto(roomId, roomDto);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable(name = "roomId") Long roomId) {
        try{
            roomService.deleteOneByIdSoft(roomId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<?> addReview(@PathVariable(name = "id") Long id,
                                      @RequestBody ReviewDto reviewDto) {
        try{
            return ResponseModel.success(reviewService.addDto(id, reviewDto));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{id}/reviews/list")
    public ResponseEntity<?> addReview(@PathVariable(name = "id") Long id,
                                       @RequestBody List<ReviewDto> reviewDtos) {
        try{
            return ResponseModel.success(reviewService.addDto(id, reviewDtos));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<?> updateReviews(@PathVariable(name = "reviewId") Long reviewId,
                                           @RequestBody ReviewDto reviewDto) {
        try{
            reviewService.updateOneDto(reviewId, reviewDto);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable(name = "reviewId") Long reviewId) {
        try{
            reviewService.deleteOneByIdSoft(reviewId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{id}/categories")
    public ResponseEntity<?> addCategory(@PathVariable(name = "id") Long id,
                                        @RequestBody CategoryDto categoryDto) {
        try{
            return ResponseModel.success(sanService.addCategoryDto(id, categoryDto));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{id}/categories/list")
    public ResponseEntity<?> addCategories(@PathVariable(name = "id") Long id,
                                           @RequestBody List<CategoryDto> categoryDtos) {
        try{
            return ResponseModel.success(sanService.addCategoryDto(id, categoryDtos));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("{sanId}/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable(name = "sanId") Long sanId,
                                            @PathVariable(name = "categoryId") Long categoryId) {
        try{
            sanService.deleteCategory(sanId, categoryId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{sanId}/links")
    public ResponseEntity<?> addLink(@PathVariable(name = "sanId") Long sanId,
                                           @RequestBody HyperLinkDto linkDto) {
        try{
            return ResponseModel.success(sanService.addHyperLinkDto(sanId, linkDto));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{sanId}/links/list")
    public ResponseEntity<?> addLinks(@PathVariable(name = "sanId") Long sanId,
                                      @RequestBody List<HyperLinkDto> linkDtos) {
        try{
            return ResponseModel.success(sanService.addHyperLinkDto(sanId, linkDtos));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/links/{linkId}")
    public ResponseEntity<?> updateLink(@PathVariable(name = "linkId") Long linkId,
                                           @RequestBody HyperLinkDto linkDto) {
        try{
            linkService.updateOneDto(linkId, linkDto);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("{sanId}/links/{linkId}")
    public ResponseEntity<?> deleteLink(@PathVariable(name = "sanId") Long sanId,
                                            @PathVariable(name = "linkId") Long linkId) {
        try{
            sanService.deleteLink(sanId, linkId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(value = "/{sanId}/pics")
    public ResponseEntity<?> getPics(@PathVariable(name = "sanId") Long sanId) {
        try{
            return ResponseModel.success(sanService.getPicUrls(sanId));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{sanId}/pics")
    public ResponseEntity<?> addPic(@PathVariable(name = "sanId") Long sanId,
                                    @RequestParam("pic") MultipartFile pic) {
        try{
            return ResponseModel.success(sanService.addPic(sanId, pic));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{sanId}/pics/list")
    public ResponseEntity<?> addPics(@PathVariable(name = "sanId") Long sanId,
                                     @RequestParam("pic") List<MultipartFile> pics) {
        try{
            return ResponseModel.success(sanService.addPics(sanId, pics));
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("{sanId}/pics/{picId}")
    public ResponseEntity<?> deletePic(@PathVariable(name = "sanId") Long sanId,
                                        @PathVariable(name = "picId") Long picId) {
        try{
            sanService.deletePic(sanId, picId);
            return ResponseModel.successPure();
        } catch (Exception e){
            return ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
