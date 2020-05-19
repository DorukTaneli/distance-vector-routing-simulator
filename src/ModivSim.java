import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class ModivSim {

    private List<Node> nodeList;

    public static void main(String[] args) {

        // Read the topology from file. Each line in the text represents a node. It assumes that all the nodes are contained in the single file
        Hashtable<Integer, Integer> linkCost;
        Hashtable<Integer, Integer> linkBandwidth;
        try {
            File nodeFile = new File("test.txt");
            Scanner myReader = new Scanner(nodeFile);
            while (myReader.hasNextLine()) {
                String nodeInfo = myReader.nextLine();
                System.out.println(nodeInfo);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


}
