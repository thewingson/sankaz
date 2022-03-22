package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "TEL_NUMBER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TelNumber extends AbstractEntity{

    @Id
    @GeneratedValue(generator = "TEL_NUMBER_SEQ", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "TEL_NUMBER_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "TEL_NUMBER_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Column(name = "VALUE", nullable = false)
    private String value;

}
