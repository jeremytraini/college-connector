import greenfoot.*;
import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;

public class Map extends MapElements {
    private boolean developer = false; // Used to identify location of nodes
    private boolean mouseDown = false;
    private int xOffset = 648;
    private int yOffset = 362;
    private int lineColor;
    private int start = 52;
    private int end;
    private int active = -1;
    private int scaleX;
    private int scaleY;
    private double mouseX = 0;
    private double mouseY = 0;
    private double zoom = 0.66;
    private double maxZoom = 2.26;
    private double minZoom = 0.47;
    private double resolution = 0.5;
    private ArrayList<NodeInfo> nodeInfo = readFromFile2();
    private GreenfootImage map = new GreenfootImage(5,5);
    private GreenfootImage updatedImage;
    private GreenfootImage newImg = new GreenfootImage("mapHigh.png");
    
    public ArrayList readFromFile2() { // Read both nodeinfo.txt and nodeconnections.txt files and parse data to populate nodeInfo array
        ArrayList<NodeInfo> nodeInfo = new ArrayList<>();
        try {
            InputStreamReader inputStreamNodes = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("roomData/nodeinfo.txt")); // Creates input stream from file as a stream
            InputStreamReader inputStreamConnections = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("roomData/nodeconnections.txt"));
            BufferedReader readFileNodes = new BufferedReader(inputStreamNodes); // Read text from character-input stream
            BufferedReader readFileConnections = new BufferedReader(inputStreamConnections);
            for (int a = 0; a < 354; a++) {
                String[] coordinates = readFileNodes.readLine().split(","); // Split coordinaes with comma
                String[] connections = readFileConnections.readLine().split(","); // Split connection nodes + their weighting with comma
                int[][] nodeConnections = new int[connections.length][2];
                for (int i = 0; i < connections.length; i++) { // Create nodeConnections array by splitting with ampersand symbol
                    if (connections[i].length() != 0) {
                        String[] connects = connections[i].split("&");
                        nodeConnections[i][0] = Integer.parseInt(connects[0]);
                        nodeConnections[i][1] = Integer.parseInt(connects[1]);
                    } else {
                        nodeConnections = new int[0][2];
                    }
                }
                nodeInfo.add(new NodeInfo(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]), 0, 0, 0, nodeConnections)); // Save parsed data to nodeInfo array
            }
        } catch(IOException e) {
            System.out.println("File read error" + e);
        }
        return nodeInfo;
    }
    
    public ArrayList readFromFile() { // Read both nodeinfo.txt and nodeconnections.txt files and parse data to populate nodeInfo array
        ArrayList<NodeInfo> nodeInfo = new ArrayList<>();
        Scanner dataReader = null;
        Scanner dataReader2 = null;
        try {
            File readFileNodes = new File("roomData/nodeinfo.txt");
            File readFileConnections = new File("roomData/nodeconnections.txt");
            dataReader = new Scanner(readFileNodes);
            dataReader2 = new Scanner(readFileConnections);
            while (dataReader.hasNextLine() && dataReader2.hasNextLine()) {
                String[] coordinates = dataReader.nextLine().split(","); // Split coordinaes with comma
                String[] connections = dataReader2.nextLine().split(","); // Split connection nodes + their weighting with comma
                int[][] nodeConnections = new int[connections.length][2];
                for (int i = 0; i < connections.length; i++) { // Create nodeConnections array by splitting with ampersand symbol
                    if (connections[i].length() != 0) {
                        String[] connects = connections[i].split("&");
                        nodeConnections[i][0] = Integer.parseInt(connects[0]);
                        nodeConnections[i][1] = Integer.parseInt(connects[1]);
                    } else {
                        nodeConnections = new int[0][2];
                    }
                }
                nodeInfo.add(new NodeInfo(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]), 0, 0, 0, nodeConnections)); // Save parsed data to nodeInfo array
            }
            dataReader.close();
        } catch(IOException e) {
            System.out.println("File read error" + e);
        }
        return nodeInfo;
    }
    
    public void saveToFile() {
        try { // Save node coordinates from nodeInfo array as CSV in nodeinfo text file
            BufferedWriter outputWriter = null;
            outputWriter = new BufferedWriter(new FileWriter("roomData/nodeinfo.txt"));
            outputWriter.write(nodeInfo.get(0).getX()+","+nodeInfo.get(0).getY());
            for (int i = 1; i < nodeInfo.size(); i++) {
                outputWriter.newLine();
                outputWriter.write(nodeInfo.get(i).getX()+","+nodeInfo.get(i).getY()); // Save each nodes coordinates onto new line
            }
            outputWriter.flush();  
            outputWriter.close();
            // Save connections (with weighting) from nodeInfo array as (modified) CSV in nodeconnections text file
            outputWriter = null;
            outputWriter = new BufferedWriter(new FileWriter("roomData/nodeconnections.txt"));
            String connectionString;
            for (int i = 0; i < nodeInfo.size(); i++) { // Handles saving the connections along with their associated weighting
                connectionString = "";
                for (int a = 0; a < nodeInfo.get(i).connections().length; a++) {
                    connectionString += Integer.toString(nodeInfo.get(i).connections()[a][0])+"&"+Integer.toString(nodeInfo.get(i).connections()[a][1])+",";
                }
                outputWriter.write(connectionString);
                outputWriter.newLine();
            }
            outputWriter.flush();  
            outputWriter.close();
        } catch(IOException e) {
            System.out.println("File save error" + e);
        }
    }
    
    public void act() {
        if(Greenfoot.getKey() != null) {
            if (Greenfoot.isKeyDown("q")) { // Zoom function when "q" or "w" key pressed
                if (zoom < maxZoom) {
                    zoom *= 1.06;
                    setMap(false);
                }
            }
            if (Greenfoot.isKeyDown("w")) {
                if (zoom > minZoom) {
                    zoom *= 0.94;
                    setMap(false);
                }
            }
            if (Greenfoot.isKeyDown("up") && yOffset < 884) { // Arrow keys move the map around by changing offsets
                yOffset += 3;
            }
            if (Greenfoot.isKeyDown("down") && yOffset > -100) {
                yOffset -= 3;
            }
            if (Greenfoot.isKeyDown("left") && xOffset < 1100) {
                xOffset += 3;
            }
            if (Greenfoot.isKeyDown("right") && xOffset > -531) {
                xOffset -= 3;
            }
            if (Greenfoot.isKeyDown("up") || Greenfoot.isKeyDown("down") || Greenfoot.isKeyDown("left") || Greenfoot.isKeyDown("right")) {
                setLocation(zoomedX(0), zoomedY(0)); // Set location to new offset when arrow key is pressed
            }
            if (developer && Greenfoot.mouseClicked(this) && Greenfoot.isKeyDown("b")) {
                MouseInfo mouse = Greenfoot.getMouseInfo(); // Add new node at the mouse position when in developer mode and "b" key is pressed
                addNode((int)((mouse.getX()-(wid))/zoom)+(wid)-xOffset+1100,(int)((mouse.getY()-hei)/zoom)+hei-yOffset+630);
            }
            if (Greenfoot.mouseClicked(this) && Greenfoot.isKeyDown("v")) { // When "v" pressed and mouse clicked, save nodes and connections to file
                saveToFile();
            }
        }
        
        if (mouseDown && (Greenfoot.mouseDragEnded(null) || Greenfoot.mouseClicked(null))) mouseDown = false;
        
        if (!mouseDown && Greenfoot.mousePressed(this)) {
            mouseDown = true;
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (mouse != null) {
                mouseX = (mouse.getX()/zoom-xOffset); // When dragging across the map, store original mouse position
                mouseY = (mouse.getY()/zoom-yOffset);
            }
        }
        if (mouseDown) {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (mouse != null) {
                xOffset = (int)((mouse.getX()-(mouseX*zoom))/zoom); // Calculate new offset
                yOffset = (int)((mouse.getY()-(mouseY*zoom))/zoom);
                if (yOffset >= 884) { // Bounds for moving the map too far
                    yOffset = 884;
                } else if (yOffset <= -100) {
                    yOffset = -100;
                }
                if (xOffset >= 1100) {
                    xOffset = 1100;
                } else if (xOffset <= -531) {
                    xOffset = -531;
                }
            }
            setLocation(zoomedX(0), zoomedY(0));
        }
    }
    
    protected void addedToWorld(World world) {
        //long startTime = System.nanoTime(); // This is used for conducting benchmarking tests
        
        end = ((Directions)getWorld()).getEnd();
        setMap(true); // Update map when added to world
        if (developer) { // Add Node object for each node if in developer mode
            for(int i=0; i < nodeInfo.size(); i++){
                getWorld().addObject(new Node(i), zoomedX(nodeInfo.get(i).getX()),zoomedY(nodeInfo.get(i).getY()));
            }
        }
        
        //long endTime = System.nanoTime();
        //System.out.println((endTime - startTime)/1e+9);
    }
    
    public void addNode(int x, int y) { // Adding node to nodeInfo array + adding Node object for it
        nodeInfo.add(new NodeInfo(x,y,0,0,0,new int[][]{}));
        getWorld().addObject(new Node(nodeInfo.size()-1), zoomedX(x), zoomedY(y));
    }
    
    public void setMap(boolean changed) { // Update and display map when a change has been made
        if (changed) { // If end node has been changed
            newImg = new GreenfootImage("mapHigh.png");
            if (developer) {
                newImg.drawImage(newGraph(newImg.getWidth(), newImg.getHeight()),0,0);
            } else {
                newPath(); // Calculate shortest path
            }
        }
        updatedImage = new GreenfootImage(newImg);
        updatedImage.scale((int)(newImg.getWidth()*zoom), (int)(newImg.getHeight()*zoom)); // Scale map based on zoom
        setImage(updatedImage);
        setLocation(zoomedX(0), zoomedY(0)); // Move to new location
    }
    
    public void newPath() { // Show new path on map, adjust map zoom and offset and show destination information onscreen
        ArrayList<Integer> path = shortestPath(start, end); // Get the shortest path from start node to end node
        if (path != null && ((Directions)getWorld()) != null) { // If there is a valid path and on the correct screen
            getWorld().removeObjects(getWorld().getObjects(Path.class)); // Remove all path objects onscreen
            int prevI = 0;
            int leftmost = nodeInfo.get(path.get(0)).getX(); // Reset coordinates for leftmost, rightmost, topmost and bottommost nodes to first node
            int rightmost = nodeInfo.get(path.get(0)).getX();
            int topmost = nodeInfo.get(path.get(0)).getY();
            int bottommost = nodeInfo.get(path.get(0)).getY();
            for(int a = 0; a < path.size()-1; a++) {
                // Create new dotted path segment between node a and node a + 1
                prevI = ((Directions)getWorld()).newPath(nodeInfo.get(path.get(a)).getX(),nodeInfo.get(path.get(a)).getY(), nodeInfo.get(path.get(a+1)).getX(), nodeInfo.get(path.get(a+1)).getY(), prevI);
                int currentX = nodeInfo.get(path.get(a)).getX(); // Get coordinates of each node in path
                int currentY = nodeInfo.get(path.get(a)).getY();
                if (currentX > leftmost) { // Replace leftmost X coordinate with the current node's X if it is smaller
                    leftmost = currentX;
                } else if (currentX < rightmost) { // Do the same for the righmost if larger
                    rightmost = currentX;
                }
                if (currentY > topmost) { // Similarly, replace topmost coordinate with the current Y coordinate if smaller
                    topmost = currentY;
                } else if (currentY < bottommost) {
                    bottommost = currentY;
                }
            }
            int centreX = (leftmost + rightmost)/2; // Calculate the centre of the path by averaging leftmost and rightmost coordinates
            int centreY = (bottommost + topmost)/2;
            ((Directions)getWorld()).addIndicator(nodeInfo.get(path.get(path.size()-1)).getX(),nodeInfo.get(path.get(path.size()-1)).getY(), nodeInfo.get(path.get(path.size()-2)).getX(),nodeInfo.get(path.get(path.size()-2)).getY());
            double bigW = (double)wid/(double)(leftmost - rightmost); // Calculate zoom by finding width to largest distance ratio
            double bigH = (double)hei/(double)(topmost - bottommost);
            if (bigW < bigH) { // Set the zoom to half the smallest total width to largest distance ratio
                zoom = bigW;
            } else {
                zoom = bigH;
            }
            if (zoom > maxZoom) { // Fix zoom value if too large or small
                zoom = maxZoom;
            } else if (zoom < minZoom) {
                zoom = minZoom;
            }
            xOffset = wid*2-centreX+700; // Centre map on the path by calculating the new map offsets
            yOffset = hei*2-centreY+350;
        }
        int walkTime = nodeInfo.get(end).fCost()/250; // Calculate walk time by dividing actual distance of path in pixels by pixels per minute ratio
        //System.out.println(Arrays.toString(results.toArray()));
        //System.out.println(walkTime);
        getWorld().addObject(new Information(1200, 700, ((Directions)getWorld()).getNodeName(), walkTime, end), 1200*5/6, 700/2); // Add information object to world
    }
    
    public GreenfootImage newGraph(int width, int height) { // This is run in developer mode only and performs similar to the code above
        GreenfootImage paths;
        paths = new GreenfootImage((int)((width)*resolution), (int)((height)*resolution));
        for(int i = 0; i < nodeInfo.size(); i++){
            for(int a = 0; a < nodeInfo.get(i).connections().length; a++){
                if (i > a) { // This shows all connections between the nodes for developer use
                    thickLine(false, nodeInfo.get(i).connections()[a][1], paths, 5, nodeInfo.get(i).getX(), nodeInfo.get(i).getY(), nodeInfo.get(nodeInfo.get(i).connections()[a][0]).getX(), nodeInfo.get(nodeInfo.get(i).connections()[a][0]).getY());
                }
            }
        }
        ArrayList<Integer> path = shortestPath(start, end);
        if (path != null) {
            getWorld().removeObjects(getWorld().getObjects(Path.class));
            int totalX = 0;
            int totalY = 0;
            for(int a=0; a < path.size()-1; a++){
                thickLine(true, 0, paths, 10, nodeInfo.get(path.get(a)).getX(),nodeInfo.get(path.get(a)).getY(), nodeInfo.get(path.get(a+1)).getX(), nodeInfo.get(path.get(a+1)).getY());
                totalX += nodeInfo.get(path.get(a)).getX();
                totalY += nodeInfo.get(path.get(a)).getY();
            }
            totalX += nodeInfo.get(path.get(path.size()-1)).getX();
            totalY += nodeInfo.get(path.get(path.size()-1)).getY();
            int centreX = totalX/path.size();
            int centreY = totalY/path.size();
            int largestXDist = 0;
            int largestYDist = 0;
            for(int a=0; a < path.size(); a++){
                int Xdist = nodeInfo.get(path.get(a)).getX() - centreX;
                if (Xdist > largestXDist) largestXDist = Xdist;
                int Ydist = nodeInfo.get(path.get(a)).getY() - centreY;
                if (Ydist > largestYDist) largestYDist = Ydist;
            }
        }
        paths.scale(width, height);
        getWorld().addObject(new Information(1200, 700, "DEVELOPER MODE", 444, end), 1200*5/6, 700/2);
        return paths;
    }
    
    public int zoomedX(int x) {
        return (int)((x+xOffset-(wid))*zoom)+(wid);
    }
    
    public int zoomedY(int y) {
        return (int)((y+yOffset-hei)*zoom)+hei;
    }
    
    public int scaled(int coord) {
        return (int)(coord*resolution);
    }
    
    public int getNodeX(int id) {
        return nodeInfo.get(id).getX();
    }
    
    public int getNodeY(int id) {
        return nodeInfo.get(id).getY();
    }
    
    public int undoZoomedX(int x) {
        return (int)(((x-wid)/zoom)-xOffset+wid);
    }
    
    public int undoZoomedY(int y) {
        return (int)(((y-hei)/zoom)-yOffset+hei);
    }
    
    public void thickLine(boolean solution, int hardness, GreenfootImage original, int thickness, int x1, int y1, int x2, int y2) {
        x1 = scaled(x1); // Fix coordiate in proportion to resolution variable
        x2 = scaled(x2);
        y1 = scaled(y1);
        y2 = scaled(y2);
        thickness = (int)(thickness*(resolution*2)); // Set thickness based on resolution
        double distance = Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2)); // Calculate distance between nodes
        int xS = (int)((thickness * (y2 - y1) / distance) / 2);
        int yS = (int)((thickness * (x2 - x1) / distance) / 2);
        int[] xs = {x1 - xS, x1 + xS, x2 + xS, x2 - xS}; // Set rectangle parameters to make thick rectangle
        int[] ys = {y1 + yS, y1 - yS, y2 - yS, y2 + yS};
        Color colour = new Color((255 * hardness/2) / 100, (255 * (100 - hardness/2)) / 100, 0, 100); // Set colour based on path hardness (ranges from red to green)
        original.setColor(colour);
        original.fillPolygon(xs, ys, 4);
        if (solution) { // If the line is the shortest path, add circles to the ends (makes the line look better)
            original.fillOval(x1-thickness/2,y1-thickness/2,thickness,thickness);
            original.fillOval(x2-thickness/2,y2-thickness/2,thickness,thickness);
        }
    }
    
    public ArrayList shortestPath(int start, int end) {
        ArrayList<Integer> OPEN = new ArrayList();
        ArrayList<Integer> CLOSED = new ArrayList();
        OPEN.add(start);
        nodeInfo.get(start).bestParent(0); // Reset start node data
        nodeInfo.get(start).gCost(0);
        nodeInfo.get(start).fCost(dist(start,end)); // Set initial F-cost to entire distance from beginnning to end
        int current;
        int bestNode;
        while (OPEN.size() != 0) { // While the open array is populated
            bestNode = bestF(OPEN); // Get node with best F-cost in open array
            current = OPEN.get(bestNode);
            OPEN.remove(bestNode);
            CLOSED.add(current);
            if (current == end) { // When end node has been reached and is the best node, finalise search
                //System.out.println(Arrays.toString(trackPath(current, start).toArray()));
                return trackPath(current, start);
            }
            for (int i = 0; i < nodeInfo.get(current).connections().length; i++) { // Search each neighbour node of current node
                int neighbour = nodeInfo.get(current).connections()[i][0];
                int weight = nodeInfo.get(current).connections()[i][1];
                if (!CLOSED.contains(neighbour)) {
                    int gCost = nodeInfo.get(current).gCost() + dist(neighbour,current)*(weight/100); // Calculate G-cost for neighbour node
                    if (OPEN.contains(neighbour) && gCost < nodeInfo.get(neighbour).gCost()) {
                        for (int z = 0; z < OPEN.size(); z++) {  // Remove neighbour from open list because new path is better
                            if ((int)OPEN.get(z) == neighbour) {
                                OPEN.remove(z);
                                z = OPEN.size();
                            }
                        }
                    }
                    if (!OPEN.contains(neighbour)) { // Add neighbour to open array as it must be searched
                        OPEN.add(neighbour);
                        nodeInfo.get(neighbour).bestParent(current);
                        nodeInfo.get(neighbour).gCost(gCost);
                        nodeInfo.get(neighbour).fCost(gCost + dist(neighbour,end)); // F-cost
                    }
                }
            }
        }
        return null; // If no valid path found, return null
    }
    
    public int dist(int node1, int node2) { // Calculate linear distance between two nodes
        return (int)Math.sqrt(Math.pow(nodeInfo.get(node1).getX() - nodeInfo.get(node2).getX(), 2) + Math.pow(nodeInfo.get(node1).getY() - nodeInfo.get(node2).getY(), 2));
    }
    
    public int bestF(ArrayList list) { // Find array index with best (lowest) F-score
        int besti = 0;
        int best = nodeInfo.get((int)list.get(0)).fCost(); // Set best node to first one
        for (int i = 1; i < list.size(); i++) {
            if (nodeInfo.get((int)list.get(i)).fCost() <= best) {
                besti = i; // If better node (lower F-cost) is found, set best index to it
                best = nodeInfo.get((int)list.get(i)).fCost();
            }
        }
        return besti; // Return best array index found
    }
    
    public ArrayList trackPath(int current, int start) {
        ArrayList path = new ArrayList();
        int i = current;
        while (i != start) { // Track back through best parents of each node and add to array
            path.add(i);
            i = nodeInfo.get(i).bestParent();
        }
        path.add(i);
        return path; // Returns array with best path found with shortestPath() function
    }
    
    public void changeNode(int id, int x, int y) {
        nodeInfo.get(id).setX(x);
        nodeInfo.get(id).setY(y);
    }
    
    public int[][] removeConnection(int node, int connections[][]) {
        int foundFlag = 0;
        if (connections.length != 0) {
            int connections1[][] = new int[connections.length-1][2]; // Create new array with one less index
            for (int i = 0; i < connections1.length; i++) {
                if (connections[i][0] == node) foundFlag = 1; // If node found in old array, skip it
                connections1[i] = connections[i+foundFlag]; // Copy old array across to new one
            }
            if (connections[connections.length-1][0] == node) foundFlag = 1;
            if (foundFlag != 0) {
                return connections1; // If a change was made, return the updated array
            }
        }
        return null; // If nothing was changed, return null
    }
    
    public void addConnection(int node1, int node2) { // Add connection to array from node1 to node2 and vice versa
        int[][] connections1 = removeConnection(node2, nodeInfo.get(node1).connections()); // Remove each node from eachothers connections just in case already there
        int[][] connections2 = removeConnection(node1, nodeInfo.get(node2).connections());
        if (connections1 == null && connections2 == null) { // If nothing was removed from both arrays, add connection
            connections1 = Arrays.copyOf(nodeInfo.get(node1).connections(), nodeInfo.get(node1).connections().length + 1); // Copy array to new one with an extra index
            connections2 = Arrays.copyOf(nodeInfo.get(node2).connections(), nodeInfo.get(node2).connections().length + 1);
            connections1[connections1.length-1] = new int[] {node2,100}; // Add each node to the last index of eachothers lengthened array
            connections2[connections2.length-1] = new int[] {node1,100};
        }
        nodeInfo.get(node1).connections(connections1);
        nodeInfo.get(node2).connections(connections2);
    }
    
    public int getActive() {
        return active;
    }
    
    public void setActive(int active) {
        this.active = active;
    }
    
    public double getZoom() {
        return zoom;
    }
    
    public void setZoom(boolean zoomIn) {
        if (zoomIn) { // True if zooming in
            if (zoom < maxZoom) { // Boundary condition for zooming in
                zoom *= 1.1; // Increment zoom by 1.1 times
                setMap(false); // Update the map to reflect change in zoom
            }
        } else { // Zoom out
            if (zoom > minZoom) {
                zoom *= 0.9;
                setMap(false);
            }
        }
    }
    
    public int getXOffset() {
        return xOffset;
    }
    
    public int getYOffset() {
        return yOffset;
    }
}
