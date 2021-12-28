package kz.open.sankaz.repo;

import kz.open.sankaz.model.SecRole;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends CommonRepo<SecRole> {
    SecRole findByName(@Param("name") String name);
}
