package kz.open.sankaz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "NOTIFICATION_TEMPLATE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplate extends AbstractDictionaryEntity {
    @Id
    @GeneratedValue(generator = "NOTIFICATION_TEMPLATE_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "NOTIFICATION_TEMPLATE_ID_SEQ", name = "NOTIFICATION_TEMPLATE_ID", allocationSize = 1)
    private Long id;

    @Lob
    @Column(name = "MESSAGE_TEMPLATE", nullable = false, length = 1024)
    private String messageTemplate;
}
