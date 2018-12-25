package ru.red.proxy;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.jsoup.Jsoup;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProxyStock {
    private static ProxyStock INSTANCE = new ProxyStock();
    private static volatile LinkedList<MyProxy> proxyList = new LinkedList<>();
    ;
    private final static String PROXY_API_HOST = "https://hidemyna.me/ru/api/proxylist.php?out=js&type=s&code=731616480369120&maxtime=2000";
    private String newProxyListStr;
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private Gson gson = new Gson();
    LinkedList<MyProxy> newProxysListColl;
    Type proxysСollectionType = new TypeToken<LinkedList<MyProxy>>() {
    }.getType();

    private ProxyStock() {
        System.out.println("Proxy Stock started..");

        Callable task = () -> {
            newProxyListStr = Jsoup
                    .connect(PROXY_API_HOST)
                    .execute().body();
            if (newProxyListStr != null && newProxyListStr.length() > 0) { //&& newProxyListStr.matches("[\\\\d]{1,3}[.]{1}[\\\\d]{1,3}[.]{1}[\\\\d]{1,3}[.]{1}[\\\\d]{1,3}[:]{1}[\\\\d]{1,}")
                newProxysListColl = gson.fromJson(newProxyListStr, proxysСollectionType);
                for (MyProxy myProxy : newProxysListColl) {
                    if (!proxyList.contains(myProxy)) proxyList.add(myProxy);
                    else System.out.println(myProxy + "  - уже есть");
                }
            } else System.out.println("|");
            return "";
        };
        scheduledExecutorService.schedule(task, 14, TimeUnit.SECONDS);
    }

    public MyProxy getFreeOneMyProxy() {
        Iterator<MyProxy> proxyIterator = proxyList.iterator();
        MyProxy thisProxy;
        while (proxyIterator.hasNext()) {
            thisProxy = proxyIterator.next();
            if (thisProxy.checkMe(thisProxy.getTestResponse())) {
                System.out.println(thisProxy.getIp() + " <<<");
                proxyIterator.remove();
                System.out.println(thisProxy.getIp() + " >>>>");
                return thisProxy;
            } else proxyIterator.remove();
        }
        return null;
    }

    public static ProxyStock getInstance() {
        return INSTANCE;
    }

}



