import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class FlowRouter {
	
	private ArrayList<Flow> flows = new ArrayList<Flow>();
	
	public ArrayList<Flow> getFlows() {
		return flows;
	}
	
	public void GetFlowsFromFile(List<Node> nodeList) {
		try {
	      File myObj = new File("flow.txt");
	      Scanner myReader = new Scanner(myObj);
	      while (myReader.hasNextLine()) {
	        String data = myReader.nextLine();
	        
	        String name = data.substring(0,1);
	        Node start = null;
	        Node end = null;
	        int flowSize = Integer.parseInt(data.substring(9));
	        
	        for (Node node: nodeList){
	        	if (node.getNodeID() == Integer.parseInt(data.substring(3, 4))) {
	        		start = node;
	        	}
	        	if (node.getNodeID() == Integer.parseInt(data.substring(6, 7))) {
	        		end = node;
	        	}
	        }
	        
	        flows.add(new Flow(name, start, end, flowSize));
	      }
	      myReader.close();
	    } catch (FileNotFoundException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
	}
	
	public void PrintFlows() {
		for (Flow f : flows) {
			System.out.println(f.toString());
		}
	}
	
	public void startFlows() {
		for (Flow f : flows) {
			System.out.println("Starting " + f.toString());
			List<Integer> bottleneckTable = f.getStart().getBottleNeckBandwidthTable();
		}
	}

}
