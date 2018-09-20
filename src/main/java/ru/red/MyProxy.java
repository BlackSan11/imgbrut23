package ru.red;


import com.sun.deploy.net.proxy.ProxyType;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class MyProxy {
    private Proxy.Type type;
    private String ip = null;
    private Integer port = null;
    private Proxy proxyObj = null;
    public MyProxy(String ip, Integer port, Proxy.Type type) {
        this.ip = ip;
        this.port = port;
        this.type = type;
        this.proxyObj = new Proxy(type, new InetSocketAddress(this.ip, this.port));
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public Proxy.Type getType() {
        return type;
    }

    public Boolean checkMe(){
        if(proxyObj == null) return null;
        try {
            Connection.Response response = Jsoup
                    .connect("https://google.com")
                    .proxy(proxyObj)
                    .headers(HTTPHeaders.DEFAULT_HEADERS)
                    .execute();
            if(response.statusCode() == 200) return true;
        } catch (IOException e) {
            return false;
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        MyProxy getedMyProxy = (MyProxy) obj;
        if(this.ip.equals(getedMyProxy.getIp()) && this.port == getedMyProxy.getPort()){
            return true;
        }else return false;
    }
}
