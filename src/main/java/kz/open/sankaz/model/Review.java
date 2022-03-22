package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import kz.open.sankaz.pojo.dto.ReviewAvgCountDto;
import kz.open.sankaz.pojo.dto.ReviewRatingDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

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
        query="select coalesce(round(cast(r.rating as numeric)), 0) as rat, count(r.id) as cnt " +
                "from review r " +
                "where r.san_id = :sanId and r.rating between :ratingStart and :ratingEnd and r.parent_id is null " +
                "group by round(cast(r.rating as numeric));",
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
                "where r.san_id = :sanId and r.rating between :ratingStart and :ratingEnd and r.parent_id is null ",
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
    @GenericGenerator(
            name = "REVIEW_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "REVIEW_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Column(name = "RATING")
    private Float rating;

    @ManyToOne
    @JoinColumn(name = "SAN_ID", foreignKey = @ForeignKey(name = "REVIEW_SAN_FK"), nullable = false)
    @JsonManagedReference
    private San san;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID", foreignKey = @ForeignKey(name = "REVIEW_PARENT_FK"))
    @JsonManagedReference
    private Review parentReview;

    @OneToMany(mappedBy = "parentReview", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Review> childReviews;

}
