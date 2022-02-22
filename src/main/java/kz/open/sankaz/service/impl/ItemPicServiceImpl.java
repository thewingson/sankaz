package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.ItemPic;
import kz.open.sankaz.model.SysFile;
import kz.open.sankaz.pojo.dto.ItemPicDto;
import kz.open.sankaz.repo.ItemPicRepo;
import kz.open.sankaz.service.ItemPicService;
import kz.open.sankaz.service.SysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ItemPicServiceImpl extends AbstractService<ItemPic, ItemPicRepo> implements ItemPicService {

    @Autowired
    private SysFileService sysFileService;

    public ItemPicServiceImpl(ItemPicRepo itemPicRepo) {
        super(itemPicRepo);
    }

    @Override
    public ItemPic addOneDto(ItemPicDto picDto) {
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
    protected Class getCurrentClass() {
        return ItemPic.class;
    }

}
