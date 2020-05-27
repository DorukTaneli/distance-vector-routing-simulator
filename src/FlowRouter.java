import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.FileNotFoundException;

public class FlowRouter {
	
	private ArrayList<Flow> flows = new ArrayList<Flow>();
	private ArrayList<Flow> queue = new ArrayList<Flow>();
	private List<Node> nodeList;
	
	private int t = 0;
	private int period = 2;
	
	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private Runnable decreaseInUseTimesAndProcessFlowIteration = new Runnable() {
	    public void run() {
	        for (Node n: nodeList) {
	        	if (n.getInUse() > 0) {
		        	n.setInUse(n.getInUse() - period);
	        	}
	        }
	        processFlows();
            if (flows.isEmpty()) {
                executor.shutdown();
            }
	    }
	};
	
	public void StartFlows() {
		executor.scheduleAtFixedRate(decreaseInUseTimesAndProcessFlowIteration, 0, period, TimeUnit.SECONDS);
	}

	public ArrayList<Flow> getFlows() {
		return flows;
	}
	
	//Constructor:
	//get nodeList and read flows from flow.txt
	public FlowRouter(List<Node> nodeList) {
		this.nodeList = nodeList;
		try {
	      File myObj = new File("flow.txt");
	      Scanner myReader = new Scanner(myObj);
	      while (myReader.hasNextLine()) {
	        String data = myReader.nextLine();
	        
	        String name = data.substring(0,1);
	        Node start = getNodeWithID(Integer.parseInt(data.substring(3, 4)));
	        Node end = getNodeWithID(Integer.parseInt(data.substring(6, 7)));
	        int flowSize = Integer.parseInt(data.substring(9));
	        
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

	
	public void processFlows() {
		System.out.println("\nt=" + t);
		t += period;
		queue.clear();
		
		for (Flow f : flows) {
			System.out.println(f.toString());
			
			List<Integer> bottleneckTable = f.getStart().getBottleNeckBandwidthTable();
			
/*uncomment when part2 is finished
			int bottleneckBandwidth = bottleneckTable.get(f.getEnd().getNodeID());
			System.out.println(bottleneckBandwidth);
*/
			
			Node currentNode = f.getStart();
			String target = Integer.toString(f.getEnd().getNodeID());
			
			String route = "[" + currentNode.getNodeID();
			
			List<Node> passedNodes = new ArrayList<Node>();
			
			while (currentNode != f.getEnd()) {
				Hashtable<String, String> fwTable = currentNode.getForwardingTable();
				Node choice1 = getNodeWithID(Integer.parseInt(fwTable.get(target).substring(1, 2)));
				Node choice2 = getNodeWithID(Integer.parseInt(fwTable.get(target).substring(3, 4)));
				
				if (choice1.getInUse() <= 0) {
					passedNodes.add(choice1);
					route = route.concat("-" + choice1.getNodeID());
					currentNode = choice1;
				} else if (choice2.getInUse() <= 0) {
					passedNodes.add(choice2);
					route = route.concat("-" + choice2.getNodeID());
					currentNode = choice2;
				} else {
					queue.add(f);
					break;
				}
			}
			
			route = route.concat("]");
			
			if (!queue.contains(f)) {
/*uncomment when part2 is finished
				for (Node n: passedNodes) {
					n.setInUse(f.getFlowSize()/bottleneckBandwidth);
				}
				f.setFlowSize(f.getFlowSize() - period*bottleneckBandwidth);
*/
				if (f.getFlowSize() <= 0) {
					flows.remove(f);
				}
				System.out.println("Route: " + route + "\tBandwidth: ");
/*uncomment when part2 is finished
				System.out.print(bottleneckBandwidth);
*/
			} else {
				System.out.println("in queue...");
			}
		}
	}
	
	private Node getNodeWithID(int id) {
		for (Node node: nodeList){
        	if (node.getNodeID() == id) {
        		return node;
        	}
		}
		return null;
	}

}
