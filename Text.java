import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Text extends Actor { 
    public GreenfootImage drawText(String text, int grey, int size) { // Create custom text image
        GreenfootImage textImg = new GreenfootImage(19*String.valueOf(text).length(), 27); // Text must be aligned appropriately
        textImg.setColor(new Color(grey, grey, grey, 255));
        textImg.setFont(new Font("Alata", false, false, size));
        textImg.drawString(text, 0, 20);
        return textImg;
    }
}
