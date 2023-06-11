package kz.open.sankaz.util;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class ReSizerImageService {


    public String reSize(byte[] imageByte,int height,int width){
        try(ByteArrayInputStream bais=new ByteArrayInputStream(imageByte);
            ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage originalImage= ImageIO.read(bais);
            BufferedImage resizedImage=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D=resizedImage.createGraphics();
            graphics2D.drawImage(originalImage,0,0, width,height,null);
            graphics2D.dispose();

            //ResizedImage for Main Page
            ImageIO.write(resizedImage,"png",baos);

        String result=  Base64.getEncoder().encodeToString(baos.toByteArray());
        return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
