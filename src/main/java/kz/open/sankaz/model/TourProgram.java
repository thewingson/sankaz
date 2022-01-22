package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "TOUR_PROGRAM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourProgram extends AbstractEntity{

    @Id
    @GeneratedValue(generator = "TOUR_PROGRAM_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "TOUR_PROGRAM_ID_SEQ", name = "TOUR_PROGRAM_ID", allocationSize = 1)
    private Long id;

    @Column(name = "SHORT_DESCRIPTION")
    private String shortDescription;

    @Column(name = "PRICE")
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "TOUR_ID", foreignKey = @ForeignKey(name = "TOUR_PROGRAM_TOUR_FK"), nullable = false)
    @JsonManagedReference
    private Tour tour;

    @OneToMany(mappedBy = "program", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<ProgramDay> days;

    @OneToMany(mappedBy = "program", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<ProgramService> services;

    public void addService(ProgramService service){
        services.add(service);
    }
}
