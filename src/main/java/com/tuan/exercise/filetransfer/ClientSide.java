package com.tuan.exercise.filetransfer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSide {
    private static final String DOMAIN_SERVER = "localhost";
    private static final int PORT_SERVER = 6969;
    private static final String MSG_TITLE = "--- Client Program ---";
    private static final String MSG_CONNECTED = "Connected to server\n";
    private static final int FILE_BUFFER_SIZE = 10 * 1024;
    private static final String MSG_SEND_DONE = "File sent to server done";
    private static final String MSG_READ_AND_SEND = "Reading and sending file to server...";

    public static void main(String[] args) throws UnknownHostException, IOException {

        Log.line(MSG_TITLE);
        Socket client = new Socket(DOMAIN_SERVER, PORT_SERVER);
        Log.line(MSG_CONNECTED);

        // get network I/O stream objects
        DataOutputStream netOutStream = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
        DataInputStream netInStream = new DataInputStream(new BufferedInputStream(client.getInputStream()));

        // storage I/O objects
        String inFileName = "input/vid";
        File file = new File(inFileName);
        if (!file.exists()) {
            client.close();
            return;
        }
        FileInputStream fileInSream = new FileInputStream(file);
        BufferedInputStream bufInStream = new BufferedInputStream(fileInSream);

        // send file size
        netOutStream.writeLong(file.length());
        Log.line(MSG_READ_AND_SEND);
        // send file to server
        int bytesRead = 0;
        byte[] buffer = new byte[FILE_BUFFER_SIZE];
        while ((bytesRead = bufInStream.read(buffer)) > 0) {
            netOutStream.write(buffer, 0, bytesRead);
            Log.line("Bytes sent: " + bytesRead);
        }
        netOutStream.flush();
        fileInSream.close();
        Log.line(MSG_SEND_DONE);

        long msgSize = netInStream.readLong();
        // get response from server
        StringBuilder res = new StringBuilder();
        int msgRead = 0;
        long totalRead = 0;
        byte[] charBuf = new byte[1024];
        while ((msgRead = netInStream.read(charBuf)) > 0) {
            res.append(new String(charBuf, 0, msgRead));

            totalRead += msgRead;
            if (totalRead >= msgSize)
                break;
        }
        Log.line(res.toString());

        // clean up network
        netOutStream.close();
        netInStream.close();
        client.close();
    }
}
