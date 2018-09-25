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

        //DBO.getInstance();
        //new IdsGenerator().start();
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        Bot bot;
        while (true){
            System.out.println("перестарт");
            MyProxy proxyForBot = ProxyStock.getInstance().getFreeOneMyProxy();
            DefaultBotOptions defaultBotOptions = new DefaultBotOptions();
            defaultBotOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
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
