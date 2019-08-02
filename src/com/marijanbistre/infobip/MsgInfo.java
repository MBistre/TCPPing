package com.marijanbistre.infobip;

import javax.swing.*;
import java.awt.image.AreaAveragingScaleFilter;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MsgInfo {

    private String time;
    private int totalMsgCount;
    private int lastSecMsgCount;
    private int rbr;
    private String lastSecond;
    private Map<Integer, Integer> pitcherS, pitcherR, catcherS, catcherR;
    private Map<String, List<Integer>> oneSecondTimes;

    public MsgInfo(String time, int totalMsgCount, int lastSecMsgCount, int rbr, String lastSecond) throws IllegalArgumentException {
        this.time = time;
        this.totalMsgCount = totalMsgCount;
        this.lastSecMsgCount = lastSecMsgCount;
        this.rbr = rbr;
        this.lastSecond = lastSecond;
        this.pitcherS = new HashMap<Integer, Integer>();
        this.pitcherR = new HashMap<Integer, Integer>();
        this.catcherS = new HashMap<Integer, Integer>();
        this.catcherR = new HashMap<Integer, Integer>();
        this.oneSecondTimes = new HashMap<String, List<Integer>>();
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

    public void insertSentMessages(int pitcherTimeStamp, int catcherTimeStamp) {
        if(pitcherTimeStamp != 0) {
            this.pitcherS.put(this.rbr, pitcherTimeStamp);
        }
        if(catcherTimeStamp != 0) {
            this.catcherS.put(this.rbr, catcherTimeStamp);
        }
    }
    public void insertReceivedMessages(int pitcherTimeStamp, int catcherTimeStamp) {
        if(pitcherTimeStamp != 0) {
            this.pitcherR.put(this.rbr, pitcherTimeStamp);
        }
        if(catcherTimeStamp != 0) {
            this.catcherR.put(this.rbr, catcherTimeStamp);
        }
    }

    public int returnaba(int rbr) {

        int pitcherSentAt = this.pitcherS.get(rbr);
        int pitcherReceivedAt = this.pitcherR.get(rbr);

        int diffInMilliSeconds = pitcherReceivedAt - pitcherSentAt;
        System.out.println("Diff: pitcherReceivedAt (" + pitcherReceivedAt + ") - pitcherSentAt (" + pitcherSentAt + ") = diff(" + diffInMilliSeconds + ")");

        List<Integer> diffTimesArray = this.oneSecondTimes.get(this.rbr);
        if(diffTimesArray != null) {
            System.out.println(this.oneSecondTimes.get(this.rbr));;
            this.oneSecondTimes.put(this.time, diffTimesArray);
        } else {
            List<Integer> list = new ArrayList<Integer>();
            list.add(diffInMilliSeconds);
            this.oneSecondTimes.putIfAbsent(this.time, list);
        }

        return diffInMilliSeconds;
    }

    public int returnAverageOfLastSecond() {
        String lastSecond = this.lastSecond;
        Map<String, List<Integer>> mapLastSecond = this.oneSecondTimes;
        int averageTime = 0;
        if(!mapLastSecond.isEmpty()) {
            List<Integer> listLastSecond = mapLastSecond.get(rbr);
            int sum = 0;
            System.out.println(listLastSecond.get(0));
            for (int i=0; i<listLastSecond.size(); i++) {
                System.out.println(listLastSecond.get(i));
                sum += listLastSecond.get(i);
            }
            averageTime = sum / listLastSecond.size();
        }
        return averageTime;
    }

    public String toString() {
        String res = rbr + ". | " + time + " | Messages: " + totalMsgCount + " | Last second msg/s: " + lastSecMsgCount + " | A->B->A " + returnAverageOfLastSecond();
        return res;
    }




}
