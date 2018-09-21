package ru.red.parser;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.red.db.DBO;
import java.io.IOException;
import java.net.Proxy;
import java.util.concurrent.ThreadLocalRandom;

public class ParserThread extends Thread {

    Proxy proxy;

    public ParserThread(Proxy proxy) {
        this.proxy = proxy;
        System.out.println("Parser thread started on " + proxy.address().toString());
    }

    public Boolean testConnection() {
        try {
            Connection.Response response = Jsoup
                    .connect("https://postimg.cc/")
                    .proxy(proxy)
                    .execute();
            if (response.statusCode() == 200) return true;
        } catch (IOException e) {
            //e.printStackTrace();
            return false;
        }
        return null;
    }

    public void run() {
        String id;
        while (true) {
            if (!testConnection()) {
                System.out.println(proxy.address().toString() + " - не прошло тест, поток завершаеться");
                ParserOperator.getINSTANCE().downThreadsStartedCount();
                break;
            }
            id = DBO.getInstance().getSinglePhoto();
            try {
                Connection.Response doc = Jsoup.connect("https://postimg.cc/" + id + "/")
                        .headers(HTTPHeaders.DEFAULT_HEADERS)
                        .proxy(proxy)
                        .execute();
                System.out.println("####200OK-" + id);
                Document parsedDoc = doc.parse();
                DBO.getInstance().updateAfterCheck(id, "exist", parsedDoc.getElementsByClass("imagename").text(), parsedDoc.getElementById("download").attr("href"));
            } catch (HttpStatusException e) {
                System.out.println("####404BAD-" + id);
                //ЕСЛИ 404
                if (e.getStatusCode() == 404) {
                    DBO.getInstance().updateAfterCheck(id, "notexist");
                } else { //ЕСЛИ ДРУГАЯ
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(4000, 10000 + 1));
            } catch (InterruptedException e) {
               // e.printStackTrace();
            }
        }
    }
}
