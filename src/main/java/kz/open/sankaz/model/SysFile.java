package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "SYS_FILE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysFile extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "SYS_FILE_SEQ", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "SYS_FILE_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SYS_FILE_SEQ"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Column(name = "FILE_NAME", nullable = false)
    private String fileName;

    @Column(name = "EXTENSION", nullable = false)
    private String extension;

    @Column(name = "SIZE", nullable = false)
    private Long size;

    @Column(name = "DELETED_DATE")
    private LocalDate deletedDate;
}
