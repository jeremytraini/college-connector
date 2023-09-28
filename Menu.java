import greenfoot.World;
import greenfoot.GreenfootImage;
import greenfoot.Greenfoot;
import greenfoot.Color;
import greenfoot.Font;
import java.awt.FileDialog;
import java.io.File;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

public class Menu extends World {
    private static final int width = 1200;
    private static final int height = 700;
    private static final int wid = width/2;
    private static final int hei = height/2;
    public Searchbar searchbar = new Searchbar();
    GreenfootImage placeImage = new GreenfootImage("icons/place.png");
    GreenfootImage dBackground = new GreenfootImage(width,height);
    ArrayList<RoomInfo> roomInfo = readFromFile2();
    
    public Menu() {
        super(width, height, 1, false);
        //long startTime = System.nanoTime(); // This is used for conducting benchmarking tests
        
        Greenfoot.setSpeed(60);
        setupMenu(0, false); // Load main menu with idle video hidden
        setPaintOrder(Video.class, Information.class, Searchbar.class, Results.class, Button.class);
        
        //long endTime = System.nanoTime();
        //System.out.println((endTime - startTime)/1e+9);
    }
        
    public Menu(int backButton, boolean vid) {
        super(width, height, 1, false);  
        setupMenu(backButton, vid);  // Load specified menu
        setPaintOrder(Video.class, Information.class, Searchbar.class, Results.class, Button.class);
    }
    
    public ArrayList readFromFile2() { // Load rooms file into ArrayList for searching
        ArrayList<RoomInfo> roomInfo = new ArrayList<>();
        try {
            InputStreamReader inputSR = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("roomData/rooms.txt")); // Creates input stream from file as a stream
            BufferedReader bufferedR = new BufferedReader(inputSR); // Read text from character-input stream
            for (int i = 0; i < 231; i++) {
                String[] data = bufferedR.readLine().split(","); // Creates an array for each element in each row of sequential file
                roomInfo.add(new RoomInfo(Integer.parseInt(data[0]), data[1], data[2]));
            }
        } catch(IOException e) {
            System.out.println("File Read error" + e);
        }
        return roomInfo;
    }
    
    public ArrayList readFromFile() { // (DEPRECATED VERSION) Load rooms file into ArrayList for searching
        ArrayList<RoomInfo> roomInfo = new ArrayList<>();
        Scanner dataReader = null;
        try {
            File readFile = new File("roomData/rooms.txt"); // Note that the use of File() does not work when exported to .jar
            dataReader = new Scanner(readFile);
            while (dataReader.hasNext()) {
                String[] data = dataReader.nextLine().split(","); // Creates an array for each element in each row of sequential file
                roomInfo.add(new RoomInfo(Integer.parseInt(data[0]), data[1], data[2]));
            }
            dataReader.close();
        } catch(IOException e) {
            System.out.println("File Read error" + e);
        }
        return roomInfo;
    }
    
    public void createBackground(boolean onlineHelp) {
        dBackground.setColor(new Color(240,240,240,255));
        dBackground.fillRect(0, 0, width, height); // Fill background with light grey
        if (onlineHelp) {
            GreenfootImage helpImg = new GreenfootImage(365,170); // Online help box
            helpImg.setColor(new Color(245, 245, 245, 255));
            helpImg.fill();
            
            helpImg.setColor(new Color(0, 0, 0, 255));
            helpImg.setFont(new Font("Alata", false, false, 27));
            helpImg.drawString("Online Help", 10, 30); // Online help title
            
            helpImg.setColor(new Color(40, 40, 40, 255));
            helpImg.setFont(new Font("Alata", false, false, 19)); // Online help text
            helpImg.drawString("• Please see packaged documentation \n   for a detailed tutorial of the software.\n• A video walkthrough of the\n   entire program is also available.", 10, 65);
            
            dBackground.drawImage(helpImg, 820, 515); // Place in bottom left of screen
        }
        setBackground(dBackground);
    }
    
    public void setupMenu(int backButton, boolean vid) {
        createHeader();
        createBackground(true);
        addObject(new Video(vid), wid, hei);
        switch (backButton) { // Draw corresponding icon onto button image
            case 0: // If backbutton is 0, make main menu
                createMenu();
                break;
            case 1: // If backbutton is 1, generate keyboard e.t.c.
                createResults("");
                break;
            case 2:
                createAmenities();
                break;
            case 3:
                createServices();
                break;
        }
    }
    
    public void createHeader() {
        addObject(new Information(true, width, height), wid, 60);
        addObject(new Information(), width-170, 65); // Display time in the top right
    }
    
    public void createMenu() {
        removeButtons();
        createBackground(true);
        addObject(new Button(500,80, "Search", 50), wid, hei);
        addObject(new Button(240,80, "Amenities", 30), wid-130, hei+100);
        addObject(new Button(240,80, "Services", 30), wid+130, hei+100);
    }
    
    public void createAmenities() {
        removeButtons();
        createBackground(false);
        addObject(new Button(0), width-50, height-50);
        addObject(new Button(240,80, "Mens", 30), wid-130, hei-50);
        addObject(new Button(240,80, "Womens", 30), wid-130, hei+50);
        addObject(new Button(240,80, "Disabled", 30), wid-130, hei+150);
        addObject(new Button(240,80, "Food", 30), wid+130, hei-50);
        addObject(new Button(240,80, "Bubblers", 30), wid+130, hei+50);
        addObject(new Button(240,80, "Lifts", 30), wid+130, hei+150);
    }
    
    public void createServices() {
        removeButtons();
        createBackground(false);
        addObject(new Button(0), width-50, height-50);
        addObject(new Button(240,80, "Clinic", 30), wid-130, hei);
        addObject(new Button(240,80, "Fields", 30), wid+130, hei);
        addObject(new Button(240,80, "Reception", 30), wid-130, hei+100);
        addObject(new Button(240,80, "Printers", 30), wid+130, hei+100);
    }
    
    public void removeButtons() {
        removeObjects(getObjects(Button.class));
        removeObjects(getObjects(Searchbar.class));
        removeObjects(getObjects(Results.class));
    }
    
    void bubbleSort(int unsortedArray[]) { // Implementation of a bubble sort algorithm
        boolean swapped;
        int temp;
        for (int i = 0; i < unsortedArray.length-1; i++) {
            swapped = false;
            for (int a = 0; a < unsortedArray.length-i-1; a++) {
                if (unsortedArray[a] > unsortedArray[a + 1]) {
                    temp = unsortedArray[a];
                    unsortedArray[a] = unsortedArray[a + 1];
                    unsortedArray[a + 1] = temp;
                    swapped = true;
                } 
            } 
            if (swapped == false) {
                break;
            }
        }
    }
    
    public void bubbleTestData() { // Driver algorithm used for bubble sort algorithm testing
        int testData[] = { 41,672,33,41,55,46,3,53,342 };
        bubbleSort(testData);
        System.out.println(Arrays.toString(testData)); // Output is [3, 33, 41, 41, 46, 53, 55, 342, 672]
        
        int testData2[] = { 882,152,143,519,190,990,914,89,155,620,249,3,356,575,796,975,294,6,476,355 };
        bubbleSort(testData2);
        System.out.println(Arrays.toString(testData2)); // Output is [3, 6, 89, 143, 152, 155, 190, 249, 294, 355, 356, 476, 519, 575, 620, 796, 882, 914, 975, 990]
    }
    
    public void multidimentionalArray() { // Multidimentional array implementation
        System.out.println("2D Array");
        int testArray[][] = { {5,2,1}, // Setting values for each cell of 2D array
                              {7,3,2},
                              {5,3,9},
                              {3,2,6},
                              {2,1,8}
                            };
        for (int i = 0; i < 5 ; i++) { // Printing the entire 2D array table in a readable way
            System.out.println("Index "+(i+1)+": ");
            for (int a = 0; a < 3; a++) {
                System.out.println("    Item "+(a+1)+": "+testArray[i][a]); 
            }
        }
        
        System.out.println();
        System.out.println("3D Array");
        int[][][] testArray2 = new int[4][3][3];
        testArray2[0] = new int[][] { {3,29,6}, // These could also have alternatively been declared in one line as done above
                                      {33,25,6},
                                      {4,2,6}
                                    }; 
        testArray2[1] = new int[][] { {4,27,60}, // This demponstrales modifying different rows
                                      {63,72,6},
                                      {86,26,6}
                                    };
        testArray2[2] = new int[][] { {38,97,6},
                                      {3,55,6},
                                      {66,1,6}
                                    };
        testArray2[3] = new int[][] { {30,52,46},
                                      {7,2,6},
                                      {3,24,36}
                                    };
        
        testArray2[0][2] = new int[] {3224, 434, 43}; // Modifying different arrays within the rows
        testArray2[3][2] = new int[] {33, 5665, 78};
        testArray2[1][0] = new int[] {45, 4, 56};
        
        testArray2[0][2][2] = 76; // Modifying individual cells
        testArray2[3][2][0] = 223;
        testArray2[1][0][1] = 4;
        
        for (int i = 0; i < 3; i++) { // Editing value in each element within a row
            testArray2[0][0][i] = 0; // Setting all Elements in Index 1 Item 1 to 0
        }
        
        for (int i = 1; i < 3; i++) { // Editing value in each of the cells for multiple rows
            for (int a = 0; a < 2; a++) {
                testArray2[0][i][a] = 777; // Setting first 2 Elements of last 2 Items to 777
            }
        }
        
        for (int i = 0; i < 4; i++) { // Printing the entire 3D array table in a readable way
            System.out.println("Index "+(i+1)+": ");
            for (int a = 0; a < 3; a++) {
                System.out.println("    Item "+(a+1)+": ");
                for (int j = 0; j < 3; j++) {
                    System.out.println("        Element "+(j+1)+": "+testArray2[i][a][j]); 
                }
            }
        }
    }
    
    public void createResults(String search) {
        search = search.toLowerCase();
        ArrayList<String> results = new ArrayList<String>();
        ArrayList<Integer> resultsEnd = new ArrayList<Integer>();
        ArrayList<String> resultsDept = new ArrayList<String>();
        ArrayList<String> availableLetters = new ArrayList<String>();
        int resultNum = 0;
        for (int i = 0; i < roomInfo.size(); i++) { // Linear search algorithm
            String entry = roomInfo.get(i).getNodeName();
            String dept = roomInfo.get(i).getDepartmentName();
            if (entry.toLowerCase().replaceAll("[^a-zA-Z0-9]+"," ").startsWith(search)) {
                resultsEnd.add(roomInfo.get(i).getNodeID());
                results.add(entry); // Add entry to results list entry name matches with search
                resultsDept.add(dept);
                String capsLetter = Character.toString(entry.charAt(search.length())).toUpperCase().replaceAll("[^a-zA-Z0-9]+"," ");
                if (search.length() < entry.length() && !availableLetters.contains(capsLetter)) {
                    availableLetters.add(capsLetter);
                }
                resultNum++;
            } else if (dept.toLowerCase().replaceAll("[^a-zA-Z0-9]+"," ").startsWith(search)) {
                resultsEnd.add(roomInfo.get(i).getNodeID());
                results.add(entry);  // Add entry to results list if department name matches with search
                resultsDept.add(dept);
                String capsLetter = Character.toString(dept.charAt(search.length())).toUpperCase().replaceAll("[^a-zA-Z0-9]+"," ");
                if (search.length() < dept.length() && !availableLetters.contains(capsLetter)) {
                    availableLetters.add(capsLetter);
                }
                resultNum++;
            }
        }
        
        //System.out.println(Arrays.toString(results.toArray()));
        //System.out.println(Arrays.toString(resultsEnd.toArray()));
        //System.out.println(Arrays.toString(resultsDept.toArray()));
        //System.out.println(Arrays.toString(availableLetters.toArray()));
        
        removeObjects(getObjects(Results.class));
        GreenfootImage background = new GreenfootImage(width,height);
        background.setColor(new Color(240,240,240,255));
        background.fillRect(0, 0, width, height);
        background.setColor(new Color(255,255,255,255));
        background.fillRect(25, 120, 450, 580); // Make box for results
        
        background.setColor(new Color(50,50,50,255));
        background.setFont(new Font("Alata", false, false, 18));
        background.drawString("Select a letter to begin search...", 50, 155); // Online help title
        
        setBackground(background);
        createKeyboard(availableLetters);
        if (search.length() != 0) {
            for (int i = 0; i < resultNum; i++) {
                GreenfootImage image = new  GreenfootImage(450, 70);
                image.setColor(new Color(255,255,255,255));
                image.fill();
                image.setColor(new Color(240,240,240,255));
                image.drawLine(0, 69, 450, 69);
                image.setColor(new Color(100, 100, 100, 255));
                image.setFont(new Font("Alata", false, false, 27));
                if (results.get(i).length() < 27) {
                    image.drawString(results.get(i), 40, 40);
                } else {
                    image.drawString(results.get(i).substring(0, 24)+"...", 40, 40);
                }
                image.setColor(new Color(200, 200, 200, 255));
                image.setFont(new Font("Alata", false, false, 15));
                image.drawString(resultsDept.get(i), 40, 60);
                image.drawImage(placeImage, 390, 15);
                addObject(new Results(i, resultNum, image, resultsEnd.get(i), results.get(i)),250, 155+i*70);
            }
        } else {
            searchbar.clearSearch();
        }
    }
    
    public void createKeyboard(ArrayList availableLetters) {
        int x = 550;
        int y = 300;
        removeButtons();
        addObject(new Button(0), width-50, height-50);
        addObject(searchbar, x+585/2-30, y-60);
        addObject(new Button("←", 60, true), 9*65+x, y-60);
        String[] rows = new String[] {"1234567890","qwertyuiop","asdfghjkl","zxcvbnm"};
        int off = 0;
        for (int i = 0; i < rows.length; i++) {
            if (i == 2) {
                off = 20;
            } else if (i == 3) {
                off = 50;
            }
            for (int a = 0; a < rows[i].length(); a++) {
                boolean available = availableLetters.contains(Character.toString(rows[i].charAt(a)).toUpperCase());
                addObject(new Button(Character.toString(rows[i].charAt(a)).toUpperCase(), 60, available), a*65+x+off,y+55*i);
            }
        }
        addObject(new Button(" ", 580, availableLetters.contains(" ")), x+650/2-30,y+55*4);
    }
}
