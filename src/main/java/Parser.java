import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.security.SecureRandom;

public class Parser extends Thread {
    final String AB = "0123456789abcdefghijklmnopqrstuvwxyz";
    SecureRandom rnd = new SecureRandom();

    String randomString(int len){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
    public void run(){
        int i = 0;
        String id;
        while (true){
            id = randomString(9);
            try{
                Document doc = Jsoup.connect("https://postimg.cc/image/"+id+"/").get();
                String title = doc.title();
                String image = doc.getElementById("main-image").attr("src");
                System.out.println(Thread.currentThread().getName() + ":: Title : " + title + " " + image);
            } catch (HttpStatusException e){
               // System.out.println(Thread.currentThread().getName() + ":: №" + i + " : " +id);
            } catch (IOException e) {
                e.printStackTrace();
            }
            i++;
        }
    }
}
