// Mastermind game by Mateusz Janusz (mjanu001@gold.ac.uk)
// 	March/April 2017

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.Random;
import java.util.Arrays;

public class Mastermind extends JFrame  implements ActionListener {
        
	int width;
	int height;
	int numColors;
	int numGuesses;
	JButton[][] colouredPegs; 
	JButton[][] whites;       
	JButton[][] blacks;       
	JButton[] computerGuess;
	int state[][];
	int[] hiddenGuess;
	JButton guessButton = new JButton("Start guessing");
	JPanel colouredPanel = new JPanel();
	JPanel whitesPanel = new JPanel();
	JPanel blacksPanel = new JPanel();
	JPanel computerGuessPanel = new JPanel();
    //JPanel panel2 = new JPanel();
	Random rand;

	/* method to check and return number of black pegs 
		(the guess which is correct in both color and position)
	*/
	static int blacks (int [] one, int [] two){
		int val=0;
		for (int i=0;i<one.length;i++){
		  if (one[i]==two[i]) val++;
		}
	    return val;
	}
	/* method to check and return number of white pegs 
		(the existence of a correct color code peg placed in the wrong position)
	*/
	static int whites (int [] one, int [] two){
		boolean found;
		int val=0;
		int [] oneA = new int[one.length]; 
		int [] twoA = new int[one.length];

		for (int i=0;i<one.length;i++){
			oneA[i]=one[i]; twoA[i]=two[i];
		}
		for (int i=0;i<one.length;i++){
			if (oneA[i]==twoA[i]) {
			oneA[i]=0-i-10;
			twoA[i]=0-i-20;
			}
		}
		
		for (int i=0;i<one.length;i++){ 
			found=false;
			  for (int j=0;j<one.length && !found;j++){ //check one by one
				   if (i!=j && oneA[i]==twoA[j]){
				   	val++;
				   	oneA[i]=0-i-10;
				   	twoA[j]=0-j-20;
				   	found=true;
				   }
			  }	
	    }
		return val;
	}

	/* method returns colour for peg
	    * 0 = red
	    * 1 = greev
	    * 2 = blue
	    * 3 = yellow
	    * 4 = pink
	    * 6 = orange
     */
	static Color choose(int i){
		  if (i==0) return Color.RED;
		  if (i==1) return Color.GREEN; 			
		  if (i==2) return Color.BLUE;
		  if (i==3) return Color.YELLOW;
		  if (i==4) return Color.PINK;
		  else return Color.ORANGE;	  
		}
/*	
	 GAME CONSTRUCTOR
	 specifying number of chances to guess, number of pegs and different colours	
*/
	public Mastermind(int max_guesses, int pegs, int colours) {
		width=pegs;
		height=max_guesses; 
		numColors=colours;
		numGuesses=0;
		rand = new Random();
		// this.setSize(new Dimension(300,50*height));
		hiddenGuess = new int[width]; //create array for secret guess
		state = new int[height][width];
		/* create buttons */
		colouredPegs = new JButton[height][width];
		whites = new JButton[height][width];
		blacks = new JButton[height][width];
		computerGuess = new JButton[width];
		/* set grid layout for all panels */
		colouredPanel.setLayout(new GridLayout(height,width));
		blacksPanel.setLayout(new GridLayout(height,width));
		whitesPanel.setLayout(new GridLayout(height,width));
		computerGuessPanel.setLayout(new GridLayout(1,width));
		/* set up padding */
		colouredPanel.setBorder(BorderFactory.createEmptyBorder(20, 20,
		20, 20));
		whitesPanel.setBorder(BorderFactory.createEmptyBorder(20, 20,
		20, 20));
		blacksPanel.setBorder(BorderFactory.createEmptyBorder(20, 20,
		20, 20));

		/* class to implement event to change colour background 
			it uses bing to set values 
			using mod (%) it keeps values in range from 0 to maximum number of available colours
		*/
		class bing implements ActionListener{   
			int x,y;
			boolean secret;
		   	
			public void actionPerformed(ActionEvent e) {
				if(secret==false){ //events for guesses
					state[x][y]=(state[x][y]+1)%numColors; 
			  		System.out.println("GUESS: level: " + x + " button: " +y + " colour: " + state[x][y]);
			  		((JButton)(e.getSource())).setBackground(choose(state[x][y]));
				} else {	//events for secret code 
					state[x][y]=(state[x][y]+1)%numColors; 
					hiddenGuess[x]=state[x][y];
			  		((JButton)(e.getSource())).setBackground(choose(state[x][y]));
			  		System.out.println("SECRET: button: "+hiddenGuess[x]+ " colour: " + state[x][y]);

				}	  
			}
			//bing constructor 
		    public bing (int p, int q, boolean isSecret){ 
		    	x=p;
		    	y=q;
		    	secret=isSecret;
		    } 
		}
		
		for (int k = 0; k < width; k++){
		  hiddenGuess[k]=0; //set red bg by default
		  computerGuess[k]= new JButton(); 
		  computerGuess[k].setVisible(true);
		  computerGuess[k].setOpaque(true);
		  computerGuess[k].setBackground(choose(hiddenGuess[k]));  
		  computerGuess[k].addActionListener(new bing(k, 0, true));
		  computerGuessPanel.add(computerGuess[k]);
		}
		
		for (int i = 0; i < height; i++) 
			for (int j = 0; j < width; j++)
			 {
			   System.out.println(i+" "+j);
			   state[i][j]=0; //set red bg by default
			   colouredPegs [i][j]= new JButton();
			   colouredPegs [i][j].addActionListener(new bing(i,j, false));
			   colouredPegs [i][j].setBackground(choose(state[i][j]));
			   colouredPegs [i][j].setOpaque(true);

			   whites [i][j]= new JButton();
			   whites [i][j].setVisible(false);
			   whites [i][j].setBackground(Color.white);
			   whites [i][j].setOpaque(true);	

			   blacks [i][j]= new JButton();
			   blacks [i][j].setVisible(false);
			   blacks [i][j].setBackground(Color.black);
			   blacks [i][j].setOpaque(true);

			   colouredPanel.add(colouredPegs [i][j]); 	
			   whitesPanel.add(whites [i][j]);
			   blacksPanel.add(blacks [i][j]);
			   if (i>0)
			   		colouredPegs[i][j].setVisible(false); 
			 }
		  
		/* create window layout: panels, then positions, text, its elements/actions  */
		setLayout(new BorderLayout());
		add(blacksPanel, "West");
		add(colouredPanel, "Center");
		add(whitesPanel, "East");
		JPanel guessPanel = new JPanel();
		guessPanel.setLayout(new FlowLayout());
		guessPanel.add(guessButton);
		add(guessPanel,"South");
		JPanel topPanel = new JPanel();
		topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20,
		20, 20));
		topPanel.setLayout(new GridLayout(1,3));
		topPanel.add(new JLabel("Blacks",JLabel.CENTER));
		topPanel.add(new JLabel("Set secret code",JLabel.CENTER));
		topPanel.add(computerGuessPanel);
		topPanel.add(new JLabel("Whites",JLabel.CENTER));
		add(topPanel,"North");
		setDefaultCloseOperation(3); //specify one of several options for the close button
		setTitle("Mastermind");
		setMinimumSize(new Dimension(width*50,height*50));
		pack(); // sizes the frame so that all its contents are at or above their preferred sizes
		setVisible(true); 
		guessButton.addActionListener(this); //adds click listener to guess button
	}
	//events for guess button
	public void actionPerformed(ActionEvent e) {

		for(int g=0; g<height;g++){

			if(g==0){
				//ask what method to use
				Object[] options = { "Naive method", "Cleverer method"};
				int d = JOptionPane.showOptionDialog(this,
							"Which method would you like to use?", "",
							JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
							null, options, null);

				if (d == JOptionPane.NO_OPTION) {
					guessClever(g);
				} else { 
					guessNaive();
				} 
			}
			 

			System.out.println("secret code: " + Arrays.toString(hiddenGuess));
			System.out.println("guess code: " + Arrays.toString(state[numGuesses]));

			int whiteThings=whites(state[numGuesses],hiddenGuess); // get number of white pegs 
			int blackThings=blacks(state[numGuesses],hiddenGuess); // get number of black pegs

			for (int i=0;i<width;i++){
				colouredPegs[numGuesses][i].setEnabled(false);
				computerGuess[i].setEnabled(false); // don't allow to change secret code anymore
			}
			

			if (blackThings==width){  //no more chances to guess left 
				for (int i=0;i<blackThings;i++)
					blacks[numGuesses][i].setVisible(true); 
				for (int i=0;i<width;i++) {
			  		computerGuess[i].setOpaque(true);
					computerGuess[i].setVisible(true);
				}
					
				int n = JOptionPane.showConfirmDialog(this,
						"You've Won! Would you like to play again?", "",
						JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.NO_OPTION) {
					System.exit(0);
				} else { 
					dispose(); /* causes the JFrame window to be destroyed and cleaned up by the operating system
									not like system.exit which causes the Java VM to terminate completely. */
					new Mastermind(height,width,numColors); //create new window game again
				}
			}
			if (numGuesses<height){ //there is still a number of chances to guess 
			  for (int i=0;i<whiteThings;i++) 
			  	whites[numGuesses][i].setVisible(true); //show white pegs if any 
			  for (int i=0;i<blackThings;i++)
			  	blacks[numGuesses][i].setVisible(true); //show black pegs

			  numGuesses++; 
			  if (numGuesses<height) {
				  	for (int i=0;i<width;i++){
				  		colouredPegs[numGuesses][i].setOpaque(true);
				  		colouredPegs[numGuesses][i].setVisible(true);
				  	}

				  } else { 
					  	for (int i=0;i<width;i++) {
					  		computerGuess[i].setOpaque(true);
					  		computerGuess[i].setVisible(true);
					  	}
						int n = JOptionPane.showConfirmDialog(this,
								"You've lost! Would you like to play again?", "",
								JOptionPane.YES_NO_OPTION);
						if (n == JOptionPane.NO_OPTION) {
							System.exit(0); //terminate completely
					} else { 
					dispose(); //destroy and clean up the window
					new Mastermind(height,width,numColors); //create window game again
					}
			  }
		   }
	   }		
	}
/* 
	cleverer method to guess secret code 
*/
	private void guessClever(int attempt){
		System.out.println("guessing using Naive method...");
		int pos = 0;
		int col = 0;
		int counter = 0;
		int b = blacks(state[attempt], hiddenGuess); //check number of colours in the right position
		while(b<4){
			counter++;
			state[attempt][pos]=col; //choose colour for the button
			int db = blacks(state[attempt], hiddenGuess); 
			if(db>b){ //check now if there is a match, if yes then move to next button
				colouredPegs [attempt][pos].setBackground(choose(state[attempt][pos])); //show correct guess on buttons
				pos++;	
			}
			//else try again
			col = (col+1)%numColors; //keep colour in range 
			b = blacks(state[attempt], hiddenGuess); 
		}
		System.out.println("Number of attempts: " + counter);

	} 
/* 
	naive method to guess secret code that ignores blacks and white
*/
	private void guessNaive(){
		System.out.println("guessing using Knuth method...");
		for(int i=0;i<width;i++) 
			state[numGuesses][i]=rand.nextInt(numColors); //random for now
	}
	
	
	public static void main(String[] args) {
		new Mastermind(10,4,6); // chances, pegs, colours
	}

	
}
