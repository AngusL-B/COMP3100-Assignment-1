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
        // System.out.println(temp);
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
        socket.close();
    }

    // Reads server information and saves the largest servers to an ArrayList
    public ArrayList<String[]> saveLargestServers() throws IOException {
        ArrayList<String[]> largestServers = new ArrayList<>();
        int mostCores = 0;
        String sType = "";

        String[] servers = sr("GETS All").split(" ");

        String[][] serverInfo = new String[Integer.valueOf(servers[1])][];
        send("OK");

        // Reads all of the server information and saves the Largest Servers to an ArrayList
        for (int i = 0; i < Integer.valueOf(servers[1]); i++) {

            serverInfo[i] = recieve().split(" ");

            // Add to list if server type is same as largest server
            if (serverInfo[i][0].equals(sType) ) {
                largestServers.add(serverInfo[i]);
            }

            // Create new list if number of cores is bigger than largest server
            if (Integer.valueOf(serverInfo[i][4]) > mostCores) {
                mostCores = Integer.valueOf(serverInfo[i][4]);
                sType = serverInfo[i][0];
                largestServers = new ArrayList<>();
                largestServers.add(serverInfo[i]);
            }
        }
        sr("OK");

        return largestServers;
    }

    // Largest-Round-Robin Algorithm
    public void LRRAlgorithm(String[] job, ArrayList<String[]> largestServers) throws IOException {

        int serverNum = 0;

        while(!(job.length < 3)) {
            sr("SCHD " + job[2] + " " + largestServers.get(serverNum)[0] + " " + largestServers.get(serverNum)[1]);
            serverNum = (serverNum + 1) % largestServers.size();
            job = sr("REDY").split(" ");
            while (!(job[0].equals("JOBN") || job[0].equals("NONE"))) {
                job = sr("REDY").split(" ");
            }
        }
    }

    // Order of Events the client performs
    public static void main(String[] args) throws UnknownHostException, IOException {
        myClient client = new myClient();

        client.sr("HELO");

        client.sr("AUTH " + System.getProperty("user.name"));

        String[] job = client.sr("REDY").split(" ");

        ArrayList<String[]> largestServers = client.saveLargestServers();

        client.LRRAlgorithm(job, largestServers);

        client.quit();
    }
}