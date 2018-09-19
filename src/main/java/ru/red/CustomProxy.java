package ru.red;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.Proxy;

public class CustomProxy extends Thread {

    Proxy webProxy;
    Bot bot;

    public CustomProxy(Proxy webProxy, Bot bot) {
        this.webProxy = webProxy;
        this.bot = bot;
    }

    @Override
    public void run() {
        if(testMy()) new Parser(this.webProxy, bot).start();
    }

    private Boolean testMy() {
        Connection.Response response = null;
        try {
            response = Jsoup
                    .connect("https://google.com")
                    .proxy(this.webProxy)
                    .headers(HTTPHeaders.DEFAULT_HEADERS)
                    .method(Connection.Method.GET)
                    .timeout(1000)
                    .execute();
            if (response.statusCode() == 200) return true;
            System.out.println(this.webProxy.address().toString() + " " + response.statusCode());
        } catch (IOException e) {
            System.out.println(this.webProxy.address().toString() + " - говно");
            return false;
        }
        return null;
    }
}
