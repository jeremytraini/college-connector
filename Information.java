import greenfoot.GreenfootImage;
import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.Font;
import java.io.File;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Information extends Text { // Information panel class
    private int clockRegulator = -1;
    
    public Information(boolean inMenu, int width, int height) { // Displays screen headers
        GreenfootImage image;
        if (inMenu) { // True if in keyboard screen
            image = new  GreenfootImage(width, 120);
            image.setColor(new Color(240,240,240,255)); // Extend header down to create top cap results screen
            image.fillRect(0, 100, width, 20);
        } else {
            image = new  GreenfootImage(width, 100);
        }
        GreenfootImage bar = new GreenfootImage(width, 100);
        bar.setColor(new Color(190,190,190,255));
        bar.fill();
        image.drawImage(bar, 0,0);
        image.drawImage(new GreenfootImage("logo.png"),40,20); // Barker logo on the left
        image.setColor(new Color(30,30,30,255));
        setImage(image); // Create then display header bar graphic
    }
    
    public Information(int width, int height, String dest, int walkTime, int node) { // Display distination information panel
        GreenfootImage image = new  GreenfootImage(width/3, height);
        image.setColor(new Color(245,245,245,255));
        image.fill();
        image.drawImage(drawText("Directions To", 80, 27), 50, 160);
        try { // This is used due to export faliures when using new File("images/places/"+node+".jpg").exists()
            image.drawImage(new GreenfootImage("images/places/"+node+".jpg"), 50, 220);
        } catch(Exception e) {
            image.setColor(new Color(220,220,220,255)); // If not valid, display placeholder image
            image.fillRect(50, 220, 300, 225);
            image.drawImage(new GreenfootImage("images/icons/camera.png"), 170, 295);
        }
        image.drawImage(drawText("Destination: "+dest, 80, 18), 50, 460);
        image.drawImage(drawText("Walk time: "+walkTime+" mins", 80, 18), 50, 490);
        image.setColor(new Color(200,200,200,255));
        image.drawLine(0,0,0,height);
        setImage(image);
    }
    
    public Information() {
        clockRegulator = (clockRegulator+1)%200; // Create time update delay
        updateTime();
    }
    
    public void act() {
        if (clockRegulator != -1) {
            clockRegulator = (clockRegulator+1)%200; // Create time update delay
            if (clockRegulator == 0) {
               updateTime();
            }
        }
    }    
    
    public void updateTime() { // Format date and time then display as string in top right
        DateFormat dateFormat = new SimpleDateFormat("EEE d MMM");
        DateFormat timeFormat = new SimpleDateFormat("h:mma");  
        Date currenttime = Calendar.getInstance().getTime();
        String strDate = dateFormat.format(currenttime);
        String strTime = timeFormat.format(currenttime).toLowerCase();
        GreenfootImage image = new GreenfootImage(400,50);
        image.drawImage(drawText(strDate, 255, 23), 0, 0);
        image.drawImage(drawText(strTime, 255, 27), 200,0);
        setImage(image);
    }
}
