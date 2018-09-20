package ru.red;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Proxy;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

public class Parser extends Thread {
    final String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
    SecureRandom rnd = new SecureRandom();
    Proxy proxy;
    Bot bot;

    public Parser(Proxy proxy, Bot bot) {
        this.proxy = proxy;
        System.out.println("Started on " + proxy.address().toString() + " Процессов" + Thread.activeCount());
        this.bot = bot;
    }

    //" + ru.red.DBO.getInstance().getLiveSinglePhoto() + "/"
    public Boolean testConnection() {
        try {
            Connection.Response response = Jsoup
                    .connect("https://postimg.cc/")
                    .proxy(proxy)
                    .execute();
            if (response.statusCode() == 200) return true;
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
                if (!testConnection()) {
                    System.out.println(proxy.address().toString() + " не работает");
                    break;
                }
                id = DBO.getInstance().getSinglePhoto();
                try {
                    Connection.Response doc = Jsoup.connect("https://postimg.cc/" + id + "/")
                            .headers(HTTPHeaders.DEFAULT_HEADERS)
                            .proxy(proxy)
                            .execute();
                    //https://postimg.cc/gallery/3i7dgk7z5/
                    //String image = doc.parse().getElementById("code_gallery").val();
                    goodsLF.write(id + "\n");
                    goodsLF.flush();
                    Document parsedDoc = doc.parse();
                    //bot.sendPhoto((long) 120988325, parsedDoc.getElementsByClass("imagename").text(), parsedDoc.getElementById("download").attr("href"), id);
                    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + parsedDoc.getElementById("download").attr("href"));
                    DBO.getInstance().updateAfterCheck(id, "exist",  parsedDoc.getElementsByClass("imagename").text(), parsedDoc.getElementById("download").attr("href"));
                } catch (HttpStatusException e) {
                    e.printStackTrace();
                    //ЕСЛИ 404
                    if (e.getStatusCode() == 404) {
                        badsLF.write(id + "\n");
                        badsLF.flush();
                        DBO.getInstance().updateAfterCheck(id, "notexist");
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
