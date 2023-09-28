import greenfoot.Greenfoot;
import greenfoot.World;
import greenfoot.GreenfootImage;
import greenfoot.MouseInfo;
import greenfoot.Color;

public class Node extends MapElements { // Node display - Developer mode only
    private int id;
    private int xOffset;
    private int yOffset;
    private int active = -1; // Current node selected for connection adding
    private double zoom;
    private boolean mouseDown = false;
    private boolean already = true;
    private GreenfootImage image = new GreenfootImage(15,15);
    private Map map;

    public Node(int id) {
        this.id = id;
        image = new GreenfootImage(15,15);
        image.setColor(new Color(255,255,255,190));
        image.fillOval(-15,-15,30,30); // New node object is made for each node in droplet shape
        image.setColor(new Color(0,0,0,255));
        image.setFont(image.getFont().deriveFont(9f));
        image.drawString(Integer.toString(id), 0, 10);
        setImage(image);
    }
    
    protected void addedToWorld(World world) {
        updateLocation(); // Set location once added to the world
    }
    
    public int getId() {
        return id;
    }
    
    public void updateLocation() {
        Map map = ((Directions)getWorld()).map; // Get data from map and set location and size based on this
        zoom = map.getZoom();
        xOffset = map.getXOffset()-1100;
        yOffset = map.getYOffset()-629;
        setLocation((int)((map.getNodeX(id)+xOffset-wid)*zoom)+wid, (int)((map.getNodeY(id)+yOffset-hei)*zoom)+hei);
        if (mouseDown && (Greenfoot.mouseDragEnded(null) || Greenfoot.mouseClicked(null))) {
            mouseDown = false;
            ((Directions)getWorld()).map.setMap(true); // When dragging, update connection markings on the map
        }
        if (Greenfoot.mouseClicked(this) && Greenfoot.isKeyDown("n")) { // Create new connection when node clicked and "n" key pressed
            if (map.getActive() != -1 && map.getActive() != id) { // Second node clicked will intiate the add node connection function
                map.addConnection(id,map.getActive());
                map.setMap(true); // Update map with new connection present
                map.setActive(-1); // Reset active variable
            } else {
                map.setActive(id); // First node clicked is the active one
            }
        }
        if (!mouseDown && Greenfoot.mousePressed(this) && Greenfoot.isKeyDown("m")) {
            mouseDown = true; // When "m" key pressed, allow for node to be moved
        }
        if (mouseDown) {
            MouseInfo mouse = Greenfoot.getMouseInfo(); // When moving, set node location to mouse coordinates
            map.changeNode(id,(int)((mouse.getX()-wid)/zoom)+wid-xOffset,(int)((mouse.getY()-hei)/zoom)+hei-yOffset);
        }
    }
    
    public void act() {
        updateLocation();
    }    
}
