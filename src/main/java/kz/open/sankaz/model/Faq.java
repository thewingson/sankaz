package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "FAQ")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Faq extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "FAQ_SEQ", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "FAQ_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "FAQ_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Column(name = "QUESTION", nullable = false)
    private String question;

    @Column(name = "ANSWER", nullable = false)
    private String answer;
}
