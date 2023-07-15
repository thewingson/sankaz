package kz.open.sankaz.migration.controller;

import kz.open.sankaz.image.SanaTourImage;
import kz.open.sankaz.migration.entity.RoomPics;
import kz.open.sankaz.migration.entity.SanPics;
import kz.open.sankaz.migration.entity.SysFile;
import kz.open.sankaz.migration.repo.RoomPicRepo;
import kz.open.sankaz.migration.repo.SanPicRepo;
import kz.open.sankaz.migration.repo.SysFileRepo;
import kz.open.sankaz.model.Room;
import kz.open.sankaz.model.San;
import kz.open.sankaz.service.SanaTourImageService;
import kz.open.sankaz.util.ReSizerImageService;
import liquibase.pro.packaged.A;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/migration",produces="application/json")

public class MigrationController {

    @Autowired
    RoomPicRepo roomPicRepo;

    @Autowired
    SanPicRepo sanPicRepo;

    @Autowired
    SysFileRepo sysFileRepo;

    @Autowired
    ReSizerImageService reSizerImageService;

    @Autowired
    SanaTourImageService sanaTourImageService;
    @Value("${application.file.upload.path.image}")
    private String imageDir;



    @GetMapping
    @RequestMapping("/room")
    public String roomMigration() {
        List<SanaTourImage> sanaTourImageList = new ArrayList<>();
        List<RoomPics> asd=roomPicRepo.findAll();
        asd.forEach(r->{
            //File
            Optional<SysFile> sysFileOptional=sysFileRepo.findById(r.getFileId());
            sysFileOptional.ifPresent(s->{
                try {
                    SanaTourImage sanaTourImage = new SanaTourImage();
                    //RoomId
                    Room room =new Room();
                    room.setId(r.getRoomId());
                    sanaTourImage.setRoomId(room);
                    sanaTourImage.setType("R");

                    URL url = new URL("http://195.210.47.239/files/images/"+s.getFileName());
                    BufferedImage img= ImageIO.read(url);
                    File file = new File(s.getFileName());
                    ImageIO.write(img,"jpg",file);
                    byte[] fileBytes= Files.readAllBytes(file.toPath());
                    sanaTourImage.setBase64Original(Base64.getEncoder().encodeToString(fileBytes));
                    sanaTourImage.setBase64Scaled(reSizerImageService.reSize(fileBytes,240,240));
                    sanaTourImageList.add(sanaTourImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });


        });

        sanaTourImageService.saveAll(sanaTourImageList);
        return "RoomMigration";
    }

    @GetMapping
    @RequestMapping("/san")
    public String sanMigration() {
        List<SanaTourImage> sanaTourImageList = new ArrayList<>();
        List<SanPics> asd= sanPicRepo.findAll();
        asd.forEach(sanPics->{

            //File
            Optional<SysFile> sysFileOptional=sysFileRepo.findById(sanPics.getPicId());
            sysFileOptional.ifPresent(s->{
                try {
                    SanaTourImage sanaTourImage = new SanaTourImage();
                    //SanId
                    San san =new San();
                    san.setId(sanPics.getSanId());
                    sanaTourImage.setSanId(san);
                    sanaTourImage.setType("S");
                    URL url = new URL("http://195.210.47.239/files/images/"+s.getFileName());
                    BufferedImage img= ImageIO.read(url);
                    File file = new File(s.getFileName());
                    ImageIO.write(img,"jpg",file);
                    byte[] fileBytes= Files.readAllBytes(file.toPath());
                    sanaTourImage.setBase64Original(Base64.getEncoder().encodeToString(fileBytes));
                    sanaTourImage.setBase64Scaled(reSizerImageService.reSize(fileBytes,240,240));
                    sanaTourImageList.add(sanaTourImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });


        });

        sanaTourImageService.saveAll(sanaTourImageList);
        return "RoomMigration";
    }

    @GetMapping
    @RequestMapping("/count")
    public Long count(){
        return  sysFileRepo.count();
    }
}
