package com.marijanbistre.infobip;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgService {

    private Map<Integer, List<String>> infoMap = new HashMap<Integer, List<String>>();

    public MsgInfo handleRequest(MsgInfo info) {
        int rbr = info.getRbr();
        int totalMsgCount;
        int lastSecMsgCount;
        String time = info.getTime();
        totalMsgCount = info.getTotalMsgCount() + 1;
        if(!info.getTime().equals(info.getLastSecond())) {
            info.setLastSecMsgCount(0);
        }
        lastSecMsgCount = info.getLastSecMsgCount() + 1;

        info.setTotalMsgCount(totalMsgCount);
        info.setLastSecMsgCount(lastSecMsgCount);
        // novi item u Mapu - rbr, countovi
        List<String> infoMsg = new ArrayList<>();
        infoMsg.add(Integer.toString(totalMsgCount));
        infoMsg.add(Integer.toString(lastSecMsgCount));
        infoMap.put(rbr, infoMsg);
        info.setRbr(rbr +1);
        info.setLastSecond(time);
        return info;
    }
}
