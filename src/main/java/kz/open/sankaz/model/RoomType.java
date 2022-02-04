package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ROOM_TYPE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomType extends AbstractDictionaryLangEntity {

    @Id
    @GeneratedValue(generator = "ROOM_TYPE_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "ROOM_TYPE_ID_SEQ", name = "ROOM_TYPE_ID", allocationSize = 1)
    private Long id;

}
