package ru.red;

import ru.red.proxy.ProxyStock;

public class T {
    public static void main(String[] args) throws InterruptedException {
        ProxyStock.getInstance();

        Thread.sleep(20000);
        while (true){
            ProxyStock.getInstance().getFreeOneMyProxy();
        }

    }
}
