package kz.open.sankaz.repo;

import kz.open.sankaz.model.Category;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends CommonRepo<Category> {

    List<Category> getAllByCodeIn(@Param("codes") List<String> codes);

}
