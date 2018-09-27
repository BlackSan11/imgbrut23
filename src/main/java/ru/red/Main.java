package ru.red;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.red.Tlg.Bot;
import ru.red.parser.ParserOperator;
import ru.red.proxy.MyProxy;
import ru.red.proxy.ProxyStock;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Main {

    public static void main(String[] args) {

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        Bot bot;
        while (true){
            System.out.println("перестарт");
            MyProxy proxyForBot = ProxyStock.getInstance().getFreeOneMyProxy();
            DefaultBotOptions defaultBotOptions = new DefaultBotOptions();
            defaultBotOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
            defaultBotOptions.setProxyHost(proxyForBot.getIp());
            defaultBotOptions.setProxyPort(proxyForBot.getPort());
            bot = new Bot(defaultBotOptions);
            try {
                telegramBotsApi.registerBot(bot);
                System.out.println("Супер");
                break;
            } catch (TelegramApiException e) {
                continue;
            }
        }
        ProxyStock.getInstance();
        ParserOperator.getINSTANCE();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Bot finalBot = bot;
        Runnable task = () -> finalBot.sendInfo();
        int initialDelay = 0;
        int period = 10;
        executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MINUTES);

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
