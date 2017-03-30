/*	Mastermind game created by Mateusz Janusz (mjanu001@gold.ac.uk)
 *  	March/April 2017
 *  
 * 
 * 
 * */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class myMastermind {
	private int colours;
    private int guesses;
    private int pegs;
    private int counter; //is it needed?
    private int[] secretCode;
    private int[] computerCode;
    private int blackPegs;
    private int whitePegs;

    //SWING
    private JFrame mainFrame;
    private JLabel secretLabel;
    private JLabel computerLabel;
    private JPanel topPanel;
    private JPanel guessPanel;
    private JButton guessButton;
    private JButton[] secretButtons;


    /*
	 GAME CONSTRUCTOR
	 specifying number of chances to guess, number of pegs and number of different colours
	 method calls 2 methods: to create secret code and then it calls
	 as many times as possible (number of guesses specified) a method for computer to guess the secret code
*/
    public myMastermind(int numberOfGuesses, int numberOfPegs, int numberOfColours){
    	guesses = numberOfGuesses;
    	pegs = numberOfPegs;
    	colours = numberOfColours;
    	counter = 0;
    	secretCode = new int[pegs];
    	computerCode = new int[pegs];

    	createGUI();
    	createSecretCode();


    	//for each try computer tries to guess the secret code
        for (int i=0; i<guesses; i++) {
        	computerRandomGuess();
        	System.out.println(Arrays.toString(computerCode)); //replace with code to show visible computer guess colour pegs
	        check(computerCode);
	        System.out.println("White pegs: " + whitePegs + " & Black pegs: " + blackPegs); //replace with code to show visible black and white pegs
	        if(blackPegs==4){
	        	System.out.println("Computer guessed!"); //replace with code to show dialog with option to start again
	        	System.exit(0);
	        }
        }
        System.out.println("-- You've won! --"); //replace with code to show dialog with option to start new game
    }

    private void createGUI(){
      mainFrame = new JFrame("Mastermind by Mateusz Janusz");
      mainFrame.setSize(500,500);
      mainFrame.setLayout(new GridLayout(guesses, pegs));

      /* text labels */
      secretLabel = new JLabel("Your secret code",JLabel.CENTER);
      computerLabel = new JLabel("Computer guesses",JLabel.CENTER);  

      /* panels */
      topPanel = new JPanel();  
      topPanel.setLayout(new FlowLayout()); 
      guessPanel = new JPanel();
      guessPanel.setLayout(new FlowLayout());

      /* buttons */
      guessButton = new JButton("Start guessing");
      secretButtons = new JButton[colours];      
      
      /* create expected number of buttons for secret code 
      and add it to the panel */
      for(int i=0; i<colours; i++){
        secretButtons[i] = new JButton();
        secretButtons[i].addActionListener(getColor(i));
        topPanel.add(secretButtons[i]);
      }
      guessPanel.add(guessButton);

      /* padding */
      topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10,
        10, 10));


      mainFrame.add(secretLabel);
      mainFrame.add(topPanel);
      mainFrame.add(computerLabel);
      mainFrame.add(guessPanel);
      mainFrame.setVisible(true);
      mainFrame.setMinimumSize(new Dimension(guesses*50,colours*80)); 
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      }); 
   }

   private void setButtonColour(i){

    public void actionPerformed(ActionEvent e) {
              ((JButton)(e.getSource())).setBackground(getColor(i));
            }
   }
    /*
     * Method to create a secret code:
     * user can choose a secret code by selecting a colour for each peg (max 6 colours)
     * */
    private void createSecretCode() { //replace with code to show pegs that can be chosen by user and button to let computer to guess
    	System.out.println("Please enter secret code"); 
    	Scanner reader = new Scanner(System.in);  // Reading from System.in

        for (int i = 0; i < secretCode.length; i++) {
        	System.out.println("Enter a number from 0 to "+ (colours-1));
        	secretCode[i] = reader.nextInt();
        }
        System.out.println("Secret code: " + Arrays.toString(secretCode));
        reader.close();
    }
    /* method returns colour for peg
        * 0 = red
        * 1 = greev
        * 2 = blue
        * 3 = yellow
        * 4 = pink
        * 6 = orange
     */
    private static Color getColor(int i){
          if (i==0) return Color.RED;
          if (i==1) return Color.GREEN;             
          if (i==2) return Color.BLUE;
          if (i==3) return Color.YELLOW;
          if (i==4) return Color.PINK;
          else return Color.ORANGE;   
        }
    /*
     * Method to generate random guess code by computer
     *
     * */
    private void computerRandomGuess(){
    	Random random = new Random();
        	for (int j = 0; j<colours; j++){
        		computerCode[j] = random.nextInt(colours);
        	}
    }
    /*
     * Method to check passed guess code
     * this method returns for each guess:
     *  number of black pegs equal to pegs with correct colour in correct positions
     *  number of white pegs equal to pegs with correct colours but not in correct positions
     * */
    private void check(int[] guess){
    	blackPegs = 0;
    	whitePegs = 0;
    	//check if guess contains correct colour in correct position
    	for(int i=0; i<colours; i++){
    		if(guess[i] == secretCode[i]){
    				blackPegs++;
    			}
    		}
    	//check if guess contains correct colour but not in the correct position
    	ArrayList<Integer> temp = new ArrayList<>();
    	for(int colour: secretCode){
    		for(int j=0; j<colours; j++){
    			if(colour == guess[j] && !temp.contains(j)){
    				temp.add(j);
    				whitePegs++;
    				break;
    			}
    		}
    	}
    	whitePegs -= blackPegs;
    }


    public static void main(String[] args){
    	new myMastermind(10,4,6);
    }

}
