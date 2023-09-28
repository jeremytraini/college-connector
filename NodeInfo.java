public class NodeInfo { // This is a records class for storing different vital information for nodes on the map
    private int xCoordinate;
    private int yCoordinate;
    private int bestParent;
    private int gCost;
    private int fCost;
    private int[][] connections;
    
    public NodeInfo(int xCoordinate, int yCoordinate, int bestParent, int gCost, int fCost, int[][] connections) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.bestParent = bestParent;
        this.gCost = gCost;
        this.fCost = fCost;
        this.connections = connections;
    }
    
    public int getX() {
        return xCoordinate;
    }
    
    public void setX(int x) {
        xCoordinate = x;
    }

    public int getY() {
        return yCoordinate;
    }
    
    public void setY(int y) {
        yCoordinate = y;
    }
    
    public int bestParent() {
        return bestParent;
    }
    
    public void bestParent(int parent) {
        bestParent = parent;
    }

    public int gCost() {
        return gCost;
    }
    
    public void gCost(int cost) {
        gCost = cost;
    }
    
    public int fCost() {
        return fCost;
    }
    
    public void fCost(int cost) {
        fCost = cost;
    }
    
    public int[][] connections() {
        return connections;
    }
    
    public void connections(int[][] arr) {
        connections = arr;
    }
}
