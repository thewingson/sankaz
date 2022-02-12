package kz.open.sankaz.service.impl;

import kz.open.sankaz.mapper.ReviewMapper;
import kz.open.sankaz.model.Review;
import kz.open.sankaz.pojo.filter.ReviewBySanIdFilter;
import kz.open.sankaz.repo.ReviewRepo;
import kz.open.sankaz.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@Transactional
public class ReviewServiceImpl extends AbstractService<Review, ReviewRepo> implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;


    public ReviewServiceImpl(ReviewRepo reviewRepo) {
        super(reviewRepo);
    }

    @Override
    public List<Review> getAllBySanId(Long sanId) {
        return repo.getAllBySanId(sanId);
    }

    @Override
    public List<Review> getAllByFilter(Long sanId, ReviewBySanIdFilter filter) {
        return repo.getAllBySanIdAndRatingIn(sanId, filter.getRatings());
    }

    @Override
    protected Class getCurrentClass() {
        return Review.class;
    }
}
