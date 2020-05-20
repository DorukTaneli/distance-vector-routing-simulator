import java.io.*;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class ModivSim {

    private List<Node> nodeList;

    public static void main(String[] args) {

        // Read the topology from file. Each line in the text represents a node. It assumes that all the nodes are contained in the single file
        Hashtable<Integer, Integer> linkCost;
        Hashtable<Integer, Integer> linkBandwidth;
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the file name to create the router:");
        String user_input = null;
        try {
            user_input = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(!user_input.equalsIgnoreCase("done")){
            System.out.println(user_input);
            try {
                File nodeFile = new File(user_input);
                Scanner myReader = new Scanner(nodeFile);
                String nodeInfo = myReader.nextLine();
                System.out.println(nodeInfo);
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            System.out.println("Node created. Enter another file name or type 'done':");
            try {
                user_input = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Topology created.");


    }


}
