package ru.red;

import org.apache.http.impl.client.CloseableHttpClient;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.facilities.TelegramHttpClientBuilder;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class Main {
    static String PROXY_LIST_FILE = "proxy.list";
    static String PROXY_LIST_FILE2 = "proxyS.list";

    public static void main(String[] args) throws IOException {
        new ProxyOperator().start();
        //DBO.getInstance();
        //new IdsGenerator().start();
        DefaultBotOptions defaultBotOptions = new DefaultBotOptions();
        defaultBotOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
        defaultBotOptions.setProxyHost("54.38.54.208");
        defaultBotOptions.setProxyPort(54321);
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        Bot bot = new Bot(defaultBotOptions);
        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        /*List<String> lines = Files.readAllLines(Paths.get(PROXY_LIST_FILE), StandardCharsets.UTF_8);
        //List<String> lines2 = Files.readAllLines(Paths.get(PROXY_LIST_FILE2), StandardCharsets.UTF_8);
        for (String line : lines) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(line.split(":")[0], Integer.parseInt(line.split(":")[1])));
            new ru.red.CustomProxy(proxy, bot).start();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
        }*/
    }


    public static Boolean testProxySocks(Proxy proxy) {
        System.setProperty("socksProxyVersion", "4");
        Connection.Response response = null;
        try {

            response = Jsoup
                    .connect("https://ya.ru")
                    .proxy(proxy)
                    .execute();
            if (response.statusCode() == 200) return true;
            System.out.println(proxy.address().toString() + " " + response.statusCode());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return null;
    }
}
