package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "COMPANY_CATEGORY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyCategory extends AbstractDictionaryLangEntity {

    @Id
    @GeneratedValue(generator = "COMPANY_CATEGORY_SEQ", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "COMPANY_CATEGORY_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "COMPANY_CATEGORY_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

}
