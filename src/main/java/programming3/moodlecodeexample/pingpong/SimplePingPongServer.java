package programming3.moodlecodeexample.pingpong;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SimplePingPongServer {
    private int port;
    private int timeout;
    private boolean isRunning = false;

    public SimplePingPongServer(int port, int timeout) {
        this.port = port;
        this.timeout = timeout;
    }

    public void start() {
        try(ServerSocket server = new ServerSocket(port)) {
            this.isRunning = true;
            while (this.isRunning) {
                acceptClient(server);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptClient(ServerSocket server) {
        try(Socket socket = server.accept()) {
            System.out.println("Got connection from " + socket);
            socket.setSoTimeout(timeout);
            handleClient(socket);
            System.out.println("Ending connection with " + socket);
        } catch(SocketTimeoutException e) {
            System.err.println("Socket timeout");
        } catch (IOException e) {
            System.err.println("Error with socket");
            e.printStackTrace();
        }
    }

    private void handleClient(Socket socket) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Sending PING to " + socket);
        writer.write("PING\n");
        writer.flush();
        System.out.println("Waiting for PONG from " + socket);
        String line = reader.readLine();
        System.out.println("Received " + line + " from " + socket);
    }

    public void stop() {
        this.isRunning = false;
    }

    public static void main(String[] args) {
        SimplePingPongServer server = new SimplePingPongServer(1042, 42000);
        server.start();
    }
}
