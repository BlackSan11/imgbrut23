package ru.red;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import ru.red.parser.HTTPHeaders;
import sun.net.SocksProxy;
import sun.net.spi.DefaultProxySelector;
import sun.plugin2.main.client.PluginProxySelector;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.*;
import java.util.concurrent.ThreadLocalRandom;

public class T {
    public static void main(String[] args) {
        SocketAddress addr = new InetSocketAddress("80.83.165.217", 59341);
        SocksProxy proxy = SocksProxy.create(addr, 5);
        Socket socket = new Socket(proxy);
        Class clazzSocks = socket.getClass();
        Method setSockVersion = null;
        Field sockImplField = null;
        SocketImpl socksimpl = null;
        try {
            sockImplField = clazzSocks.getDeclaredField("impl");
            sockImplField.setAccessible(true);
            socksimpl = (SocketImpl) sockImplField.get(socket);
            Class clazzSocksImpl = socksimpl.getClass();
            setSockVersion = clazzSocksImpl.getDeclaredMethod("setV4");
            setSockVersion.setAccessible(true);
            if (null != setSockVersion) {
                setSockVersion.invoke(socksimpl);
            }
            sockImplField.set(socket, socksimpl);
        } catch (Exception e) {
        }
        InetSocketAddress dest = new InetSocketAddress("google.com", 80);

        try {
            socket.connect(dest);
            dest.toString();
        } catch (IOException ex) {
        }

        try {
            System.setProperty("socksProxyVersion", "4");
            Connection.Response response = Jsoup.connect("https://google.com")
                    .headers(HTTPHeaders.DEFAULT_HEADERS)
                    .method(Connection.Method.GET)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.52 Safari/536.5")
                    .execute();
            System.out.println(response.statusCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
