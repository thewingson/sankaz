package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "STOCK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stock extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "STOCK_SEQ", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "STOCK_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "STOCK_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TITLE_KZ")
    private String titleKz;

    @Column(name = "DESCRIPTION_KZ")
    private String descriptionKz;

    @Column(name = "VIEW_COUNT", nullable = false)
    private int viewCount = 0;

    @Column(name = "ACTIVE", nullable = false)
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "SAN_ID", foreignKey = @ForeignKey(name = "STOCK_SAN_FK"))
    @JsonManagedReference
    private San san;

}
