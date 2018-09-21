package ru.red.parser;

import ru.red.proxy.ProxyStock;

public class ParserOperator {
    private static ParserOperator INSTANCE = new ParserOperator();
    private volatile Integer threadsCount = 100;
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


    public static ParserOperator getINSTANCE() {
        return INSTANCE;
    }
}
