package kz.open.sankaz.migration.repo;


import kz.open.sankaz.migration.entity.SysFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysFileRepo extends CrudRepository<SysFile,Long> {


}
