import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
public class gameSquare extends JFrame {
    private final int WIDTH;
    private final int HEIGHT;
    private final int noOfIncorrect;
    private final int passLength;
    private final String gameImgDIR;
    private final String gameImgType;
    private final String imgBaseDIR;
    private final String letterImgDIR;
    private final String letterImgType;
    private JLabel background;
    private letterRectangle gameRack;
    private hangman gameHangman;
    private int numIncorrect;
    private JLabel correct;
    private JLabel incorrect;
    private String password;
    private StringBuilder passwordHidden;
    public gameSquare(){
        WIDTH = 600;
        HEIGHT = 550;
        noOfIncorrect = 6;
        passLength = 20;
/*
        try {
            Image backgroundImage = ImageIO.read(new File("back.png"));
            JLabel background = new JLabel(new ImageIcon(backgroundImage));
            setContentPane(background);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        gameImgDIR = letterImgDIR = "images/";
        gameImgType = letterImgType = ".png";
        imgBaseDIR = "hangman";

      /*  try {
            ImageIcon backgroundImg = new ImageIcon(ImageIO.read(new File("back.png")));
            background = new JLabel(backgroundImg);
            background.setLayout(new BorderLayout());
            add(background, BorderLayout.CENTER);
        } catch (IOException e) {
            System.out.println("Error loading background image: " + e.getMessage());
        }*/

        setTitle("Hangman");
        setSize(WIDTH, HEIGHT);
        setResizable(true);
        getContentPane().setBackground(new Color(173, 216, 230));
        addCloseWindowListener();

        initialize();

    }

    //one of the more important methods. Initializes the gameSquare after every round played.
    private void initialize()
    {
        //sets the number of incorrect tries displayed to be 0
        numIncorrect = 0;

        //resets the Word label
        correct = new JLabel("Word: ");
        //setting the custom font for both correct and incorrect
        correct.setFont(new java.awt.Font("Consolas", 1, 24));
        incorrect = new JLabel("Incorrect: " + numIncorrect);
        incorrect.setFont(new java.awt.Font("Consolas", 1, 24));
        //generates a password to be used
        password = new String();
        //makes a new string Builder for asterisk builder to hide the password
        passwordHidden = new StringBuilder();

        //calls the get password function to either and ask the user to either create their own word
        //or generate a new word
        getPassword();

        //adds the panel that shows Word: and number of incorrect guesses
        addTextPanel();
        //adds the bottom letter rack that displays all the alphabets
        addLetterRack();
        //adds a JPanel where the images of hangman are to be rendered
        addHangman();


        // set board slightly above middle of screen to prevent dialogs
        //     from blocking images

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getSize().width / 2,
                dim.height / 2 - getSize().height / 2 - 80);
        setVisible(true);
    }

    //closing operation
    private void addCloseWindowListener()
    {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent we)
            {
                int prompt = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to quit?",
                        "Quit?",
                        JOptionPane.YES_NO_OPTION);

                if (prompt == JOptionPane.YES_OPTION)
                    System.exit(0);
            }
        });
    }
    private void addTextPanel()
    {
        //new JPanel object
        JPanel textPanel = new JPanel();
        //set a layout for the JPanel make it 1 row and two columns
        textPanel.setLayout(new GridLayout(1,2));
        //this is what the "Word:" thing is
        textPanel.add(correct);
        //this is the panel that displays "Incorrect:"
        textPanel.add(incorrect);
        //Setting the font
        textPanel.setFont(new java.awt.Font("Consolas", 2, 36));
        // use BorderLayout to set the components of the gameboard in
        //     "visually agreeable" locations
        add(textPanel, BorderLayout.NORTH);
    }
    private void addLetterRack()
    {
        //makes an object of the rectangle class, give it arguments, the password, the directory where the letter
        //images are and the type of image(png) in our case
        gameRack = new letterRectangle(password,
                letterImgDIR,
                letterImgType);
        //adding listeners to the game rack to detext mouse input
        gameRack.attachListeners(new TileListener());
        //add the game rack to the south of the Frame
        add(gameRack, BorderLayout.SOUTH);
    }
    private void addHangman()
    {
        JPanel hangmanPanel = new JPanel();
        gameHangman = new hangman(imgBaseDIR,
                gameImgDIR,
                gameImgType);
        hangmanPanel.add(gameHangman);
        add(hangmanPanel, BorderLayout.CENTER);
    }

    //important
    public static String getCategory(String fileName) {
        ArrayList<String> wordList = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] words = line.split(" ");
                for (String word : words) {
                    wordList.add(word);
                }
            }
            if (!wordList.isEmpty()) {
                Collections.shuffle(wordList);
                return wordList.get(0);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Could not find the file");
        }
        return null;
    }

    //important
    private void getPassword()
    {
        String[] options = {"Choose your own word","Random Words", "Quit"};
        JPanel passwordPanel = new JPanel();
        JLabel passwordLabel = new JLabel("Enter Password to Be Guessed: ");
        JTextField passwordText = new JTextField(passLength);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordText);
        int confirm = -1;

        while (password.isEmpty())
        {
            confirm = JOptionPane.showOptionDialog(null,
                    passwordPanel,
                    "Enter Password",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (confirm == 0)
            {
                password = passwordText.getText();
                lengthCheck();
            }
            else if (confirm == 1){
                passwordGen();
            }
            else if (confirm == 2)
                System.exit(0);
        }

        // use a regular expression to replace all characters with *'s and
        //     hide the password when it is displayed
        //important
        passwordHidden.append(password.replaceAll(".", "*"));
        correct.setText(correct.getText() + passwordHidden.toString());
    }
    //important
    private void passwordGen(){
        String[] options = {"Animals","Countries", "Cities"};
        JPanel categoryPanel = new JPanel();
        JLabel categoryLabel = new JLabel("Select Category to Pick Words from ");
        categoryPanel.add(categoryLabel);
        int confirm = -1;

        while (password.isEmpty())
        {
            confirm = JOptionPane.showOptionDialog(null,
                    categoryPanel,
                    "Enter Password",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (confirm == 0)
            {
                password = getCategory("animals.txt");
                lengthCheck();

            }

            else if (confirm == 1){
                password = getCategory("countries.txt");
                lengthCheck();

            }

            else if (confirm == 2)
                password = getCategory("cities.txt");
                lengthCheck();

        }
    }
    private void lengthCheck(){
        if (!password.matches("[a-zA-Z]+") ||
                password.length() > passLength)
        {
            JOptionPane.showMessageDialog(null,
                    "Password must be less than 10 characters and " +
                            "only contain letters A-Z.",
                    "Invalid Password",
                    JOptionPane.ERROR_MESSAGE);
            password = ""; // empty password if error occurs
        }

    }
    private void newGameDialog()
    {
        int dialogResult = JOptionPane.showConfirmDialog(null,
                "The word was: " + password +
                        "\nWould You Like to Start a New Game?",
                "Play Again?",
                JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION)

            initialize(); // re-initialize the GameBoard
        else
            System.exit(0);
    }

    //logic behind the game
    private class TileListener implements MouseListener
    {
        //override the method because MouseListener is an interface, all methods have to be
        //overridden
        @Override
        //important
        public void mousePressed(MouseEvent e)
        {
            //make a variable of the location of the mouse click, where it was clicked on the screen
            Object source = e.getSource();
            //If the the mouseclick(source) it on of of the letters(letter
            //objects of the tilesLetter class) then run this if statement
            if(source instanceof tilesLetter)
            {
                //declare a character variable and give it an empty dummy value
                char c = ' ';
                int index = 0;
                //used to check if the while statement is run
                boolean updated = false;

                // cast the source of the click to a LetterTile object
                tilesLetter tilePressed = (tilesLetter) source;
                c = tilePressed.guess();

                // reveal each instance of the character if it appears in
                // the password
                while ((index = password.toLowerCase().indexOf(c, index)) != -1)
                {
                    passwordHidden.setCharAt(index, password.charAt(index));
                    index++;
                    updated = true;
                }

                // if the guess was correct, update the GameBoard and check
                //     for a win
                if (updated)
                {
                    correct.setText("Word: " + passwordHidden.toString());

                    if (passwordHidden.toString().equals(password))
                    {
                        gameRack.removeListeners();
                        gameHangman.winningScreen();
                        newGameDialog();
                    }
                }

                // otherwise, add an incorrect guess and check for a loss
                else
                {
                    incorrect.setText("Incorrect: " + ++numIncorrect);

                    if (numIncorrect >= noOfIncorrect)
                    {
                        gameHangman.losingScreen();
                        gameRack.removeListeners();
                        newGameDialog();
                    }

                    else
                        gameHangman.nextImage(numIncorrect);
                }
            }
        }

        // These methods must be implemented in every MouseListener
        //     implementation, but since they are not used in this application,
        //     they contain no code

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    }
}
