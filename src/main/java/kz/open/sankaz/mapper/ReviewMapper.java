package kz.open.sankaz.mapper;

import kz.open.sankaz.dto.ReviewDto;
import kz.open.sankaz.model.Review;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

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

}
