package com.tuan.exercise.filetransfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CustomIO {

    public static void sendFromStorage(InputStream fileIn, DataOutputStream netOut, long fileSize) throws IOException {
        netOut.writeLong(fileSize);

        int perRead = 0;
        byte[] buffer = new byte[Constant.FILE_BUFFER_SIZE];
        while ((perRead = fileIn.read(buffer)) > 0) {
            netOut.write(buffer, 0, perRead);
            Log.line("Bytes sent: " + perRead);
        }
        netOut.flush();
    }

    public static void saveFromNetwork(DataInputStream netIn, OutputStream fileOut) throws IOException {
        long fileSize = netIn.readLong();

        int perRead = 0;
        long totalRead = 0;
        byte[] buffer = new byte[Constant.FILE_BUFFER_SIZE];
        while ((perRead = netIn.read(buffer)) > 0) {
            fileOut.write(buffer, 0, perRead);
            Log.line("Bytes read: " + perRead);

            totalRead += perRead;
            if (totalRead >= fileSize)
                break;
        }
        fileOut.flush();
    }

    public static void sendStringMsg(DataOutputStream netOut, String msg) throws IOException {
        netOut.writeLong(msg.getBytes().length);
        netOut.write(msg.getBytes());
        netOut.flush();
    }

    public static String getStringMsg(DataInputStream netIn) throws IOException {
        long msgSize = netIn.readLong();

        StringBuilder res = new StringBuilder();
        int perRead = 0;
        long totalRead = 0;
        byte[] strBuffer = new byte[Constant.MSG_BUFFER_SIZE];
        while ((perRead = netIn.read(strBuffer)) > 0) {
            res.append(new String(strBuffer, 0, perRead));

            totalRead += perRead;
            if (totalRead >= msgSize)
                break;
        }

        return res.toString();
    }
}
