package ru.red.parser;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class ImgAnalizer {

    public static StringBuffer getExif(String imgURL){
        Metadata metadata = null;
        try {
            //FileUtils.copyURLToFile(new URL(imgURL), img);//FileUtils.toFile(new URL(imgURL));
            metadata = ImageMetadataReader.readMetadata(new URL(imgURL).openStream());
            StringBuffer exifS = new StringBuffer();
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    if(tag.getTagName().equals("Image Width") |
                            tag.getTagName().equals(" Image Height") |
                            tag.getTagName().equals("GPS Latitude") |
                            tag.getTagName().equals("GPS Longitude") |
                            tag.getTagName().equals("GPS Date Stamp") |
                            tag.getTagName().equals("GPS Time-Stamp") |
                            tag.getTagName().equals("Make") |
                            tag.getTagName().equals("Model") |
                            tag.getTagName().equals("Date/Time")){
                        exifS.append(directory.getName() + " - " + tag.getTagName()+ " = " + tag.getDescription() + "\n");
                    }
                }
            }
            if(exifS.length() < 3) return null;
            return exifS;
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
