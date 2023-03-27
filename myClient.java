import java.net.Socket;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class myClient {
    private Socket socket;
    private BufferedReader in;
    private DataOutputStream out;

    public myClient() throws UnknownHostException, IOException {
        socket = new Socket("127.0.0.1", 50000);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new DataOutputStream(socket.getOutputStream());
    }

    public void send(String message) throws IOException {
        out.write((message + "\n").getBytes());
        out.flush();
    }

    public String recieve() throws IOException {
        String temp = in.readLine();
        System.out.println(temp);
        return temp;
    }

    public void quit() throws IOException {
        send("QUIT");
        recieve();
        in.close();
        out.close();
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        myClient client = new myClient();

        client.send("HELO");
        client.recieve();
        client.send("AUTH client");
        client.recieve();
        client.send("REDY");
        client.recieve();
        client.quit();
    }
}
