package kz.open.sankaz.service;

import kz.open.sankaz.dto.CategoryDto;
import kz.open.sankaz.dto.HyperLinkDto;
import kz.open.sankaz.dto.ItemPicDto;
import kz.open.sankaz.dto.SanDto;
import kz.open.sankaz.model.San;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SanService extends CommonService<San>, CommonDtoService<San, SanDto> {

    /***
     * for Entity
     */
//    Room add(Room room);
//    Review add(Review review);
//    HyperLink add(HyperLink link);
//
//    Room editOne(Room room);
//    Review editOne(Review review);
//    HyperLink editOne(HyperLink link);

    void deleteCategory(Long sanId, Long categoryId);
    void deleteLink(Long sanId, Long linkId);
    void deletePic(Long sanId, Long picId);
//
//    void getRoom(Long id);
//    void getReview(Long id);
//    void getLink(Long id);
//
//    SanDto getOneDto(Long id);
//    List<SanDto> getAllDto();
//
//    San addRoomsDto(Long id, List<RoomDto> roomDtos);
//    San addReviewsDto(Long id, List<ReviewDto> reviewDtos);
//    San addHyperLinksDto(Long id, List<HyperLinkDto> hyperLinkDtos);

    /***
     * for DTO
     */
//    Room addRoomDto(San san, RoomDto roomDto);
//    Review addReviewDto(San san, ReviewDto reviewDto);
//
//    List<Room> addRoomDto(Long sanId, List<RoomDto> roomDtos);
//    Room addRoomDto(Long sanId, RoomDto roomDto);
//    List<Review> addReviewDto(Long sanId, List<ReviewDto> reviewDtos);
//    Review addReviewDto(Long sanId, ReviewDto reviewDto);
//
//    Room updateRoomDto(Long roomId, RoomDto roomDto);
//    Review updateReviewDto(Long reviewId, ReviewDto reviewDto);

    San addCategoryDto(Long id, CategoryDto categoryDto);
    San addCategoryDto(Long id, List<CategoryDto> categoryDtos);

    San addHyperLinkDto(Long id, HyperLinkDto hyperLinkDto);
    San addHyperLinkDto(Long id, List<HyperLinkDto> hyperLinkDtos);

    San addItemPicDto(Long id, ItemPicDto picDto);
    San addItemPicDto(Long id, List<ItemPicDto> picDtos);

    String addPic(Long sanId, MultipartFile pic) throws IOException;
    List<String> addPics(Long sanId, List<MultipartFile> pics) throws IOException;

    List<String> getPicUrls(Long sanId);
}
