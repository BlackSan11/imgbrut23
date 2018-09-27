package ru.red.proxy;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import ru.red.parser.HTTPHeaders;
import sun.net.SocksProxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.NoSuchElementException;

public class MyProxy {
    private Proxy.Type type;
    private String ip = null;
    private Integer port = null;
    private SocksProxy proxyObj = null;
    private Boolean busy = false;

    public MyProxy(String ip, Integer port, Proxy.Type type) {
        this.ip = ip;
        this.port = port;
        this.type = type;
        SocketAddress addr = new InetSocketAddress(this.ip, this.port);
        this.proxyObj = SocksProxy.create(addr, 5);
    }

    public String getIp() {
        return this.ip;
    }

    public Integer getPort() {
        return this.port;
    }

    public Proxy.Type getType() {
        return this.type;
    }

    public Boolean checkMe(){
        if(proxyObj == null) return false;
        try {
            Connection.Response response = Jsoup
                    .connect("https://google.com")
                    .proxy(proxyObj)
                    .headers(HTTPHeaders.DEFAULT_HEADERS)
                    .timeout(5000)
                    .execute();
            if(response.statusCode() == 200) return true;
        } catch (NoSuchElementException e){
            return false;
        }catch (IOException e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
//        if (obj == null || obj.getClass().equals(this.getClass())) {
//            return false;
//        }*/
        MyProxy getedMyProxy = (MyProxy) obj;
        if(this.ip.equals(getedMyProxy.getIp()) && this.port == getedMyProxy.getPort()){
            return true;
        }else return false;
    }

    public Proxy getProxyObj() {
        return this.proxyObj;
    }

    public Boolean getBusy() {
        return this.busy;
    }

    public void setBusy(Boolean busy) {
        this.busy = busy;
    }
}
