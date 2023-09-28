public class Indicator extends MapElements {
    public int initialAngle = 98; // Initial angle of kiosk (true bearing - 0 is up)
    public int finalAngle; // Angle to face to begin navigation
    public int direction = 1;
    private int xOffset;
    private int yOffset;
    private int finalX;
    private int finalY;
    private int animationStage = 0;
    private double size;
    private double theta = 7.85;
    private double zoom;
    
    public Indicator(int finalAngle, int finalX, int finalY) {
        this.finalAngle = finalAngle; // This is the angle to begin the navigation
        this.finalX = finalX;
        this.finalY = finalY;
        size = 2.5;
        setRotation(initialAngle);
        if (finalAngle-initialAngle > 180) { // When in turning phase, turn shortest in direction
            direction = -1;
        }
        setImage("direction.png");
        getImage().scale((int)(128*size),(int)(128*size));
    }
    
    public void act() {
        if (animationStage == 0) { // First stage of animation - move towards starting position
            int[] location = convertLocation(); // Get coordinates of final location
            if (getX() > location[0]) {
                setLocation(getX()-2, getY()); // Move indicator in horizontal direction of starting position
            } else {
                setLocation(getX()+2, getY());
            }
            if (getY() > location[1]) {
                setLocation(getX(), getY()-2); // Move indicator in vertical direction of starting position
            } else {
                setLocation(getX(), getY()+2);
            }
            if (Math.abs(getX()-location[0]) < 3) { // Snap to destination location if within 3 pixels
                setLocation(location[0], getY());
            }
            if (Math.abs(getY()-location[1]) < 3) {
                setLocation(getX(), location[1]);
            }
            if (size >= 0.5) { // Shrink image until specific size
                setImage("direction.png");
                getImage().scale((int)(128*size),(int)(128*size));
                size -= 0.01;
            } else if (getX() == location[0] && getY() == location[1]) { // If correct size and location, begin next animation stage
                animationStage = 1;
            }
        } else {
            updateLocation(); // Snap to final location when view moved or scaled
            if (getRotation() != finalAngle) { // Rotation stage
                setRotation(getRotation()+1*direction); // Rotate 1 degree in shortest direction to final direction
            } else if (theta > 29.8) { // If has flashed about 3 times, remove
                getWorld().removeObject(this);
            } else { // Flashing stage
                getImage().setTransparency((int)(180+Math.sin(theta)*75)); // Set transparancy according to values in sine curve (pulsating)
                theta += 0.08;
            }
        }
    }    
    
    public int[] convertLocation() {
        Map map = ((Directions)getWorld()).map;
        zoom = map.getZoom();
        xOffset = map.getXOffset()-1100;
        yOffset = map.getYOffset()-629;
        return new int[] {(int)((finalX+xOffset-wid)*zoom)+wid, (int)((finalY+yOffset-hei)*zoom)+hei}; // Fix coordinates for zoom
    }
    
    public void updateLocation() {
        Map map = ((Directions)getWorld()).map;
        zoom = map.getZoom();
        xOffset = map.getXOffset()-1100;
        yOffset = map.getYOffset()-629;
        setLocation((int)((finalX+xOffset-wid)*zoom)+wid, (int)((finalY+yOffset-hei)*zoom)+hei);
    }
}
