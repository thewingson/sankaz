package kz.open.sankaz.service.impl;

import kz.open.sankaz.dto.CategoryDto;
import kz.open.sankaz.dto.HyperLinkDto;
import kz.open.sankaz.dto.ItemPicDto;
import kz.open.sankaz.dto.SanDto;
import kz.open.sankaz.mapper.SanMapper;
import kz.open.sankaz.model.SanType;
import kz.open.sankaz.model.HyperLink;
import kz.open.sankaz.model.ItemPic;
import kz.open.sankaz.model.San;
import kz.open.sankaz.repo.SanRepo;
import kz.open.sankaz.service.CategoryService;
import kz.open.sankaz.service.HyperLinkService;
import kz.open.sankaz.service.ItemPicService;
import kz.open.sankaz.service.SanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class SanServiceImpl extends AbstractService<San, SanRepo> implements SanService {

    private final SanRepo sanRepo;

    @Lazy
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private HyperLinkService linkService;

    @Autowired
    private ItemPicService picService;

    @Autowired
    private SanMapper sanMapper;

    @Value("${application.file.upload.path}")
    private String APPLICATION_UPLOAD_PATH;

    public SanServiceImpl(SanRepo sanRepo) {
        super(sanRepo);
        this.sanRepo = sanRepo;
    }

    @Override
    public SanDto getOneDto(Long id) {
        San one = getOne(id);
        return sanMapper.sanToDtoWithAll(one);
    }

    @Override
    public List<SanDto> getAllDto() {
//        return sanMapper.sanToDto(getAll());
        return sanMapper.sanToDtoWithAll(getAll());
    }

    @Override
    public List<SanDto> getAllDto(Map<String, Object> params) {
        return sanMapper.sanToDto(getAll(params));
    }

    @Override
    public San addOneDto(SanDto sanDto) {
        log.info("SERVICE -> SanServiceImpl.addOneDto()");
        San san = new San();
        san.setDescription(sanDto.getDescription());
        san.setName(sanDto.getName());

        if(sanDto.getCategories()!= null && !sanDto.getCategories().isEmpty()){
            List<SanType> categoriesByCode = categoryService
                    .getAllByCodeIn(sanDto.getCategories().stream().map(CategoryDto::getCode).collect(Collectors.toList()));
            san.setCategories(categoriesByCode);
        }

        return addOne(san);
    }

    @Override
    public San updateOneDto(Long id, SanDto sanDto) {
        log.info("SERVICE -> SanServiceImpl.updateOneDto()");
        San san = getOne(id);
        if(sanDto.getName() != null){
            san.setName(sanDto.getName());
        }
        if(sanDto.getDescription() != null){
            san.setDescription(sanDto.getDescription());
        }

        return editOneById(san);
    }

    @Override
    public San updateOneDto(Map<String, Object> params, SanDto sanDto) {
        // Backlog: потом, с помощью JOOQ
        return null;
    }

    @Override
    public void deleteCategory(Long sanId, Long categoryId) {
        San san = getOne(sanId);
        SanType category = categoryService.getOne(categoryId);
        san.deleteCategory(category);
        repo.save(san);
    }

    @Override
    public void deleteLink(Long sanId, Long linkId) {
        San san = getOne(sanId);
        HyperLink link = linkService.getOne(linkId);
        san.deleteLink(link);
        repo.save(san);
    }

    @Override
    public void deletePic(Long sanId, Long picId) {
        San san = getOne(sanId);
        ItemPic pic = picService.getOne(picId);
        san.deletePic(pic);
        repo.save(san);
    }

    @Override
    public San addCategoryDto(Long id, CategoryDto categoryDto) {
        San san = getOne(id);
        SanType category;
        if(categoryDto.getId() != null){
            category = categoryService.getOne(categoryDto.getId());
        } else {
            category = categoryService.getOneDto(categoryDto.getCode());
        }

        san.addCategory(category);
        return editOneById(san);
    }

    @Override
    public San addCategoryDto(Long id, List<CategoryDto> categoryDtos) {
        List<San> sans = new ArrayList<>();
        categoryDtos.forEach(categoryDto -> {
            sans.add(addCategoryDto(id, categoryDto));
        });
        if(sans.isEmpty()){
            return null;
        } else {
            return sans.get(0);
        }
    }

    @Override
    public San addHyperLinkDto(Long id, HyperLinkDto hyperLinkDto) {
        San san = getOne(id);
        HyperLink link = linkService.addOneDto(hyperLinkDto);
        san.addLink(link);
        return editOneById(san);
    }

    @Override
    public San addHyperLinkDto(Long id, List<HyperLinkDto> hyperLinkDtos) {
        List<San> sans = new ArrayList<>();
        hyperLinkDtos.forEach(linkDto -> sans.add(addHyperLinkDto(id, linkDto)));
        if(sans.isEmpty()){
            return null;
        } else {
            return sans.get(0);
        }
    }

    @Override
    public San addItemPicDto(Long id, ItemPicDto picDto) {
        San san = getOne(id);
        ItemPic pic = picService.addOneDto(picDto);
        san.addPic(pic);
        return editOneById(san);
    }

    @Override
    public San addItemPicDto(Long id, List<ItemPicDto> picDtos) {
        List<San> sans = new ArrayList<>();
        picDtos.forEach(picDto -> sans.add(addItemPicDto(id, picDto)));
        if(sans.isEmpty()){
            return null;
        } else {
            return sans.get(0);
        }
    }

    @Override
    public String addPic(Long sanId, MultipartFile pic) throws IOException {
        San san = getOne(sanId);
        String fileNameWithPath = "";
        if (pic != null && !pic.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(APPLICATION_UPLOAD_PATH);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + pic.getOriginalFilename();
            fileNameWithPath = APPLICATION_UPLOAD_PATH + "/" + resultFilename;

            pic.transferTo(new File(fileNameWithPath));

            ItemPicDto picDto = new ItemPicDto();
            picDto.setExtension(pic.getContentType());
            picDto.setFileName(pic.getOriginalFilename());
            picDto.setSize(String.valueOf(pic.getSize()));
            ItemPic itemPic = picService.addOneDto(picDto);

            san.addPic(itemPic);
            editOneById(san);
        }
        return fileNameWithPath;
    }

    @Override
    public List<String> addPics(Long sanId, List<MultipartFile> pics) throws IOException {
        List<String> fileNameWithPathList = new ArrayList<>();
        for (MultipartFile file : pics) {
            fileNameWithPathList.add(addPic(sanId, file));
        }
        return fileNameWithPathList;
    }

    @Override
    public List<String> getPicUrls(Long sanId) {
        San san = getOne(sanId);
        List<String> picUrls = san.getPics()
                .stream()
                .map(itemPic -> APPLICATION_UPLOAD_PATH + itemPic.getFile().getFileName())
                .collect(Collectors.toList());

        return picUrls;
    }

    @Override
    protected Class getCurrentClass() {
        return SanType.class;
    }

}
