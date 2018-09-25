package ru.red.parser;

import ru.red.proxy.ProxyStock;

public class ParserOperator {
    private static ParserOperator INSTANCE = new ParserOperator();
    private volatile String server = "s33";
    private volatile Integer threadsCount = 400;
    private volatile int threadsStartedCount = 0;

    private ParserOperator() {
        pasrsingStart();
    }

    private void pasrsingStart(){
        new ThreadActualizer().start();
    }

    public Integer getThreadsCount() {
        return threadsCount;
    }

    public synchronized int getThreadsStartedCount() {
        return threadsStartedCount;
    }

    public synchronized void upThreadsStartedCount() {
        this.threadsStartedCount += 1;
    }

    public synchronized void downThreadsStartedCount() {
        this.threadsStartedCount -= 1;
    }

    public synchronized String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public static ParserOperator getINSTANCE() {
        return INSTANCE;
    }
}
