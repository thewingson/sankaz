package kz.open.sankaz.repo;

import kz.open.sankaz.model.HyperLink;
import kz.open.sankaz.model.HyperLinkType;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HyperLinkRepo extends CommonRepo<HyperLink> {

    List<HyperLink> getAllByLinkType(@Param("hyperLinkType") HyperLinkType hyperLinkType);

}
