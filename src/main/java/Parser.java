import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.security.SecureRandom;

public class Parser extends Thread {
    final String AB = "0123456789abcdefghijklmnopqrstuvwxyz";
    SecureRandom rnd = new SecureRandom();

    String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public void run() {
        int i = 0;
        String id;
        while (true) {
            id = randomString(9);
            try {
                try {
                    //Document doc = Jsoup.connect("https://postimg.cc/image/"+id+"/").get();
                    Document doc = Jsoup.connect("https://postimg.cc/gallery/" + id + "/").post();
                    //https://postimg.cc/gallery/3i7dgk7z5/
                    String image = doc.getElementById("code_gallery").val();
                    System.out.println(image);
                    System.out.println("##########################################################");
                    new Sender(image).start();
                   /* String imageLo = image.toLowerCase();
                    if((!imageLo.contains("2014") & !imageLo.contains("2015") & !imageLo.contains("2016") & !imageLo.contains("2013")) & (image.contains("2018") | image.contains("IMG") | imageLo.contains("photo") | imageLo.contains("dsc") | imageLo.contains("mtp") | imageLo.contains("image")) ){
                        System.out.println(Thread.currentThread().getName() + ":: Title : " + title + " " + image + " |" + id);
                        //new Sender(image).start();
                    }*/
                    //new Sender(image).start();
                } catch (ConnectException ee) {
                } catch (SocketTimeoutException eee) {
                    eee.printStackTrace();
                }
            } catch (HttpStatusException e) {
                //e.printStackTrace();
                //System.out.println(Thread.currentThread().getName() + ":: №" + i + " : " + id);
            } catch (IOException e) {
                e.printStackTrace();
            }

            i++;
        }
    }
}
