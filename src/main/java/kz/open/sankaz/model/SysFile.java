package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "SYS_FILE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysFile extends BaseEntity {

    @Id
    @GeneratedValue(generator = "SYS_FILE_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "SYS_FILE_ID_SEQ", name = "SYS_FILE_ID", allocationSize = 1)
    private Long id;

    @Column(name = "FILE_NAME", nullable = false)
    private String fileName;

    @Column(name = "EXTENSION", nullable = false)
    private String extension;

    @Column(name = "SIZE", nullable = false)
    private String size;
}
