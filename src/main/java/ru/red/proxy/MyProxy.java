package ru.red.proxy;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Objects;

public class MyProxy {
    private final Proxy.Type type = Proxy.Type.HTTP;
    private String ip = null;
    private Integer port = null;
    private Proxy proxyObj = null;

    public MyProxy(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }


    public Proxy.Type getType() {
        return this.type;
    }
    public Response getTestResponse(){
        try {
            return Jsoup
                        .connect("https://ya.ru")
                        .proxy(this.getProxyObj())
                        .method(Connection.Method.GET)
                        .timeout(3000)
                        .execute();
        } catch (IOException e) {
            return null;
        }
    }

    public boolean checkMe(Response response){
        return (response != null && response.statusCode() == 200);
    }

    public Proxy getProxyObj() {
        if (proxyObj == null){
            this.proxyObj = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.ip, this.port));
        }
        return proxyObj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyProxy myProxy = (MyProxy) o;
        return Objects.equals(ip, myProxy.ip) &&
                Objects.equals(port, myProxy.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }

    public String getIp() {
        return this.ip;
    }

    public Integer getPort() {
        return this.port;
    }

}


