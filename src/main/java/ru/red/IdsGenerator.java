package ru.red;

import org.bson.Document;

import java.security.SecureRandom;
import java.util.Date;
import java.util.LinkedList;

public class IdsGenerator extends Thread {

    private final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
    SecureRandom rnd = new SecureRandom();

    @Override
    public void run() {
        LinkedList linkedList;
        Document document;
        Document document2;
        while (true) {
            linkedList = new LinkedList();
            document2 = new Document();
            document2.put("status", "");
            document2.put("updated", new Date().getTime() / 1000);
            for (int i = 0; i < 100000; i++){
                document = new Document();
                document.put("_id", randomString(8));
                document.putAll(document2);
                linkedList.add(document);
            }
            DBO.getInstance().addSinglePhotos(linkedList);
        }
    }

    String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(CHARS.charAt(rnd.nextInt(CHARS.length())));
        return sb.toString();
    }
}
