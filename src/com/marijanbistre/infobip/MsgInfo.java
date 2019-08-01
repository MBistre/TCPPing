package com.marijanbistre.infobip;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MsgInfo {

    private String time;
    private int totalMsgCount;
    private int lastSecMsgCount;
    private int rbr;
    private String lastSecond;

    public MsgInfo(String time, int totalMsgCount, int lastSecMsgCount, int rbr, String lastSecond) throws IllegalArgumentException {
        this.time = time;
        this.totalMsgCount = totalMsgCount;
        this.lastSecMsgCount = lastSecMsgCount;
        this.rbr = rbr;
        this.lastSecond = lastSecond;
    }

    public String getTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        time = dtf.format(now);
        return time;
    }
    public String getLastSecond() {
        return lastSecond;
    }
    public void setLastSecond(String lastSecond) {
        this.lastSecond = lastSecond;
    }

    public void setTotalMsgCount(int totalMsgCount) {
        this.totalMsgCount = totalMsgCount;
    }
    public int getTotalMsgCount() {
        return totalMsgCount;
    }

    public void setLastSecMsgCount(int lastSecMsgCount) {
        this.lastSecMsgCount = lastSecMsgCount;
    }
    public int getLastSecMsgCount() {
        return lastSecMsgCount;
    }

    public void setRbr(int rbr) {
        this.rbr = rbr;
    }

    public int getRbr() {
        return rbr;
    }

    public String toString() {
        String res = rbr + ". Time : " + time + ", Total count: " + totalMsgCount + " Total count in last second: " + lastSecMsgCount + " Last time was " + lastSecond;
        return res;
    }




}
