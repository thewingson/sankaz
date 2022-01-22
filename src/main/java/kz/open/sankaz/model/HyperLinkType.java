package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "HYPER_LINK_TYPE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HyperLinkType extends AbstractDictionaryEntity {

    @Id
    @GeneratedValue(generator = "HYPER_LINK_TYPE_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "HYPER_LINK_TYPE_ID_SEQ", name = "HYPER_LINK_TYPE_ID", allocationSize = 1)
    private Long id;
}
