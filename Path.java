import greenfoot.GreenfootImage;
import greenfoot.World;

public class Path extends MapElements {
    private GreenfootImage low;
    private GreenfootImage high;
    private GreenfootImage origLow = new GreenfootImage("blueDot.png");
    private GreenfootImage origHigh = new GreenfootImage("blueDot2.png");
    private int xCoord;
    private int yCoord;
    private int pathRegulator;
    private int xOffset;
    private int yOffset;
    private int size;
    private double zoom;
    private boolean toggle = true;
    
    public Path(int delay, int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        pathRegulator = delay*20; // The time between flashes
    }
    
    protected void addedToWorld(World world) {
        updateLocation(); // Set location onece added to the world
    }
    
    public void act() {
        updateLocation(); // Stick path object to map by setting location based on map offset
        if (toggle) { // Set image based on whether flashing image or normal
            setImage(low);
            pathRegulator = (pathRegulator+1)%(20*10); // Update pathRegulator based on timing
        } else {
            setImage(high);
            pathRegulator = (pathRegulator+1)%(20);
        }
        if (pathRegulator == 0) { // Swap image when pathRegulator is at 0
           toggle = !toggle;
           pathRegulator += 20;
        }        
    }
    
    public void updateLocation() {
        Map map = ((Directions)getWorld()).map;
        if (zoom != map.getZoom() || xOffset != map.getXOffset()-1100 || yOffset != map.getYOffset()-629) { // If something has changed in the map
            zoom = map.getZoom(); // Get data from map object and set size and location based on it
            size = (int)(20*zoom);
            low = new GreenfootImage(origLow);
            low.scale(size,size);
            high = new GreenfootImage(origHigh);
            high.scale(size,size);
            xOffset = map.getXOffset()-1100;
            yOffset = map.getYOffset()-629;
            setLocation((int)((xCoord+xOffset-wid)*zoom)+wid, (int)((yCoord+yOffset-hei)*zoom)+hei);
        }
    }
}
