import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class Sender extends Thread {
    private final String toId = "120988325";
    private String link;

    public Sender(String link) {
        this.link = link;
    }

    public void run() {
        try {
            URL url = new URL("https://api.telegram.org/bot479139427:AAFfFSL6q5hwuYXA_JceJKEh9CxIajUu740/sendMessage?text="+ link +"&chat_id=120988325");
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("45.76.91.46", 3128)); // or whatever your proxy is
            HttpURLConnection uc = (HttpURLConnection)url.openConnection(proxy);
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
