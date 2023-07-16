package kz.open.sankaz.repo;

import kz.open.sankaz.image.SanaTourImage;
import kz.open.sankaz.model.San;
import kz.open.sankaz.model.SecRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SanaTourImageRepo extends CommonRepo<SanaTourImage> {
    @Query(value = "select si.base64_scaled from sanatour_image  si where si.san_id = :sa limit 1",nativeQuery = true)
    String findFirstBySanId(@Param("sa") Long sa);
}
