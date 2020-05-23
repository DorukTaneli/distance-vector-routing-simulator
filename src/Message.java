public class Message {

    private int senderID;
    private int receiverID;
    private int linkBandwith;
    private int[] distanceVector; //distance[i] gives the cost to the ith router

    public Message(int senderID, int receiverID, int linkBandwith, int[] distanceVector) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.linkBandwith = linkBandwith;
        this.distanceVector = distanceVector;
    }
}
