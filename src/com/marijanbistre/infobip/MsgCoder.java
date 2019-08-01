package com.marijanbistre.infobip;

import java.io.IOException;

public interface MsgCoder {
    byte[] toWire(MsgInfo info) throws IOException;
    MsgInfo fromWire(byte[] input) throws IOException;
}
