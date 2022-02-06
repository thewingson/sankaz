package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @SequenceGenerator(sequenceName = "TEL_NUMBER_ID_SEQ", name = "TEL_NUMBER_ID", allocationSize = 1)
    private Long id;

    @Column(name = "VALUE", nullable = false)
    private String value;

}
