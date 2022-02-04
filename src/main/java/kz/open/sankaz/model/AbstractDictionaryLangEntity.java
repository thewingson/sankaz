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
public abstract class AbstractDictionaryLangEntity extends AbstractDictionaryEntity {
    @Column(name = "NAME_KZ")
    protected String nameKz;

    @Column(name = "DESCRIPTION_KZ")
    protected String descriptionKz;
}
