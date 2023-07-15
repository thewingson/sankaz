package kz.open.sankaz.migration.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@IdClass(RoomPics.class)
@Table(name = "room_pics")
public class RoomPics implements Serializable {

    @Id
    private Long roomId;

    @Id
    private Long fileId;

}
