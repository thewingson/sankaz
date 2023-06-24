package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "SAN_ADDITIONAL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SanAdditional extends BaseEntity {

    @Id
    @GeneratedValue(generator = "SANATOUR_ADDITIONAL_SEQ", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "SANATOUR_ADDITIONAL_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SANATOUR_ADDITIONAL_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;
    @ManyToOne
    @JoinColumn(name = "san")
    @JsonBackReference(value = "sanAdditionals")
    private San san;

    @ManyToOne
    @JoinColumn(name = "additional_dic")
    @JsonManagedReference(value = "additionalDic")
    private SanAdditionalDic additionalDic;
    @Column
    private Long cost;
    @Column
    private boolean enable;

}
