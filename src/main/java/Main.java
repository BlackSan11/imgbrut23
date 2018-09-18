import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sun.net.SocksProxy;


import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static com.sun.org.apache.xalan.internal.lib.ExsltStrings.split;

public class Main {
    static String PROXY_LIST_FILE = "proxy.list";
    static String PROXY_LIST_FILE2 = "proxyS.list";

    public static void main(String[] args) throws IOException {
        //new Parser().start();
        List<String> lines = Files.readAllLines(Paths.get(PROXY_LIST_FILE), StandardCharsets.UTF_8);
        List<String> lines2 = Files.readAllLines(Paths.get(PROXY_LIST_FILE2), StandardCharsets.UTF_8);
        for (String line : lines) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(line.split(":")[0], Integer.parseInt(line.split(":")[1])));
            if (testProxy(proxy)) {
                new Parser(proxy).start();
            } else continue;
        }
/*        for (String line2 : lines2) {
            if (testProxySocks(line2.split(":"))) {
                new Parser().start();
            } else continue;
        }
        */
        while (true) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Thread.activeCount() > 0) System.out.println("Запущено " + Thread.activeCount() + " процессов.");
            else {
                System.out.println("Все процессы завершены");
                break;
            }
        }
    }

    public static Boolean testProxy(Proxy proxy) {
        Connection.Response response = null;
        try {
            response = Jsoup
                    .connect("https://ya.ru")
                    .proxy(proxy)
                    .headers(HTTPHeaders.DEFAULT_HEADERS)
                    .method(Connection.Method.GET)
                    .execute();
            if (response.statusCode() == 200) return true;
            System.out.println(proxy.address().toString() + " " + response.statusCode());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return null;
    }

    public static Boolean testProxySocks(String[] proxy) {
        System.setProperty("socksProxyVersion", "4");
        Connection.Response response = null;
        try {
            SocketAddress proxyAddr = new InetSocketAddress(proxy[0], Integer.parseInt(proxy[1]));
            Proxy proxy1 = SocksProxy.create(proxyAddr, 4);
            response = Jsoup
                    .connect("https://ya.ru")
                    .proxy(proxy1)
                    .execute();
            if (response.statusCode() == 200) return true;
            System.out.println(proxy[0] + " " + response.statusCode());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return null;
    }
}
