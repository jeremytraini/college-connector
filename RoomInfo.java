public class RoomInfo { // This is a records class for reading and storing the id, name and department name in the room search
    private int nodeID;
    private String nodeName;
    private String departmentName;
    
    public RoomInfo(int nodeID, String nodeName, String departmentName) {
        this.nodeID = nodeID;
        this.nodeName = nodeName;
        this.departmentName = departmentName;
    }

    public int getNodeID() {
        return nodeID;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getDepartmentName() {
        return departmentName;
    }
}
