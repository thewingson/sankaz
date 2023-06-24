package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SAN_ADDITIONAL_DIC")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SanAdditionalDic extends BaseEntity{

    @Id
    @GeneratedValue(generator = "SANATOUR_ADDITIONAL_DIC_SEQ", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "SANATOUR_ADDITIONAL_DIC_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SANATOUR_ADDITIONAL_DIC_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;
    @Column
    private String nameKz;
    @Column
    private String nameRu;
    @Column
    private boolean enable;

    @OneToMany(mappedBy = "additionalDic", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JsonBackReference(value = "additionalDic")
    private List<SanAdditional> sanAdditionalList;

}
