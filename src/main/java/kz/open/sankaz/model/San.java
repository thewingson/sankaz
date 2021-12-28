package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "SAN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class San extends AbstractEntity{

    @Id
    @GeneratedValue(generator = "SAN_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "SAN_ID_SEQ", name = "SAN_ID", allocationSize = 1)
    protected Long id;

    @Column(name = "NAME", nullable = false)
    protected String name;

    @Column(name = "DESCRIPTION")
    protected String description;
}
