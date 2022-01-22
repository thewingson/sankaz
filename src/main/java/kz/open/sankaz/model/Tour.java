package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "TOUR")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tour extends AbstractEntity{

    @Id
    @GeneratedValue(generator = "TOUR_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "TOUR_ID_SEQ", name = "TOUR_ID", allocationSize = 1)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<TourProgram> programs;

    public void addPrograms(TourProgram program){
        programs.add(program);
    }
}
