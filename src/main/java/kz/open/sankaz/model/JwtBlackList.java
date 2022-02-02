package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "JWT_BLACK_LIST")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtBlackList extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "JWT_BLACK_LIST_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "JWT_BLACK_LIST_ID_SEQ", name = "JWT_BLACK_LIST_ID", allocationSize = 1)
    private Long id;

    @Column(name = "ACCESS_TOKEN", nullable = false)
    private String accessToken;

    @Column(name = "USERNAME", nullable = false)
    private String username;

}
