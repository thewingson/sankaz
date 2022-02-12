package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

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
