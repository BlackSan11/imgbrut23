package ru.red.proxy;

import org.jsoup.Jsoup;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class ProxyStock {
    private static ProxyStock INSTANCE = new ProxyStock();
    private static volatile LinkedList<MyProxy> proxyList;

    private ProxyStock() {
        proxyList = new LinkedList<>();
        System.out.println("Proxy Stock started..");
        new ProxyUpdater().start();
    }

    public synchronized MyProxy getFreeOneMyProxy() {
        MyProxy thisProxy;
        while (true) {
            try{
                for (ListIterator<MyProxy> myProxyIter = proxyList.listIterator(); myProxyIter.hasNext();){
                    thisProxy = myProxyIter.next();
                    if(thisProxy.checkMe() && !thisProxy.getBusy()){
                        thisProxy.setBusy(true);
                        return thisProxy;
                    }
                    myProxyIter.remove();
                }
            } catch (ConcurrentModificationException e){

            }

            /*for (MyProxy myProxy : proxyList) {
                if(myProxy.checkMe() && !myProxy.getBusy()){
                    myProxy.setBusy(true);
                    return myProxy;
                }
            }*/
            /*for (ListIterator<MyProxy> thisProxyIt = proxyList.listIterator(); thisProxyIt.hasNext();) {
                MyProxy thisProxy = thisProxyIt.next();
                if (thisProxy.checkMe()) {
                    if (!thisProxy.getBusy()) {
                        thisProxy.setBusy(true);
                        return thisProxy;
                    }
                } else thisProxyIt.remove();
            }*/
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static ProxyStock getInstance() {
        return INSTANCE;
    }

    protected void updateProxyList(LinkedList<MyProxy> newProxyList){
        newProxyList.removeAll(proxyList);
        proxyList.addAll(newProxyList);
    }

}

class ProxyUpdater extends Thread {

    private volatile static LinkedList<MyProxy> proxyList = new LinkedList<>();
    private final static String PROXY_API_HOST = "http://hidemyna.me/ru/api/proxylist.php/api/proxylist.php?out=plain&type=hs&code=323478756239637";

    public void run() {
        System.out.println("Proxy Updater started..");
        while (true){
            LinkedList<MyProxy> newProxys = getNewProxyList();
            if (newProxys != null) {
                /*for (Iterator<MyProxy> thisProxyIt = newProxys.iterator(); thisProxyIt.hasNext(); ) {
                    MyProxy thisProxy = thisProxyIt.next();
                    if (thisProxy.checkMe() && !proxyList.contains(thisProxy)) proxyList.add(thisProxy);
                }*/
                ProxyStock.getInstance().updateProxyList(newProxys);
                newProxys.clear();
                System.out.println("Proxy list updated..");
            }
            try {
                Thread.sleep(180000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

