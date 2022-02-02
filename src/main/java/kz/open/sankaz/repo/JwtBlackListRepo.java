package kz.open.sankaz.repo;

import kz.open.sankaz.model.JwtBlackList;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JwtBlackListRepo extends CommonRepo<JwtBlackList> {
    List<JwtBlackList> findAllByUsername(@Param("username") String username);
}
