package ru.red.parser;

import ru.red.proxy.ProxyStock;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class ThreadActualizer extends Thread {
    LinkedList<String> linkedList = new LinkedList<>();
    @Override
    public void run() {
        try{

            FileInputStream fstream = new FileInputStream("useragents.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            while ((strLine = br.readLine()) != null){
                linkedList.add(strLine);
            }
        }catch (IOException e){
            System.out.println("Ошибка");
        }
        while (true) {
            if (ParserOperator.getINSTANCE().getThreadsStartedCount() < ParserOperator.getINSTANCE().getThreadsCount()) {
                ParserOperator.getINSTANCE().upThreadsStartedCount();
                new ParserThread(ProxyStock.getInstance().getOneFreeMyProxy().getProxyObj(), linkedList.get(ThreadLocalRandom.current().nextInt(linkedList.size() - 1))).start();
            } else {
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                   // e.printStackTrace();
                }
            }
        }
    }
}
