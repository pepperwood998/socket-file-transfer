package com.tuan.exercise.filetransfer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class ServerSide {
    ;

    public static void main(String[] args) throws IOException {

        Log.line(Constant.Server.MSG_TITLE);
        ServerSocket server = new ServerSocket(Constant.PORT);

        // waiting for client
        Log.line(Constant.Server.MSG_CLIENT_WAITING);
        Socket client = server.accept();
        Log.line(Constant.Server.MSG_CLIENT_CONNECTED);

        // get network client IO stream objects
        DataInputStream netInStream = new DataInputStream(new BufferedInputStream(client.getInputStream()));
        DataOutputStream netOutStream = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));

        // read file extension
        String fileExt = CustomIO.getStringMsg(netInStream);
        // if file info contains no extension
        if ("non".equals(fileExt)) {
            fileExt = "";
        }
        
        // storage I/O objects
        String outFileName = "output/vid" + fileExt;
        File outputFile = new File(outFileName);
        if (!outputFile.exists()) {
            outputFile.getParentFile().mkdirs();
        }
        FileOutputStream fileOutStream = new FileOutputStream(outputFile);
        BufferedOutputStream bufOutStream = new BufferedOutputStream(fileOutStream);

        // receive and save file from client
        Log.line(Constant.Server.MSG_READ_AND_SAVE);
        long before = System.nanoTime();
        CustomIO.saveFromNetwork(netInStream, bufOutStream);
        fileOutStream.close();
        long totalTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - before);

        Log.line("Total time: " + totalTime + " ms");
        Log.line(Constant.Server.MSG_SAVE_DONE);

        // send response to client
        CustomIO.sendStringMsg(netOutStream, Constant.Server.MSG_RES_FILE_RECEIVED);

        // clean up network
        netOutStream.close();
        netInStream.close();
        client.close();
        server.close();
    }
}
