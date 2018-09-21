package ru.red.parser;

public class SinglePhoto {
    private String sID = "";
    private String act = "";

    public SinglePhoto(String sID, String act) {
        this.sID = sID;
        this.act = act;
    }

    public String getsID() {
        return sID;
    }

    public String getAct() {
        return act;
    }
}
