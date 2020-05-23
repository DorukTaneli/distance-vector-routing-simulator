import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class ModivSim {

    // Holds the nodes in the topology
    private static List<Node> nodeList = new ArrayList<Node>();

    public static void main(String[] args) {

        createTopology();



    }

    /**
     * <p>
     *     Prompts the user to enter the file name containing the node information. Creates a node object and adds the node to the nodeList.
     *     Process stops when the user enters "done" as input.
     * </p>
     */
    private static void createTopology() {
        Hashtable<Integer, Integer> linkCost;
        Hashtable<Integer, Integer> linkBandwidth;
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the file name to create the router node:");
        String user_input = null;
        try {
            user_input = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(!user_input.equalsIgnoreCase("done")){
            linkCost = new Hashtable<>();
            linkBandwidth = new Hashtable<>();
            try {
                File nodeFile = new File(user_input);
                Scanner myReader = new Scanner(nodeFile);
                String nodeInfo = myReader.nextLine();
                String formatter[] = nodeInfo.split(",", 2);
                int nodeID = Integer.parseInt(formatter[0]);
                formatter = formatter[1].split("\\(", -1);
                //Create the linkCost and linkBandwidth maps based on the info read from the file
                for(String info : formatter){
                    if(!info.equals("")){
                        info = info.replace(")", "");
                        String formatted_info[] = info.split(",", -1);
                        int neighborID = Integer.parseInt(formatted_info[0]);
                        int cost = Integer.parseInt(formatted_info[1]);
                        int bandwidth = Integer.parseInt(formatted_info[2]);
                        linkCost.put(neighborID, cost);
                        linkBandwidth.put(neighborID, bandwidth);
                    }
                }
                Node node = new Node(nodeID, linkCost, linkBandwidth);
                nodeList.add(nodeID, node);
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
