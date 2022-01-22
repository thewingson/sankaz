package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "CATEGORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category extends AbstractDictionaryEntity {

    @Id
    @GeneratedValue(generator = "CATEGORY_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "CATEGORY_ID_SEQ", name = "CATEGORY_ID", allocationSize = 1)
    private Long id;

}
