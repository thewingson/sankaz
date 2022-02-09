package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "GENDER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Gender extends AbstractDictionaryLangEntity {

    @Id
    @GeneratedValue(generator = "GENDER_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "GENDER_ID_SEQ", name = "GENDER_ID", allocationSize = 1)
    private Long id;

}
