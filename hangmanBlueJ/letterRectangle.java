import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
//the tray where ech letter is served
public class letterRectangle extends JPanel {
    //number of columns
    private final int rackCol;
    //number of rows
    private final int rackRow;
    //gridlayout class to arrange the letters in a grid
    private final GridLayout rectLayout;
    //total number of letters to be placed on the rack
    private final int capacity;
    //where each letter img is located aka the Images folder
    private final String imgDIR;
    //type of image, in our case a png
    private final String imgType;
    //word to guess
    private final String password;
    //ArrayList contains the tilesLetter class that renders each individual letter image, call it rack
    private final ArrayList<tilesLetter> rack;
    //no argument constructor
    public letterRectangle(){
        this("password", "images/", ".png");
    }
    //main constructor
    public letterRectangle(String imgPassword, String imageDirectory, String imageType){
        rackCol = 2;
        rackRow = 13;
        rectLayout = new GridLayout(rackCol,rackRow);
        rectLayout.setVgap(10);
        capacity = rackCol * rackRow;

        imgDIR = imageDirectory;
        imgType = imageType;

        //make a new arraylist and assign it to the rack we made earlier
        rack = new ArrayList<>();

        password = imgPassword;

        //adding some padding
        setBorder(BorderFactory.createEmptyBorder(10,17,10,10));
        setLayout(rectLayout);
        loadRack();



    }
    //loading the rack with an enhanced for loop
    private void loadRack(){
        buildRack();
        //increment through rack, each element called tile, add the tile. add is a function of the swing packagae
        //it adds the tile(letter) to its assigned location
        for (tilesLetter tile : rack)
            add(tile);
    }
    //important
    private void buildRack()
    {
        //password that's fed is converted into all lowercase
        StringBuilder passwordBuilder =
                new StringBuilder(password.toLowerCase());
        //primitive types cannot be used so Character is used
        ArrayList<Character> tiles = new ArrayList<>(); // cannot use char
        Random rand = new Random();
        int i = 0, j = 0;

        // add the password letters to the rack
        while (!passwordBuilder.isEmpty())
        {
            // want to make sure that no letters are repeated in tile rack
            if (!tiles.contains(passwordBuilder.charAt(0)))
            {
                tiles.add(passwordBuilder.charAt(0));
                i++;
            }
            passwordBuilder.deleteCharAt(0);
        }

        // add random values to fill the rest with the rack
        for (; i < capacity; i++)
        {
            Character c = 'a'; // 'a' is just a default value
            do
            {
                c = (char) (rand.nextInt(26) + 'a');
            } while (tiles.contains(c));
            tiles.add(c);
        }


        // grab random tiles from the ArrayList to display randomly on the
        //    GameBoard
        for (i = 0; i < capacity; i++)
        {
            j = rand.nextInt(tiles.size());
            rack.add(new tilesLetter(tiles.get(j),
                    imgDIR,
                    imgType));
            tiles.remove(j);
        }
    }
    public void attachListeners(MouseListener l)
    {
        for (tilesLetter tile : rack)
            tile.addTileListener(l);
    }
    public void removeListeners()
    {
        for (tilesLetter tile : rack)
            tile.removeTileListener();
    }
}