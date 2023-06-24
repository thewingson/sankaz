package kz.open.sankaz.repo.dictionary;


import kz.open.sankaz.model.SanAdditional;
import kz.open.sankaz.model.SanAdditionalDic;
import kz.open.sankaz.repo.CommonRepo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SanAdditionalDicRepo extends CommonRepo<SanAdditionalDic> {
@Query("select s from SanAdditionalDic s")
List<SanAdditionalDic> findAll();



}
