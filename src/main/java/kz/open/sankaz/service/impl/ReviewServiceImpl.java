package kz.open.sankaz.service.impl;

import kz.open.sankaz.dto.ReviewDto;
import kz.open.sankaz.listener.event.CreateEvent;
import kz.open.sankaz.listener.event.DeleteEvent;
import kz.open.sankaz.listener.event.UpdateEvent;
import kz.open.sankaz.mapper.ReviewMapper;
import kz.open.sankaz.model.Review;
import kz.open.sankaz.model.San;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.repo.ReviewRepo;
import kz.open.sankaz.service.ReviewService;
import kz.open.sankaz.service.SanService;
import kz.open.sankaz.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class ReviewServiceImpl extends AbstractService<Review, ReviewRepo> implements ReviewService {

    private final ReviewRepo reviewRepo;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private SanService sanService;

    @Autowired
    private UserService userService;


    public ReviewServiceImpl(ReviewRepo reviewRepo) {
        super(reviewRepo);
        this.reviewRepo = reviewRepo;
    }

    @Override
    public ReviewDto getOneDto(Long id) {
        Review one = getOne(id);
        return reviewMapper.reviewToDto(one);
    }

    @Override
    public List<ReviewDto> getAllDto() {
        return reviewMapper.reviewToDto(getAll());
    }

    @Override
    public List<ReviewDto> getAllDto(Map<String, Object> params) {
        return reviewMapper.reviewToDto(getAll(params));
    }

    @Override
    public Review addOneDto(ReviewDto reviewDto) {
        log.info("SERVICE -> ReviewServiceImpl.addOneDto()");
        Review review = new Review();
        review.setText(reviewDto.getText());
        review.setRating(reviewDto.getRating());

        SecUser user = userService.getUser(reviewDto.getUsername());// TODO: Exception or validation
        review.setUser(user);
        San san = sanService.getOne(reviewDto.getSan().getId());// TODO: Exception or validation
        review.setSan(san);

        if(reviewDto.getParentReview() != null){
            Review parentReview = getOne(reviewDto.getParentReview());
            review.setParentReview(parentReview);
        }

        return addOne(review);
    }

    @Override
    public Review updateOneDto(Long id, ReviewDto reviewDto) {
        log.info("SERVICE -> ReviewServiceImpl.updateOneDto()");
        Review review = getOne(id);
        if(reviewDto.getText() != null && !review.getText().equals(reviewDto.getText())){
            review.setText(reviewDto.getText());
        }
        if(reviewDto.getRating() != null && !review.getRating().equals(reviewDto.getRating())){
            review.setRating(reviewDto.getRating());
        }
        if(reviewDto.getSan() != null && !review.getSan().getId().equals(reviewDto.getSan().getId())){
            San sanById = sanService
                    .getOne(reviewDto.getSan().getId());
            review.setSan(sanById);
        }
        if(reviewDto.getUsername() != null && !review.getUser().getUsername().equals(reviewDto.getUsername())){
            SecUser user = userService.getUser(reviewDto.getUsername());
            review.setUser(user);
        }
        if(reviewDto.getParentReview() != null && !review.getParentReview().getId().equals(reviewDto.getParentReview())){
            Review parentReview = getOne(reviewDto.getParentReview());
            review.setParentReview(parentReview);
        }
        return editOneById(review);
    }

    @Override
    public Review updateOneDto(Map<String, Object> params, ReviewDto reviewDto) {
        // Backlog: потом, с помощью JOOQ
        return null;
    }

    @Override
    protected Class getCurrentClass() {
        return Review.class;
    }

    @Override
    protected ApplicationEvent getCreateEvent(Review review) {
        return new CreateEvent(review);
    }

    @Override
    protected ApplicationEvent getDeleteEvent(Review review) {
        return new DeleteEvent(review);
    }

    @Override
    protected ApplicationEvent getUpdateEvent(Review review) {
        return new UpdateEvent(review);
    }
}
