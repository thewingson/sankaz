package kz.open.sankaz.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "SAN_CATEGORIES",
            joinColumns = @JoinColumn(name = "SAN_ID", foreignKey = @ForeignKey(name = "SAN_CATEGORIES_SAN_FK")),
            inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID", foreignKey = @ForeignKey(name = "SAN_CATEGORIES_CAT_FK")))
    private List<Category> categories;

    @OneToMany(mappedBy = "san", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<Room> rooms;

    @OneToMany(mappedBy = "san", cascade = CascadeType.REMOVE)
    @JsonBackReference
    private List<Review> reviews;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "SAN_PICS",
            joinColumns = @JoinColumn(name = "SAN_ID", foreignKey = @ForeignKey(name = "SAN_PICS_SAN_FK")),
            inverseJoinColumns = @JoinColumn(name = "PIC_ID", foreignKey = @ForeignKey(name = "SAN_PICS_PIC_FK")))
    private List<ItemPic> pics;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "SAN_LINKS",
            joinColumns = @JoinColumn(name = "SAN_ID", foreignKey = @ForeignKey(name = "SAN_LINKS_SAN_FK")),
            inverseJoinColumns = @JoinColumn(name = "PIC_ID", foreignKey = @ForeignKey(name = "SAN_LINKS_LINK_FK")))
    private List<HyperLink> links;

    public void addCategory(Category category){
        categories.add(category);
    }

    public void deleteCategory(Category category){
        categories.remove(category);
    }

    public void addPic(ItemPic pic){
        pics.add(pic);
    }

    public void deletePic(ItemPic pic){
        pics.remove(pic);
    }

    public void addLink(HyperLink link){
        links.add(link);
    }

    public void deleteLink(HyperLink link){
        links.remove(link);
    }

    public void addRoom(Room room){
        rooms.add(room);
    }

    public void addReview(Review review){
        reviews.add(review);
    }
}
