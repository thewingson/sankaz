package kz.open.sankaz.repo;

import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.model.SecUserToken;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecUserTokenRepo extends CommonRepo<SecUserToken> {
    List<SecUserToken> findAllByUser(@Param("user") SecUser user);
    List<SecUserToken> findAllByUserAndIsBlocked(@Param("user") SecUser user,
                                                 @Param("isBLocked") boolean isBlocked);
    SecUserToken findByUserAndAccessToken(@Param("user") SecUser user,
                                          @Param("accessToken") String accessToken);
}
