package ru.red.proxy;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProxyStock {
    private static final ProxyStock INSTANCE = new ProxyStock();
    private volatile LinkedList<MyProxy> proxyList = new LinkedList<>();
    private final static String PROXY_API_HOST = "https://hidemyna.me/ru/api/proxylist.php?out=js&type=s&code=731616480369120&maxtime=2000";
    private String newProxyListStr;
    private Gson gson = new Gson();
    LinkedList<MyProxy> newProxysListColl;
    Type proxysСollectionType = new TypeToken<LinkedList<MyProxy>>(){}.getType();

    private ProxyStock() {
        System.out.println("Proxy Stock started..");
        try {
            updateProxyList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateProxyList() throws IOException {
        System.out.println("Обновляем список проксей.");
        newProxyListStr = Jsoup
                .connect(PROXY_API_HOST)
                .execute().body();
        if (newProxyListStr != null && newProxyListStr.length() > 0) { //&& newProxyListStr.matches("[\\\\d]{1,3}[.]{1}[\\\\d]{1,3}[.]{1}[\\\\d]{1,3}[.]{1}[\\\\d]{1,3}[:]{1}[\\\\d]{1,}")
            newProxysListColl = gson.fromJson(newProxyListStr, proxysСollectionType);
            int c = 0;
            for (MyProxy myProxy : newProxysListColl) {
                if (this.proxyList == null || !this.proxyList.contains(myProxy)) {
                    this.proxyList.add(myProxy);
                    c++;
                }
                else System.out.println(myProxy.getIp() + "  - уже есть");
            }
            System.out.println("Добавлено " + c + " проксей из " + newProxysListColl.size() + " полученных.");
        } else System.out.println("|");
    }

    public MyProxy getOneFreeMyProxy() {
        int c = 0;
        while (true){
            for (MyProxy myProxy : this.proxyList) {
                if (!myProxy.isBysu()) {
                    if(myProxy.checkMe(myProxy.getTestResponse())){
                        System.out.println(myProxy.getIp() + " <<< " + this.proxyList.size());
                        myProxy.setBysu(true);
                        return myProxy;
                    } else {
                        myProxy.setBysu(true);
                    }
                }
            }
            try {
                if(c > 1) Thread.sleep(120000);
                this.updateProxyList();
                c++;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static ProxyStock getInstance() {
        return INSTANCE;
    }

}



