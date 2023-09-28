import greenfoot.Actor;
import greenfoot.Greenfoot;
//import java.io.File;

public class Video extends Actor { // Class to play the video on the idle screen
    private static int totalFrames = 1102;//new File("images/video").listFiles().length; // This is the number of frames in the video
    private boolean show = true;
    private int frame = Greenfoot.getRandomNumber(totalFrames); // Start video on random frame
    private long oldTime;
    private int transparancy = 255;
    
    public Video(boolean vid) {
        oldTime = System.currentTimeMillis(); // Begin timer
        if (vid) { // vid is true if idling on directions screen
            setImage("video/vid"+frame+".jpg");
            frame++;
        } else { // vid is false if program starting or a back button has been pressed
            setImage("noimg.png");
            show = false;
        }
    }
    
    public void act() {
        if (Greenfoot.mouseClicked(null)) {
            oldTime = System.currentTimeMillis(); // Reset timer on click to detect when idling
        }
        if (transparancy == 5) { // Decrease transparancy until invisible when screen tapped
            transparancy = 255; // Reset transparency when hidden
            getImage().setTransparency(transparancy);
            setImage("noimg.png"); // Allows buttons to be pressed while hidden
        } else if (transparancy != 255) {
            transparancy -= 10;
            getImage().setTransparency(transparancy);
        }
        if (show) {
            if (frame == totalFrames) frame = 0; // Loop video
            setImage("video/vid"+frame+".jpg");  // While video is showing, display next frame
            frame++;
            if (Greenfoot.mouseClicked(null)) { // If screen tapped, begin hide process
                ((Menu)getWorld()).createMenu();
                transparancy -= 10;
                show = false;
            }
        } else if (System.currentTimeMillis() > oldTime+20000) {
            show = true;
        }
    }    
}
