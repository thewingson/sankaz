package kz.open.sankaz.mapper;

import kz.open.sankaz.model.Stock;
import kz.open.sankaz.pojo.dto.notifications.StockDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class NotificationMapper extends AbstractMapper {

    @Named("stockToDto")
    @Mapping(target = "viewCount", source = "viewCount")
    @Mapping(target = "sanId", source = "san.id")
    abstract public StockDto stockToDto(Stock stock);
    @IterableMapping(qualifiedByName = "stockToDto")
    abstract public List<StockDto> stockToDto(List<Stock> stocks);

}
