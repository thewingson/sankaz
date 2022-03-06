package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import kz.open.sankaz.pojo.dto.ReviewAvgCountDto;
import kz.open.sankaz.pojo.dto.ReviewRatingDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@SqlResultSetMapping(
        name="getRatingInfoMapping",
        classes={
                @ConstructorResult(
                        targetClass= ReviewRatingDto.class,
                        columns={
                                @ColumnResult(name="rat", type = float.class),
                                @ColumnResult(name="cnt", type = int.class)
                        }
                )
        }
)
@NamedNativeQuery(name="Review.getRatingInfo",
        query="select r.rating as rat, count(r.id) as cnt " +
                "from review r " +
                "where r.san_id = :sanId and r.rating between :ratingStart and :ratingEnd " +
                "group by r.rating;",
        resultSetMapping="getRatingInfoMapping")
@SqlResultSetMapping(
        name="getReviewInfoMapping",
        classes={
                @ConstructorResult(
                        targetClass= ReviewAvgCountDto.class,
                        columns={
                                @ColumnResult(name="aver", type = Float.class),
                                @ColumnResult(name="cnt", type = Integer.class)
                        }
                )
        }
)
@NamedNativeQuery(name="Review.getReviewInfo",
        query="select coalesce(cast(avg(r.rating) as float), 0) as aver , count(r.id) as cnt " +
                "from  review r " +
                "where r.san_id = :sanId and r.rating between :ratingStart and :ratingEnd",
        resultSetMapping="getReviewInfoMapping")
@Entity
@Table(name = "REVIEW")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review extends Message {

    @Id
    @GeneratedValue(generator = "REVIEW_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "REVIEW_ID_SEQ", name = "REVIEW_ID", allocationSize = 1)
    private Long id;

    @Column(name = "RATING", nullable = false)
    private Float rating;

    @ManyToOne
    @JoinColumn(name = "SAN_ID", foreignKey = @ForeignKey(name = "REVIEW_SAN_FK"), nullable = false)
    @JsonManagedReference
    private San san;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID", foreignKey = @ForeignKey(name = "REVIEW_PARENT_FK"))
    @JsonManagedReference
    private Review parentReview;

}
