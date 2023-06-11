package kz.open.sankaz.repo;

import kz.open.sankaz.image.SanaTourImage;
import kz.open.sankaz.model.SecRole;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SanaTourImageRepo extends CommonRepo<SanaTourImage> {
}
