package ru.red.parser;

import ru.red.proxy.ProxyStock;

public class ThreadActualizer extends Thread {
    @Override
    public void run() {
        while (true) {
            if (ParserOperator.getINSTANCE().getThreadsStartedCount() < ParserOperator.getINSTANCE().getThreadsCount()) {
                ParserOperator.getINSTANCE().upThreadsStartedCount();
                new ParserThread(ProxyStock.getInstance().getFreeOneMyProxy().getProxyObj()).start();
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
