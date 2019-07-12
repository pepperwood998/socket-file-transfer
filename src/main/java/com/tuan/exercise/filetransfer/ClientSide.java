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

    public static void main(String[] args) throws UnknownHostException, IOException {

        Log.line(Constant.Client.MSG_TITLE);
        Socket client = new Socket(Constant.Client.DOMAIN_SERVER, Constant.PORT);
        Log.line(Constant.Client.MSG_CONNECTED);

        // get network I/O stream objects
        DataOutputStream netOutStream = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
        DataInputStream netInStream = new DataInputStream(new BufferedInputStream(client.getInputStream()));

        // storage I/O objects
        // file type is already stored in the header of that file
        String inFileName = "input/vid.mp4";
        File file = new File(inFileName);
        if (!file.exists()) {
            client.close();
            return;
        }
        FileInputStream fileInSream = new FileInputStream(file);
        BufferedInputStream bufInStream = new BufferedInputStream(fileInSream);

        // send file extension
        String fileName = file.getName();
        String fileExt = "non";
        char delim = '.';
        int delimInd = fileName.lastIndexOf((int) delim);
        if (delimInd > 0) {
            fileExt = fileName.substring(delimInd);
        }
        CustomIO.sendStringMsg(netOutStream, fileExt);

        // read and send file to server
        Log.line(fileExt);
        Log.line(Constant.Client.MSG_READ_AND_SEND);
        CustomIO.sendFromStorage(bufInStream, netOutStream, file.length());
        Log.line(Constant.Client.MSG_SEND_DONE);

        // read server response
        Log.line(CustomIO.getStringMsg(netInStream));

        // clean up network
        netOutStream.close();
        netInStream.close();
        client.close();
    }
}
