package kz.open.sankaz.mapper;

import kz.open.sankaz.pojo.dto.ReviewCreateDto;
import kz.open.sankaz.pojo.dto.ReviewDto;
import kz.open.sankaz.model.Review;
import kz.open.sankaz.model.SecUser;
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

    @Named("reviewToDto")
    @Mapping(target = "san", ignore = true)
    @Mapping(target = "parentReview", ignore = true)
    abstract public ReviewDto reviewToDto(Review review);
    @IterableMapping(qualifiedByName = "reviewToDto")
    abstract public List<ReviewDto> reviewToDto(List<Review> reviews);

    @Named("reviewToDtoWithSanAndParent")
    @Mapping(target = "parentReview",
            expression = "java(review.getParentReview().getId())")
    @Mapping(target = "san",
            expression = "java(sanMapper.sanToDto(review.getSan()))")
    abstract public ReviewDto reviewToDtoWithSanAndParent(Review review);
    @IterableMapping(qualifiedByName = "reviewToDtoWithSanAndParent")
    abstract public List<ReviewDto> reviewToDtoWithSanAndParent(List<Review> reviews);

    @Named("reviewToReviewCreateDto")
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
    @Mapping(target = "createTs", expression = "java(getNewCreateTs())")
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

}
