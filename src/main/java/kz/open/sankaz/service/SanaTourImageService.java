package kz.open.sankaz.service;

import kz.open.sankaz.image.SanaTourImage;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SanaTourImageService extends CommonService<SanaTourImage> {

    @Override
    List<SanaTourImage> getAll();


}
