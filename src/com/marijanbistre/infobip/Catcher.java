package com.marijanbistre.infobip;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class Catcher {
    private static Socket socket;
    private static final int BUFFSIZE = 32;

    public static void main(String[] args) throws Exception{

        int port = 25000;
        String ipAddress = "localhost";
        InetAddress address = InetAddress.getByName(ipAddress);

        ServerSocket serverSocket = new ServerSocket(port, 50, address);
        System.out.println("Server started and listening to the port 25000");

        MsgCoder coder = new MsgTextCoder();
        MsgService service = new MsgService();

        while(true) {
            socket = serverSocket.accept();
            System.out.println("Handling client at " + socket.getRemoteSocketAddress());
            Framer framer = new LengthFramer(socket.getInputStream());
            try {
                byte[] req;
                while ((req = framer.nextMsg()) != null) {
                    System.out.println("Received message ("+req.length+" bytes)");
                    MsgInfo responseMsg = service.handleRequest(coder.fromWire(req));
                    framer.frameMsg(coder.toWire(responseMsg), socket.getOutputStream());
                }
             } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                System.out.println("Closing connection");
                socket.close();
            }
        }

    }
}
