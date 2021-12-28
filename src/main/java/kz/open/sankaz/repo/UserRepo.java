package kz.open.sankaz.repo;

import kz.open.sankaz.model.SecUser;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CommonRepo<SecUser> {

    SecUser findByUsername(@Param("username") String username);
}
