import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sun.net.SocksProxy;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

public class Parser extends Thread {
    final String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
    SecureRandom rnd = new SecureRandom();
    Proxy proxy;

    public Parser(Proxy proxy) {
        this.proxy = proxy;
        System.out.println("Started on " + proxy.address().toString() + " Процессов" + Thread.activeCount());
    }

    String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
    public Boolean testConnection(){
        try {
            Connection.Response response = Jsoup
                    .connect("https://postimg.cc/RNHKc3BL")
                    .proxy(proxy)
                    .execute();
            if(response.statusCode() == 200) return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return null;
    }
    public void run() {
        try {
            /*System.setProperty("http.proxyHost", "80.211.164.247");
            System.setProperty("http.proxyPort", "3128");*/
            BufferedWriter badsLF = new BufferedWriter(new FileWriter("bads.log", true));
            BufferedWriter goodsLF = new BufferedWriter(new FileWriter("goods.log", true));
            BufferedWriter errorsLF = new BufferedWriter(new FileWriter("errors.log", true));
            String id;
            while (true) {
                if(!testConnection()) {
                    System.out.println("Proxy " + proxy.address().toString() + " погорел");
                    break;
                }
                id = randomString(8);
                try {
                    Connection.Response doc = Jsoup.connect("https://postimg.cc/" + id + "/")
                            .headers(HTTPHeaders.DEFAULT_HEADERS)
                            .proxy(proxy)
                            .execute();
                    //https://postimg.cc/gallery/3i7dgk7z5/
                    //String image = doc.parse().getElementById("code_gallery").val();
                    System.out.println("https://postimg.cc/gallery/" + id + " - OK");
                    goodsLF.write(id + "\n");
                    Jsoup.connect("https://api.telegram.org/bot479139427:AAFfFSL6q5hwuYXA_JceJKEh9CxIajUu740/sendMessage?text=https://postimg.cc/"+ id + "/&chat_id=120988325")
                    .proxy(proxy)
                    .method(Connection.Method.GET)
                    .execute();
                    goodsLF.flush();
                } catch (HttpStatusException e) {
                    e.printStackTrace();
                    //ЕСЛИ 404
                    if (e.getStatusCode() == 404) {
                        badsLF.write(id + "\n");
                        badsLF.flush();
                    } else { //ЕСЛИ ДРУГАЯ
                        errorsLF.write(id + "\n");
                        errorsLF.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    errorsLF.write(id + "\n");
                    errorsLF.flush();
                }
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(4000, 10000 + 1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
