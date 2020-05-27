
public class Flow {

	private String name;
	private Node start;
	private Node end;
	private int flowSize;
	
	public Flow(String name, Node start, Node end, int flowSize) {
		this.name = name;
		this.start = start;
		this.end = end;
		this.flowSize = flowSize;
	}

	public String getName() {
		return name;
	}

	public Node getStart() {
		return start;
	}

	public Node getEnd() {
		return end;
	}

	public int getFlowSize() {
		return flowSize;
	}

	@Override
	public String toString() {
		return "Flow [name=" + name + ", startNode=" + start.getNodeID() + 
				", endNode=" + end.getNodeID() + ", flowSize=" + flowSize + "]";
	}
	
	
}
