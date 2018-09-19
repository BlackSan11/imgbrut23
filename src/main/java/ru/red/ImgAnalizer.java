package ru.red;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.IOException;
import java.net.URL;

public class ImgAnalizer {

    public static StringBuffer analize(String imgURL){
        Metadata metadata = null;
        try {
            //FileUtils.copyURLToFile(new URL(imgURL), img);//FileUtils.toFile(new URL(imgURL));
            metadata = ImageMetadataReader.readMetadata(new URL(imgURL).openStream());
            StringBuffer exifS = new StringBuffer();
            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    exifS.append(directory.getName() + " - " + tag.getTagName()+ " = " + tag.getDescription() + "\n");
                }
            }
            return exifS;
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
