package com.marijanbistre.infobip;


import java.io.*;

public class LengthFramer implements Framer {

    public static final int MAXMESSAGELENGTG = 65535;
    public static final int BYTEMASK = 0xff;
    public static final int SHORMASK = 0xffff;
    public static final int BYTESHIFT = 8;

    private DataInputStream in;

    public LengthFramer(InputStream in) throws IOException {
        this.in = new DataInputStream(in);
    }

    @Override
    public void frameMsg(byte[] message, OutputStream out) throws IOException {
        if(message.length > MAXMESSAGELENGTG) {
            throw new IOException("Message too long");
        }
        out.write((message.length >>BYTESHIFT) & BYTEMASK);
        out.write(message.length & BYTEMASK);
        out.write(message);
        out.flush();
    }

    @Override
    public byte[] nextMsg() throws IOException {
        int length;
        try {
            length = in.readUnsignedShort();
        } catch (EOFException e) {
            return null;
        }
        byte[] msg = new byte[length];
        in.readFully(msg);
        return msg;
    }
}
