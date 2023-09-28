import greenfoot.World;
import greenfoot.Greenfoot;

public class Directions extends World {        
    private static final int width = 1200;
    private static final int height = 700;
    private int endNode;
    public Map map = new Map();
    long oldTime;
    String nodeName = "";
    
    public Directions(int endNode, String nodeName, int backButton) {
        super(width, height, 1, false);
        addObject(new Button(0), width-50, height-50); // Add home button
        addObject(new Button(backButton), width-130, height-50); // Add back button
        this.endNode = endNode;
        this.nodeName = nodeName;
        addObject(map, 631, 357);
        addObject(new Button(true), width*2/3-100, height-40); // Adding the zoom buttons (+ and -)
        addObject(new Button(false), width*2/3-40, height-40);
        addObject(new Information(false, width, height), width/2, 50); // Creation of header bar with date/time
        addObject(new Information(), width-170, 65); // Display time in the top right
        oldTime = System.currentTimeMillis(); // Setup for idle timer
        setPaintOrder(Button.class, Information.class, Indicator.class, Path.class, Node.class, Map.class);
    }
    
    public void act() {
        //System.out.println(System.currentTimeMillis()-oldTime);
        if (Greenfoot.mouseClicked(null)) {
            oldTime = System.currentTimeMillis(); // Reset timer if the user clicks their mouse
        } else if (System.currentTimeMillis() > oldTime+20000) {
            Greenfoot.setWorld(new Menu(0, true)); // If user is idling, load menu and play idle video
        }
    }
    
    public int getEnd() {
        return endNode;
    }
    
    public String getNodeName() {
        return nodeName; // Room name of current node
    }
    
    public void addIndicator(int x1, int y1, int x2, int y2) { // Add the red direction indicator to world
        int direction = 180-(int)Math.toDegrees(Math.atan2(x2-x1,y2-y1)); // Convert direction from radians to degrees
        addObject(new Indicator(direction, x1, y1), width/3, height/2+60); // Add current location and direction indicator to the screen
    }
    
    public int newPath(int x1, int y1, int x2, int y2, int prevI) { // Created dotted path between two coordinates
        double currentX = x1;
        double currentY = y1;
        double direction = Math.atan2((y2-y1),(x2-x1)); // This calculates the angle between the two input coordinates in radians
        int density = 18; // How many pixels per path dot
        double repeat = Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2))/density; // How many dots to put in a particular segment
        for(int i=1; i < repeat; i++){
            addObject(new Path(i+prevI, (int)currentX, (int)currentY), 0, 0);
            currentX += density*Math.cos(direction);
            currentY += density*Math.sin(direction);
        }
        return (int)(repeat+prevI); // prevI ensures that there is a correct offset between dotted segments
    }
}
