package kz.open.sankaz.repo;

import kz.open.sankaz.model.HyperLink;
import kz.open.sankaz.model.HyperLinkType;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HyperLinkRepo extends CommonRepo<HyperLink> {

    List<HyperLink> getAllByItemType(@Param("itemType") String itemType);
    List<HyperLink> getAllByLinkType(@Param("hyperLinkType") HyperLinkType hyperLinkType);
    List<HyperLink> getAllByItemTypeAndItemId(@Param("itemType") String itemType, @Param("itemId") Long itemId);

}
