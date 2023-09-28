import greenfoot.Actor;
import greenfoot.GreenfootImage;
import greenfoot.Color;
import greenfoot.Font;

public class Searchbar extends Actor {
    private String search = "";
    
    public Searchbar() { // Create empty searchbar
        clearSearch();
    }
    
    public void clearSearch() { // Clear search variable and clear searchbar
        search = "";
        GreenfootImage image = new  GreenfootImage(585, 50);
        image.setColor(new Color(255,255,255,255));
        image.fill();
        setImage(image);
    }
    
    public void updateSearch(String letter) {
        GreenfootImage image = new  GreenfootImage(585, 50); // Create new searchbar image
        image.setColor(new Color(255,255,255,255));
        image.fill();
        if (letter.length() != 0) {
            image.setColor(new Color(100, 100, 100, 255));
            image.setFont(new Font("Alata", false, false, 27));
            if (letter == "←") { // If letter pressed is backspace character
                if (search.length() != 0) {
                    search = search.substring(0, search.length() - 1); // Remove last character from search
                }
            } else {
                search += letter; // Add letter to string
            }
            if (search.length() < 35) { // Display text and truncate search if too long for searchbar
                image.drawString(search, 20, 35);
            } else {
                image.drawString(search.substring(0, 32)+"...", 20, 35);
            }
            ((Menu)getWorld()).createResults(search);
        } else {
            search = ""; // Clear search if letter is empty
        }
        setImage(image);
    }
}
