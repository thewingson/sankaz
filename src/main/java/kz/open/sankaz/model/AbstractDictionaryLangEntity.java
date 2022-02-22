package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Locale;

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

    public String getLocaleName(Locale locale){
        return null;
    }

    public String getLocaleDescription(Locale locale){
        return null;
    }

    public String getCurrentLocaleName(){
        return null;
    }

    public String getCurrentLocaleDescription(){
        return null;
    }
}
