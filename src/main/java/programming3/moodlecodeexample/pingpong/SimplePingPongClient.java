package programming3.moodlecodeexample.pingpong;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class SimplePingPongClient {
    private String serverHost;
    private int serverPort;
    private BufferedReader reader;
    private BufferedWriter writer;

    public SimplePingPongClient(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void connect() {
        try (Socket socket = new Socket(serverHost, serverPort)) {
            pingPong(socket);
        } catch(UnknownHostException e) {
            System.err.println("Cannot reach the host");
        } catch(SocketTimeoutException e) {
            System.err.println("Connection closed due to timeout");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pingPong(Socket socket) throws IOException {
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Waiting for PING from " + socket);
        String line = this.reader.readLine();
        if (line != null && line.equals("PING")) {
            System.out.println("Got PING from " + socket + ", waiting 10s");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
            System.out.println("Sending PONG to " + socket);
            writer.write("PONG\n");
            writer.flush();
        }
    }

    public static void main(String[] args) {
        SimplePingPongClient client = new SimplePingPongClient("localhost", 1042);
        client.connect();
    }
}
