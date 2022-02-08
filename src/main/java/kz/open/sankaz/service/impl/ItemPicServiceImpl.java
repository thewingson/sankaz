package kz.open.sankaz.service.impl;

import kz.open.sankaz.pojo.dto.ItemPicDto;
import kz.open.sankaz.mapper.ItemPicMapper;
import kz.open.sankaz.model.ItemPic;
import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.repo.ItemPicRepo;
import kz.open.sankaz.service.ItemPicService;
import kz.open.sankaz.service.SysFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class ItemPicServiceImpl extends AbstractService<ItemPic, ItemPicRepo> implements ItemPicService {

    private final ItemPicRepo itemPicRepo;

    @Autowired
    private SysFileService sysFileService;

    @Autowired
    private ItemPicMapper itemPicMapper;

    @Value("${application.file.upload.path}")
    private String APPLICATION_UPLOAD_PATH;

    public ItemPicServiceImpl(ItemPicRepo itemPicRepo) {
        super(itemPicRepo);
        this.itemPicRepo = itemPicRepo;
    }

    @Override
    public ItemPicDto getOneDto(Long id) {
        ItemPic one = getOne(id);
        return itemPicMapper.itemPicToDtoWithFile(one);
    }

    @Override
    public List<ItemPicDto> getAllDto() {
        return itemPicMapper.itemPicToDtoWithFile(getAll());
    }

    @Override
    public List<ItemPicDto> getAllDto(Map<String, Object> params) {
        return itemPicMapper.itemPicToDtoWithFile(getAll(params));
    }

    @Override
    public ItemPic addOneDto(ItemPicDto picDto) {
        log.info("SERVICE -> ItemPicServiceImpl.addOneDto()");
        SysFile file = new SysFile();
        file.setFileName(picDto.getFileName());
        file.setExtension(picDto.getExtension());
        file.setSize(Long.valueOf(picDto.getSize()));
        file = sysFileService.addOne(file);

        ItemPic pic = new ItemPic();
        pic.setFile(file);

        return addOne(pic);
    }

    @Override
    public ItemPic updateOneDto(Long id, ItemPicDto picDto) {
        throw new RuntimeException("Method is not supported!");
    }

    @Override
    public ItemPic updateOneDto(Map<String, Object> params, ItemPicDto hyperLinkDto) {
        // Backlog: потом, с помощью JOOQ
        throw new RuntimeException("Method is not supported!");
    }

    @Override
    public byte[] getByteArrayFromPic(ItemPic pic) throws IOException {
//        InputStream in = getClass()
//                .getResourceAsStream(APPLICATION_UPLOAD_PATH + pic.getFile().getFileName());
//        return IOUtils.toByteArray(in);

        return Files.readAllBytes(Paths.get(APPLICATION_UPLOAD_PATH + pic.getFile().getFileName()));
    }

    @Override
    protected Class getCurrentClass() {
        return ItemPic.class;
    }

}
