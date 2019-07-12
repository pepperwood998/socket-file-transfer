package com.tuan.exercise.filetransfer;

public class Constant {

    public static final int FILE_BUFFER_SIZE = 10 * 1024;
    public static final int MSG_BUFFER_SIZE = 1024;
    public static final int PORT = 6969;

    public static class Server {

        public static final String MSG_TITLE = "--- Server Program ---";
        public static final String MSG_CLIENT_WAITING = "Waiting for client...";
        public static final String MSG_CLIENT_CONNECTED = "A client connected\n";
        public static final String MSG_READ_AND_SAVE = "Reading and saving file to storage...";
        public static final String MSG_SAVE_DONE = "File saved done";
        public static final String MSG_RES_FILE_RECEIVED = "File received by server";
    }

    public static class Client {

        public static final String DOMAIN_SERVER = "localhost";
        public static final String MSG_TITLE = "--- Client Program ---";
        public static final String MSG_CONNECTED = "Connected to server\n";
        public static final String MSG_SEND_DONE = "File sent to server done";
        public static final String MSG_READ_AND_SEND = "Reading and sending file to server...";
    }
}
