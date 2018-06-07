import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.file.Files;

public class Sender extends Thread {
    private final String toId = "120988325";
    private String link;

    public Sender(String link) {
        this.link = link;
    }

    private String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
    private URLConnection connections;

    public void run() {
        /*URL url = null;
        try {
            url = new URL(this.link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        File file = new File(url.getPath());
        sendPhotoTo(file, toId);*/
       try {
            URL url = new URL("https://api.telegram.org/bot479139427:AAFfFSL6q5hwuYXA_JceJKEh9CxIajUu740/sendMessage?text=" + link + "&chat_id=120988325");
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            uc.setRequestProperty("Content-Language", "en-US");
            uc.setRequestMethod("GET");
            uc.connect();
            System.out.println(uc.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}


