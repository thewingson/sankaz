package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "SAN_TYPE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SanType extends AbstractDictionaryLangEntity {

    @Id
    @GeneratedValue(generator = "SAN_TYPE_SEQ", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "SAN_TYPE_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SAN_TYPE_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

}
