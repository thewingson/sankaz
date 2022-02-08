package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "CITY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class City extends AbstractDictionaryLangEntity {

    @Id
    @GeneratedValue(generator = "CITY_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "CITY_ID_SEQ", name = "CITY_ID", allocationSize = 1)
    private Long id;

}
