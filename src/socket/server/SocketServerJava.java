package socket.server;

import java.io.*;
import java.net.*;


public class SocketServerJava {
    public static void main(String[] args) {
        ServerSocket server = null;
        int connectCount = 0;

        try {
            server = new ServerSocket(5000);

            while (true) {
                Socket connectedClientSocket = server.accept();
                InetAddress ia = connectedClientSocket.getInetAddress();

                // client port
                int port = connectedClientSocket.getLocalPort();
                // client ip
                String ip = ia.getHostAddress();

                connectCount++;
                System.out.print(connectCount);
                System.out.print(" 접속 port: " + port);
                System.out.println(" Client IP: " + ip);

                // handler 클래스로 client socket 전달
                ThreadServerHandler handler = new ThreadServerHandler(connectedClientSocket);
                handler.start();

            }
        } catch (IOException ioe) {
            System.err.println("예외 발생...");
        } finally {
            try {
                server.close();
            } catch (IOException ignored) { }
        }
    }
}
