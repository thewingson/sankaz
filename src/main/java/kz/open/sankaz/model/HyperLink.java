package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "HYPER_LINK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HyperLink extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "HYPER_LINK_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "HYPER_LINK_ID_SEQ", name = "HYPER_LINK_ID", allocationSize = 1)
    private Long id;

    @Column(name = "VALUE", nullable = false)
    private String value;

    /**
     * item Type or entity Name
     * */
    @Column(name = "ITEM_TYPE", nullable = false)
    private String itemType;

    /**
     * item ID or entity ID
     * */
    @Column(name = "ITEM_ID", nullable = false)
    private Long itemId;

    @ManyToOne
    @JoinColumn(name = "TYPE_ID", foreignKey = @ForeignKey(name = "HYPER_LINK_TYPE_FK"), nullable = false)
    @JsonManagedReference
    private HyperLinkType linkType;
}
