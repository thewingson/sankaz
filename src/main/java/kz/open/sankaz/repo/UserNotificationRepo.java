package kz.open.sankaz.repo;

import kz.open.sankaz.model.Stock;
import kz.open.sankaz.model.UserNotification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationRepo extends CommonRepo<UserNotification> {
    List<UserNotification> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select count(e) " +
            "from UserNotification e " +
            "where e.user.id = :userId " +
            "and e.viewed = :isViewed")
    int getNotViewedNotificationsCount(@Param("userId") Long userId,
                                        @Param("isViewed") boolean isViewed);

    List<UserNotification> findAllByStock(@Param("stock") Stock stock);
}
