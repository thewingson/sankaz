package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ITEM_PIC")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemPic extends AbstractEntity{

    @Id
    @GeneratedValue(generator = "ITEM_PIC_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "ITEM_PIC_ID_SEQ", name = "ITEM_PIC_ID", allocationSize = 1)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "FILE_ID", foreignKey = @ForeignKey(name = "ITEM_PIC_FILE_FK"), nullable = false)
    @JsonManagedReference
    private SysFile file;
}
