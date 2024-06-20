import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class tilesLetter extends JLabel {
    private final char letterIMG;
    private final String letterDIR;
    private final String letterType;
    private final int defaultWidth;
    private final int defaultHeight;
    private String path;
    private BufferedImage img;
    public tilesLetter(){this('a', "Images/", ".png");}
    public tilesLetter(char imageLetter, String imageDIR, String imageType){

        letterIMG = imageLetter;
        letterDIR = imageDIR;
        letterType = imageType;

        defaultHeight = defaultWidth = 50;

        setPreferredSize(new Dimension(defaultWidth,defaultHeight));
        path = letterDIR + letterIMG + letterType;
        img = loadImage(path);


    }
    //important
    private BufferedImage loadImage(String image_path){
        BufferedImage img = null;

        try{
            img = ImageIO.read(new File(image_path));
        }
        catch (IOException e){
            System.err.println("Error executing loadImage() at " + image_path);

        }
        return img;
    }
    private void loadNewImage(String suffix){
        path = letterDIR + letterIMG + "_" + suffix + letterType;
        img = loadImage(path);
        repaint();
    }
    private MouseListener tilesListener;

    //a method to add a tileListener to each new letter placed on the Letter Rectangle
    public void addTileListener(MouseListener newOne){
        tilesListener = newOne;
        addMouseListener(tilesListener);
    }
    public void removeTileListener(){removeMouseListener(tilesListener);}
    public char guess(){
        loadNewImage("guessed");
        removeTileListener();
        return letterIMG;
    }
    @Override
    protected void paintComponent(Graphics newG){
        super.paintComponent(newG);
        newG.drawImage(img,0,0,defaultWidth,defaultHeight,null );

    }
}
