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
    String userAgent;

    public ParserThread(Proxy proxy, String userAgent) {
        this.proxy = proxy;
        this.userAgent = userAgent;
        System.out.println("Parser thread started on " + proxy.address().toString());
    }

    public Boolean testConnection() {
        try {
            Connection.Response response = Jsoup
                    .connect("https://postimg.cc/V0vsQV79")
                    .headers(HTTPHeaders.DEFAULT_HEADERS)
                    .header("User-Agent",userAgent)
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
        String id,srv;
        while (true) {
            if (!testConnection()) {
                System.out.println(proxy.address().toString() + " - не прошло тест, поток завершаеться");
                ParserOperator.getINSTANCE().downThreadsStartedCount();
                break;
            }
            id = DBO.getInstance().getSinglePhoto();
            srv = ParserOperator.getINSTANCE().getServer();
            try {
                Connection.Response doc = Jsoup.connect("http://"+ srv +".postimg.cc/" + id)
                        .headers(HTTPHeaders.DEFAULT_HEADERS)
                        .header("User-Agent", userAgent)
                        .followRedirects(true)
                        .proxy(proxy)
                        .execute();
                System.out.println("####200OK-" + id);
                Document parsedDoc = doc.parse();
                DBO.getInstance().updateAfterCheck(id, srv, "exist", parsedDoc.getElementsByClass("imagename").text(), parsedDoc.getElementById("download").attr("href"));
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
                Thread.sleep(ThreadLocalRandom.current().nextInt(4000, 9000 + 1));
            } catch (InterruptedException e) {
               // e.printStackTrace();
            }
        }
    }
}
