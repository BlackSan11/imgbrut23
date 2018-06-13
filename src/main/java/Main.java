import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sun.net.SocksProxy;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("1");
        List proxyList = new LinkedList();
        Files.lines(Paths.get("proxy.list"), StandardCharsets.UTF_8).forEach(proxyList::add);
        System.out.println("2");
        Document proxyCheck;
        SocksProxy proxy = null;
        SocksProxy socksProxy = null;
        for (Object o : proxyList) {
            System.out.println("3");
            proxy = SocksProxy.create(new InetSocketAddress(o.toString().split(":")[0], Integer.parseInt(o.toString().split(":")[1])), 5);
            //proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(o.toString().split(":")[0], Integer.parseInt(o.toString().split(":")[1])));
            try {
                proxyCheck = Jsoup.connect("https://postimages.org/").proxy(proxy).get();
                new Parser(proxy).start();
            } catch (IOException e) {
                // proxyCheck = Jsoup.connect("https://ya.ru/").get();
                // proxyCheck.title();
                // e.printStackTrace();
            }
        }
        System.out.println("4");

    }

    //Proxy proxy = new Proxy();
}

