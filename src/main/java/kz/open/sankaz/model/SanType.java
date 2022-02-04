package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "SAN_TYPE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SanType extends AbstractDictionaryEntity {

    @Id
    @GeneratedValue(generator = "SAN_TYPE_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "SAN_TYPE_ID_SEQ", name = "SAN_TYPE_ID", allocationSize = 1)
    private Long id;

}
