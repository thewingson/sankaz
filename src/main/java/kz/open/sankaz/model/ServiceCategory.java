package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "SERVICE_CATEGORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCategory extends AbstractDictionaryLangEntity {

    @Id
    @GeneratedValue(generator = "SERVICE_CATEGORY_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "SERVICE_CATEGORY_ID_SEQ", name = "SERVICE_CATEGORY_ID", allocationSize = 1)
    private Long id;

}
