import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ModivSim {
    // Holds the nodes in the topology
    private static List<Node> nodeList = new ArrayList<Node>();

    public static void main(String[] args) {
        createTopology();
        setNeighbors();
        for (Node node: nodeList) {
            System.out.println(node.getNodeID());
            node.getNodeGUI().println("dst  |   0   1   2   3   4");
            for (int[] table: node.getDistanceTable()) {
                node.getNodeGUI().print("router  |");
                for (int distance: table) {
                    node.getNodeGUI().print("  " + distance);
                }
                node.getNodeGUI().print("\n");
            }
        }
        for (Node node: nodeList){
            node.printInfo();
        }
        sendDistanceVectors();
        for (Node node: nodeList){
            node.getNodeGUI().println("dst  |   0   1   2   3   4");
            for (int[] table: node.getDistanceTable()) {
                node.getNodeGUI().print("router  |");
                for (int distance: table) {
                    node.getNodeGUI().print("  " + distance);
                }
                node.getNodeGUI().print("\n");
            }
        }
        /*
        for (int i=0; i<10; i++) {
            distanceVectorRouting();
        }
        for (Node node: nodeList) {
            node.setConverged(true);
        }
        distanceVectorRouting();
        */
        
        System.out.println("Reading flows from flow.txt");
        FlowRouter fr = new FlowRouter(nodeList);
        fr.PrintFlows();
        System.out.println("Processing Flows");
        fr.StartFlows();
    }

    /**
     * <p>
     *     Prompts the user to enter the file name containing the node information. Creates a node object and adds the node to the nodeList.
     *     Process stops when the user enters "done" as input.
     * </p>
     */
    private static void createTopology() {
        int size = 5;
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
                        //System.out.println(formatted_info[2].trim() + "a");
                        int bandwidth = Integer.parseInt(formatted_info[2].trim());
                        linkCost.put(neighborID, cost);
                        linkBandwidth.put(neighborID, bandwidth);
                    }
                }
                Iterator<Map.Entry<Integer, Integer>> itr = linkCost.entrySet().iterator();
                Map.Entry<Integer, Integer> entry = null;
                while(itr.hasNext()){
                    entry = itr.next();
                    System.out.println(entry.getKey() + "->" + entry.getValue());
                }
                Node node = new Node(nodeID, linkCost, linkBandwidth, size);
                nodeList.add(nodeID, node);
                myReader.close();

            } catch (FileNotFoundException e) {
                System.out.println("File name doesn't exist.");
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

    /**
     * <p>
     *     Fills the neighbors Hashtable in each node object with a reference to the node objects stored in the nodeList.
     * </p>
     */
    private static void setNeighbors(){
        Hashtable <Integer, Node> neighbors;
        for (Node node: nodeList){
            neighbors = new Hashtable<Integer, Node>();
            for(Map.Entry<Integer, Integer> entry : node.getLinkBandwidth().entrySet()) {
                int neighborID = entry.getKey();
                neighbors.put(neighborID, nodeList.get(neighborID));
            }
            node.setNeighbors(neighbors);
        }
    }

    private static void sendDistanceVectors() {
        for (Node node: nodeList){
            node.sendUpdate();
        }
    }

    private static void distanceVectorRouting() throws IOException, InterruptedException {
        BufferedReader input =
                new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter the period of time (s) for DVR algorithm to work: ");
        String p = new String();
        p = input.readLine();
        while(true) {
            int convergence = 0;
            for (Node node: nodeList){
                node.sendUpdate();
                //for (int[] distanceTable: node.getDistanceTable()) {
                //   System.out.println("--- a dt ---");
                //   for (int distance: distanceTable) {
                //       System.out.println(distance);
                //   }
                //}
                if (node.isConverged()) {
                    convergence++;
                }
            }
            if (convergence == nodeList.size()) {
                System.out.println("END");
                break;
            }
            TimeUnit.SECONDS.sleep(Long.parseLong(p));
        }
        for (Node node: nodeList){
            node.printInfo();
        }

    }
}
