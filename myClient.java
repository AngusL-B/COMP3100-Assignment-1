import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class myClient {
    private Socket socket;
    private BufferedReader in;
    private DataOutputStream out;

    // Constructor for myClient
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

    // Sends and recieves a message one after the other
    public String sr(String message) throws IOException {
        send(message);
        return recieve();
    }

    // Closes the connection
    public void quit() throws IOException {
        sr("QUIT");
        in.close();
        out.close();
    }

    // Reads server information and saves the largest servers to an ArrayList
    public ArrayList<String[]> saveLargestServers() throws IOException {
        ArrayList<String[]> largestServers = new ArrayList<>();
        int mostCores = 0;

        String[] servers = sr("GETS All").split(" ");

        String[][] serverInfo = new String[Integer.valueOf(servers[1])][];
        send("OK");

        // Reads all of the server information and saves the Largest Servers to an ArrayList
        for (int i = 0; i < Integer.valueOf(servers[1]); i++) {

            serverInfo[i] = recieve().split(" ");

            // Add to list if same as largest server
            if (Integer.valueOf(serverInfo[i][4]) == mostCores) {
                largestServers.add(serverInfo[i]);
            }

            // Create new list if bigger than largest server
            if (Integer.valueOf(serverInfo[i][4]) > mostCores) {
                mostCores = Integer.valueOf(serverInfo[i][4]);
                largestServers = new ArrayList<>();
                largestServers.add(serverInfo[i]);
            }
        }
        sr("OK");

        return largestServers;
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        myClient client = new myClient();

        client.sr("HELO");
        client.sr("AUTH client");

        String[] job = client.sr("REDY").split(" ");

        ArrayList<String[]> largestServers = client.saveLargestServers();

        client.quit();
    }
}
