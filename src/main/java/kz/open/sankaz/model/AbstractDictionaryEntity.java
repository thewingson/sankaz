package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class AbstractDictionaryEntity extends AbstractEntity {
    @Column(name = "NAME", nullable = false)
    protected String name;

    @Column(name = "CODE", nullable = false, unique = true)
    protected String code;

    @Column(name = "DESCRIPTION")
    protected String description;
}
