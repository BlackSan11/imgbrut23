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

public class Parser extends Thread {
    final String AB = "0123456789abcdefghijklmnopqrstuvwxyz";
    SecureRandom rnd = new SecureRandom();
    SocksProxy proxy;

    public Parser(SocksProxy proxy) {
        this.proxy = proxy;
        System.out.println(proxy.toString() + " started");
    }

    String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public void run() {
        try {
            /*System.setProperty("http.proxyHost", "80.211.164.247");
            System.setProperty("http.proxyPort", "3128");*/
            BufferedWriter badsLF = new BufferedWriter(new FileWriter("bads.log"));
            BufferedWriter goodsLF = new BufferedWriter(new FileWriter("goods.log"));
            BufferedWriter errorsLF = new BufferedWriter(new FileWriter("errors.log"));
            String id;
            while (true) {
                id = randomString(9);
                try {
                    //Document doc = Jsoup.connect("https://postimg.cc/image/"+id+"/").get();
                    Document doc = Jsoup.connect("https://postimg.cc/gallery/" + id + "/").proxy(proxy).get();
                    //https://postimg.cc/gallery/3i7dgk7z5/
                    String image = doc.getElementById("code_gallery").val();
                    System.out.println(image);
                    System.out.println("##########################################################");
                    goodsLF.write(id + "\n");
                    goodsLF.flush();
                    new Sender(image).start();
                } catch (HttpStatusException e) {
                    if (e.getStatusCode() == 404) {
                        badsLF.write(id + "\n");
                        badsLF.flush();
                    } else {
                        errorsLF.write(id + "\n");
                        errorsLF.flush();
                    }
                } catch (IOException e) {
                    errorsLF.write(id + "\n");
                    errorsLF.flush();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
