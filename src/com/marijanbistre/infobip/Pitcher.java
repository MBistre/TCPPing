package com.marijanbistre.infobip;

import com.sun.nio.sctp.SendFailedNotification;

import java.awt.desktop.SystemSleepEvent;
import java.io.*;
import java.lang.reflect.GenericDeclaration;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;

public class Pitcher {
    private static Socket socket;

    public static void main(String args[]) throws Exception {

        // Connecting to socket
        String host = "localhost";
        int port = 25000;
        InetAddress address = InetAddress.getByName(host);
        String word = "test";
        socket = new Socket(address, port);
        System.out.println("Connected to server.. sending echo string");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String time = dtf.format(now);

        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        MsgCoder coder = new MsgTextCoder();
        Framer framer = new LengthFramer(socket.getInputStream());

        MsgInfo msgInfo = new MsgInfo(time, 0, 0, 1, time);
        byte[] encodedMsg = coder.toWire(msgInfo);

        while(msgInfo.getTotalMsgCount() < 1000) {
            System.out.println("Sending message (" + encodedMsg.length + " bytes): ");
            System.out.println(msgInfo);

            framer.frameMsg(encodedMsg, out);
            encodedMsg = framer.nextMsg();
            msgInfo = coder.fromWire(encodedMsg);
            Thread.sleep(300);
        }

        socket.close();
    }
}
