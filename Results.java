import greenfoot.Actor;
import greenfoot.GreenfootImage;
import greenfoot.Greenfoot;
import greenfoot.MouseInfo;
import greenfoot.Color;

public class Results extends Actor {
    private int resultNum = 0;
    private int yPosition = 0;
    private int node = 0;
    private int yOffset = 155;
    private int initMouseY;
    private int mouseY;
    private boolean mouseDown = false;
    private String nodeName = "";
    
    public Results(int index, int resultNum, GreenfootImage image, int node, String nodeName) {
        this.yPosition = index*70; // Set initial position to list position times item height
        setLocation(250, yOffset+yPosition);
        this.resultNum = resultNum;
        this.node = node;
        this.nodeName = nodeName;
        //image.setColor(Color.BLACK);
        //image.drawString(Integer.toString(node),50,50); // For testing purposes
        setImage(image);
    }
    
    public void act() { // Handles the moevement of the results when dragged and action when clicked
        if (!mouseDown && Greenfoot.mousePressed(null)) {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (mouse.getX() < 500) { // If mouse is in results area and is pressed
                mouseDown = true;
                if (mouse != null) { // If mouse is within the window, begin scroll
                    mouseY = mouse.getY()-yOffset;
                    initMouseY = mouse.getY();
                }
            }
        }
        
        if (mouseDown) {
            if (Greenfoot.mouseDragEnded(null) || Greenfoot.mouseClicked(null)) {
                MouseInfo mouse = Greenfoot.getMouseInfo();
                if (Greenfoot.mouseClicked(this) && mouse.getY()-initMouseY == 0) { // If mouse has not moved, register as a click
                    Greenfoot.setWorld(new Directions(node, nodeName, 1)); // Select this result and get directions
                }
                mouseDown = false;
            }
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (mouse != null) {
                yOffset = mouse.getY()-mouseY; // Calculate where to place item
                if (yOffset < -resultNum*70+700) {
                    yOffset = -resultNum*70+700; // Set uppermost scroll bound
                }
                if (yOffset > 155) {
                    yOffset = 155; // Set lowwermost scroll bound
                }
            }
            setLocation(250, yPosition+yOffset);
        }
    }    
}
