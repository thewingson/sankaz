package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "PROGRAM_DAY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramDay extends AbstractEntity{

    @Id
    @GeneratedValue(generator = "PROGRAM_DAY_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "PROGRAM_DAY_ID_SEQ", name = "PROGRAM_DAY_ID", allocationSize = 1)
    private Long id;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "TOUR_PROGRAM_ID", foreignKey = @ForeignKey(name = "PROGRAM_DAY_PROGRAM_FK"), nullable = false)
    @JsonManagedReference
    private TourProgram program;
}
