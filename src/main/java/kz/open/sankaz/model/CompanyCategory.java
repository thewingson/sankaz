package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @SequenceGenerator(sequenceName = "COMPANY_CATEGORY_ID_SEQ", name = "COMPANY_CATEGORY_ID", allocationSize = 1)
    private Long id;

}
