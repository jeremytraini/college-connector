import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;
import greenfoot.Color;
import greenfoot.Font;

public class Button extends Text {
    private int backButton = -1;
    private int width;
    private boolean active;
    private String text;
    private String letter;
    private boolean zoomIn = true;
    
    public Button(int backButton) { // Create back button
        this.backButton = backButton;
        GreenfootImage image = new  GreenfootImage(70, 70);
        image.setColor(new Color(50,50,50,255));
        image.fill();
        GreenfootImage icon;
        if (backButton == 0) { // If button directs to main menu, set image to home
            icon = new GreenfootImage("icons/home.png");
        } else { // Otherwise set to back button
            icon = new GreenfootImage("icons/back.png");
        }
        image.drawImage(icon, 5,5);
        setImage(image);
    }
    
    public Button(boolean zoomIn) { // Create zoom button
        GreenfootImage img = new  GreenfootImage(50, 50);
        img.setColor(new Color(50,50,50,255)); // Fill button with dark grey
        img.fill();
        if (zoomIn) {
            img.drawImage(new GreenfootImage("icons/in.png"), 5, 5); // Set image based on button type (+ or -)
        } else {
            img.drawImage(new GreenfootImage("icons/out.png"), 5, 5);
            this.zoomIn = false;
        }
        setImage(img);
    }
    
    public Button(String letter, int width, boolean active) { // Create keyboard button
        this.letter = letter;
        this.width = width;
        this.active = active;
        updateButton(active); // Set keyboard letter colour based on whether its "active"
    }
    
    public void updateButton(boolean active) { // Update keyboard letter colour
        GreenfootImage image = new  GreenfootImage(width, 50);
        if (active) {
            image.setColor(new Color(50,50,50,255));
        } else {
            image.setColor(new Color(150,150,150,255));
        }
        image.fill();
        image.setColor(new Color(255, 255, 255, 255));
        image.setFont(new Font("Alata", false, false, 27));
        image.drawString(letter, 20, 35);
        setImage(image);
    }
    
    public Button(int width, int height, String text, int size) { // Create onscreen menu buttons
        this.text = text;
        GreenfootImage image = new  GreenfootImage(width, height); // Create button image
        image.setColor(new Color(50,50,50,255)); // Fill with dark grey
        image.fill();
        image.setColor(new Color(255, 255, 255, 255));
        image.setFont(new Font("Alata", false, false, size));
        GreenfootImage icon;
        
        switch (text) { // Draw corresponding icon onto button image
            case "Search":
                image.drawImage(new GreenfootImage("icons/search.png"), 130, 15);
                break;
            case "Amenities":
                image.drawImage(new GreenfootImage("icons/toilet.png"), 15, 15);
                break;
            case "Services":
                image.drawImage(new GreenfootImage("icons/building.png"), 15, 15);
                break;
            case "Mens":
                image.drawImage(new GreenfootImage("icons/mens.png"), 60, 15);
                break;
            case "Womens":
                image.drawImage(new GreenfootImage("icons/womens.png"), 45, 15);
                break;
            case "Disabled":
                image.drawImage(new GreenfootImage("icons/disabled.png"), 25, 15);
                break;
            case "Food":
                image.drawImage(new GreenfootImage("icons/food.png"), 50, 15);
                break;
            case "Bubblers":
                image.drawImage(new GreenfootImage("icons/bubblers.png"), 25, 15);
                break;
            case "Lifts":
                image.drawImage(new GreenfootImage("icons/lifts.png"), 50, 15);
                break;
            case "Clinic":
                image.drawImage(new GreenfootImage("icons/nurse.png"), 40, 15);
                break;
            case "Fields":
                image.drawImage(new GreenfootImage("icons/fields.png"), 40, 15);
                break;
            case "Reception":
                image.drawImage(new GreenfootImage("icons/reception.png"), 15, 15);
                break;
            case "Printers":
                image.drawImage(new GreenfootImage("icons/printers.png"), 25, 15);
                break;
        }
        image.drawString(text, (image.getWidth()-((size/2)*String.valueOf(text).length()))/2+25, image.getHeight()/2+(size-10)/2); // Draw text string onto button
        setImage(image);
    }
    
    public void act() {
        if (Greenfoot.mouseClicked(this)) { 
            if (text != null) { // If button is a menu button
                switch (text) { // Execute function based on button text
                    case "Search":
                        ((Menu)getWorld()).createResults("");
                        break;
                    case "Amenities":
                        ((Menu)getWorld()).createAmenities();
                        break;
                    case "Services":
                        ((Menu)getWorld()).createServices();
                        break;
                    case "Mens":
                        Greenfoot.setWorld(new Directions(317, "Nearest Mens", 2));
                        break;
                    case "Womens":
                        Greenfoot.setWorld(new Directions(43, "Nearest Womens", 2));
                        break;
                    case "Disabled":
                        Greenfoot.setWorld(new Directions(44, "Nearest Disabled", 2));
                        break;
                    case "Food":
                        Greenfoot.setWorld(new Directions(350, "Nearest Food", 2));
                        break;
                    case "Bubblers":
                        Greenfoot.setWorld(new Directions(38, "Nearest Bubbler", 2));
                        break;
                    case "Lifts":
                        Greenfoot.setWorld(new Directions(297, "Nearest Lift", 2));
                        break;
                    case "Clinic":
                        Greenfoot.setWorld(new Directions(195, "Clinic", 3));
                        break;
                    case "Fields":
                        Greenfoot.setWorld(new Directions(202, "Nearest Field", 3));
                        break;
                    case "Reception":
                        Greenfoot.setWorld(new Directions(51, "Reception", 3));
                        break;
                    case "Printers":
                        Greenfoot.setWorld(new Directions(51, "Nearest Printer", 3));
                        break;
                }
            } else if (letter != null) { // If button is a keyboard key
                if (active) ((Menu)getWorld()).searchbar.updateSearch(letter);
            } else if (backButton != -1) { // If back button
                Greenfoot.setWorld(new Menu(backButton, false)); // Go back to menu screen with backButton parameter
            } else if (getWorld() instanceof Directions) { // If on directions screen (and not another button)
                ((Directions)getWorld()).map.setZoom(zoomIn); // Zoom when clicked
            } else {
                ((Menu)getWorld()).createMenu(); // Otherwise, change menu to main menu
            }
        }
    }    
}
