package kz.open.sankaz.mapper;

import kz.open.sankaz.model.Review;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.pojo.dto.ReviewBySanIdDto;
import kz.open.sankaz.pojo.dto.ReviewCreateDto;
import kz.open.sankaz.pojo.filter.ReviewCreateFilter;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ReviewMapper {

    @Autowired
    protected SanMapper sanMapper;

    @Named("reviewToReviewCreateDto")
    @Mapping(target = "reviewDate", source = "messageDate")
    @Mapping(target = "username", expression = "java(review.getUser().getUsername())")
    @Mapping(target = "fullName", expression = "java(getUsernameFromUser(review.getUser()))")
    @Mapping(target = "sanId", source = "san.id")
    @Mapping(target = "parentReviewId", source = "parentReview.id")
    abstract public ReviewCreateDto reviewToReviewCreateDto(Review review);
    @IterableMapping(qualifiedByName = "reviewToReviewCreateDto")
    abstract public List<ReviewCreateDto> reviewToReviewCreateDto(List<Review> reviews);

    /**
     * DTO to Review
     * */
    @Named("reviewCreateFilterToReview")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "san", ignore = true)
    @Mapping(target = "parentReview", ignore = true)
    abstract public Review reviewCreateFilterToReview(ReviewCreateFilter filter);
    @IterableMapping(qualifiedByName = "reviewCreateFilterToReview")
    abstract public List<Review> reviewCreateFilterToReview(List<ReviewCreateFilter> filter);

    public String getUsernameFromUser(SecUser user){
        if(user.getFullName() != null && !user.getFullName().isEmpty()){
            return user.getFullName();
        } else {
            return user.getUsername();
        }
    }

    public LocalDateTime getNewCreateTs(){
        return LocalDateTime.now();
    }

    @Named("reviewToReviewBySanIdDto")
    @Mapping(target = "username", source = "review.user.username")
    @Mapping(target = "parentReviewId", source = "review.parentReview.id")
    abstract public ReviewBySanIdDto reviewToReviewBySanIdDto(Review review);
    @IterableMapping(qualifiedByName = "reviewToReviewBySanIdDto")
    abstract public List<ReviewBySanIdDto> reviewToReviewBySanIdDto(List<Review> reviews);

}
