package com.marijanbistre.infobip;

import com.sun.nio.sctp.SendFailedNotification;

import java.awt.desktop.SystemSleepEvent;
import java.io.*;
import java.lang.reflect.GenericDeclaration;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Pitcher extends Thread{
    private static Socket socket;

    public static void main(String args[]) throws Exception {

        // Connecting to socket
        String host = "localhost";
        int port = 25000;
        InetAddress address = InetAddress.getByName(host);
        // Broj poruka po minuti i izračun za Thred.sleep()
        int rate = 3;
        double sec = (1d/rate)*1000d;
        int msgs = (int)sec;
        socket = new Socket(address, port);
        System.out.println("Connected to server.. sending echo string");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String time = dtf.format(now);

        // Slanje poruka catcheru
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        MsgCoder coder = new MsgTextCoder();
        Framer framer = new LengthFramer(socket.getInputStream());

        MsgInfo msgInfo = new MsgInfo(time, 0, 0, 1, time);
        byte[] encodedMsg = coder.toWire(msgInfo);

        int expectedRbr = 1;
        while(msgInfo.getTotalMsgCount() <= 1000) {

            if(!msgInfo.getLastSecond().equals(msgInfo.getTime())) {
                System.out.println(msgInfo);
            }
            if(msgInfo.getTotalMsgCount() >= 1000) {
                System.out.println(msgInfo);
            }
            framer.frameMsg(encodedMsg, out);
            encodedMsg = framer.nextMsg();
            msgInfo = coder.fromWire(encodedMsg);
            msgInfo.setLastSecond(msgInfo.getTime());
            msgInfo.insertSentMessages((int) System.currentTimeMillis(), 0);

            // primanje poruke od catchera
            expectedRbr++;
            int message = in.read();
            msgInfo.insertReceivedMessages((int) System.currentTimeMillis(), 0);
            if(message != expectedRbr){
                System.out.println(expectedRbr + ". message wasn't received." + message);
            }
            Thread.sleep(msgs);
        }
        socket.close();
    }
}