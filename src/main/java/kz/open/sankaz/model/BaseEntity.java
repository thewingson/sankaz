package kz.open.sankaz.model;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {
    public abstract Long getId();
}
