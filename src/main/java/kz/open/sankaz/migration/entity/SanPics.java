package kz.open.sankaz.migration.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "san_pics")
@IdClass(SanPics.class)
public class SanPics implements Serializable {
    @Id
    private Long sanId;
    @Id
    private Long picId;

}
