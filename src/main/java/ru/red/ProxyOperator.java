package ru.red;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.util.LinkedList;

public class ProxyOperator extends Thread {

    private final static String PROXY_API_HOST = "http://hidemyna.me/ru/api/proxylist.php/api/proxylist.php?out=plain&type=hs&code=323478756239637";
    private static LinkedList<MyProxy> actualProxies = new LinkedList<>();

    @Override
    public void run() {
        getNewProxyList();
        System.out.println("d");
    }

    private void proxyActualizer(){
        LinkedList<MyProxy> newProxys = getNewProxyList();
        if(newProxys != null){
            for (MyProxy newProxy : newProxys) {
                actualProxies.containsAll()
            }
        }
        if(actualProxies.size() > 0){

        }
        try {
            Thread.sleep(180000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private LinkedList<MyProxy> getNewProxyList() {
        try {
            InputStream inputStream = Jsoup
                    .connect(PROXY_API_HOST)
                    .execute().bodyStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String strLine;
            LinkedList<MyProxy> getedProxy = new LinkedList<>();
            while ((strLine = reader.readLine()) != null) {
                getedProxy.add(new MyProxy(strLine.split(":")[0], Integer.valueOf(strLine.split(":")[1]), Proxy.Type.HTTP));
            }
            return getedProxy;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
