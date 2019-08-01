package com.marijanbistre.infobip;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class MsgTextCoder implements MsgCoder {

    public static final String MAGIC = "Infobip";
    public static final String RESPONSESTR = "R";
    public static final String CHARSETNAME = "US-ASCII";
    public static final String DELIMITER = " ";
    public static final int MAXWIRELENGTH = 3000;


    @Override
    public byte[] toWire(MsgInfo info) throws IOException {
        String msgString = MAGIC + DELIMITER
                + info.getTime() + DELIMITER
                + Integer.toString(info.getTotalMsgCount()) + DELIMITER
                + Integer.toString(info.getLastSecMsgCount()) + DELIMITER
                + Integer.toString(info.getRbr()) + DELIMITER
                + info.getLastSecond();
        byte data[] = msgString.getBytes(CHARSETNAME);
        return data;
    }

    @Override
    public MsgInfo fromWire(byte[] input) throws IOException {
        ByteArrayInputStream msgStream = new ByteArrayInputStream(input);
        Scanner sc = new Scanner(new InputStreamReader(msgStream, CHARSETNAME));
        String time;
        int totalMsgCount;
        int lastSecMsgCount;
        int rbr;
        String lastSecond;

        String token;
        try {
            token = sc.next();
            if(!token.equals(MAGIC)) {
                throw new IOException("Bad magic string: " + token);
            }

            token = sc.next();
            time = token;

            token = sc.next();
            totalMsgCount = Integer.parseInt(token);

            token = sc.next();
            lastSecMsgCount = Integer.parseInt(token);

            token = sc.next();
            rbr = Integer.parseInt(token);

            token = sc.next();
            lastSecond = token;

        } catch (IOException ioe) {
            throw new IOException("Parse error...");
        }

        return new MsgInfo(time, totalMsgCount, lastSecMsgCount, rbr, lastSecond);
    }
}
