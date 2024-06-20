import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;
import javax.imageio.ImageIO;
import javax.swing.*;
//way to use images in your program is through the JLabel class
public class hangman extends JLabel{
    //width of image
    private final int defaultWidth;
    //height of image
    private final int defaultHeight;

    //base name for image
    private final String image_base_name;

    //type of image
    private final String image_type;

    //where the image folder is
    private final String image_directory;

    //where the image is
    private String path;

    //image being displayed
    private BufferedImage image;

    //basic no argument constructor
    public hangman(){
        this("hangman", "images/",".png");
    }

    //creates the image of the hangman after being given the parameters
    public hangman(String imageBaseName, String imageDirectory, String imageType){
        defaultWidth = 600;
        defaultHeight = 370;

        image_base_name = imageBaseName;
        image_directory = imageDirectory;
        image_type = imageType;

        //image numbering "_(image number)

        setBackground(new Color(51, 153, 255));
        setPreferredSize(new Dimension(defaultWidth,defaultHeight));
        path = image_directory + image_base_name + "_0" + image_type;
        image = loadImage(path);

        setBackground(new Color(51, 153, 255));

    }

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


    public void nextImage(int imageNo){
        loadNewImage(String.valueOf(imageNo));
    }

    public void losingScreen(){loadNewImage("lose");}

    public void winningScreen(){loadNewImage("win");}

    private void loadNewImage(String suffix){
        path = image_directory + image_base_name + "_" + suffix + image_type;
        image = loadImage(path);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics newG){
        super.paintComponent(newG);
        newG.drawImage(image,0,0,defaultWidth,defaultHeight,null);
        setBackground(new Color(51, 153, 255));

    }
}
