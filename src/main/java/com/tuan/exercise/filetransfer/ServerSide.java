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
    private static final int PORT = 6969;
    private static final String MSG_TITLE = "--- Server Program ---";
    private static final String MSG_CLIENT_WAITING = "Waiting for client...";
    private static final String MSG_CLIENT_CONNECTED = "A client connected\n";
    private static final int FILE_BUFFER_SIZE = 10 * 1024;
    private static final String MSG_READ_AND_SAVE = "Reading and saving file to storage...";
    private static final String MSG_SAVE_DONE = "File saved done";
    private static final String MSG_RES_FILE_RECEIVED = "File received by server";

    public static void main(String[] args) throws IOException {

        Log.line(MSG_TITLE);
        ServerSocket server = new ServerSocket(PORT);

        // waiting for client
        Log.line(MSG_CLIENT_WAITING);
        Socket client = server.accept();
        Log.line(MSG_CLIENT_CONNECTED);

        // get network client IO stream objects
        DataInputStream netInStream = new DataInputStream(new BufferedInputStream(client.getInputStream()));
        DataOutputStream netOutStream = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));

        // storage I/O objects
        String outFileName = "output/vid";
        File outputFile = new File(outFileName);
        if (!outputFile.exists()) {
            outputFile.getParentFile().mkdirs();
        }
        FileOutputStream fileOutStream = new FileOutputStream(outputFile);
        BufferedOutputStream bufOutStream = new BufferedOutputStream(fileOutStream);

        // read file size
        long fileSize = netInStream.readLong();

        Log.line(MSG_READ_AND_SAVE);
        // receive and save file from client
        int bytesRead = 0;
        long totalRead = 0;
        byte[] buffer = new byte[FILE_BUFFER_SIZE];
        long before = System.nanoTime();
        while ((bytesRead = netInStream.read(buffer)) > 0) {
            bufOutStream.write(buffer, 0, bytesRead);
            Log.line("Bytes read: " + bytesRead);

            totalRead += bytesRead;
            if (totalRead >= fileSize)
                break;
        }
        bufOutStream.flush();
        fileOutStream.close();
        long totalTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - before);
        Log.line("Total time: " + totalTime + " ms");
        Log.line(MSG_SAVE_DONE);

        // send response to client
        netOutStream.writeLong(MSG_RES_FILE_RECEIVED.getBytes().length);
        netOutStream.write(MSG_RES_FILE_RECEIVED.getBytes());
        netOutStream.flush();

        // clean up network
        netOutStream.close();
        netInStream.close();
        client.close();
        server.close();
    }
}
