package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PROGRAM_SERVICE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramService extends AbstractEntity{

    @Id
    @GeneratedValue(generator = "PROGRAM_SERVICE_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "PROGRAM_SERVICE_ID_SEQ", name = "PROGRAM_SERVICE_ID", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "PRICE")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "PROGRAM_ID", foreignKey = @ForeignKey(name = "PROGRAM_SERVICE_PROGRAM_FK"), nullable = false)
    @JsonManagedReference
    private TourProgram program;
}
