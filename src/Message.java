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

    public int getSenderID() {
        return senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public int getLinkBandwith() {
        return linkBandwith;
    }

    public int[] getDistanceVector() {
        return distanceVector;
    }
}
