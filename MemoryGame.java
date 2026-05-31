import javax.swing.*;
/**
 * The MemoryGame class controls logic and flow of gameplay by facilitating
 * it completely. The user is shown a sequence of letters and needs to
 * not only remember but type the sequence they were displayed. The game
 * tracks the statistics with such. Players interact with the GUI
 * to facilitate the gameplay.
 * 
 * Game allows for students to select number of buttons displayed,
 * length of memory sequence, and whether button positions are randomized.
 * 
 * This class runs the game in collaboration with MemoryGameGUI.
 */
public class MemoryGame
{

    //Correct sequence that user needs to remember
     private String [] memoryStrings;
     //The actual GUI the user interacts with to play the game
     private MemoryGameGUI gui;
     //Number of rounds played
     private int rounds;
     //Number of correct answers
     private int score;
     //Number of buttons on screen
     private int buttons;
     //Length of sequence for user
     private int sequence;

    /**
     * The MemoryGame Constructor:
     * MemoryGame Constructor actually develops the GUI and initiates
     * settings. It prompts the user to determine button randomization,
     * number of buttons, sequence length and even the GUI.
     */
    public MemoryGame(){
            //Prompts user if they want randomization
            int response = JOptionPane.showConfirmDialog(null, "Randomized Button display?", "Game Setup", JOptionPane.YES_NO_OPTION);

            boolean choice = (response == JOptionPane.YES_OPTION);

            //Initializes button variable for GUI
            buttons = 0;

            try {
                //Prompts user for how many buttons they want in GUI
                String buttonsInput = JOptionPane.showInputDialog(null, "Between 3-5, how many buttons would you like?");
                buttons = Integer.parseInt(buttonsInput);
                //Ensures buttons are in allowed range
                if (buttons < 3){
                    JOptionPane.showMessageDialog(null, "Too low! Automatic button count is 3.");
                    buttons = 3;
                }
                else if (buttons>6) {
                    JOptionPane.showMessageDialog(null, "Too high! Automatic button count is 3.");
                    buttons = 3;
                }
            }
            catch (NumberFormatException e){
                //Validate that input given by user is okay for button amt
                JOptionPane.showMessageDialog(null, "Misinput! Automatic button count is 3.");
                buttons = 3;
            }
            //Initiate the variable before randomization
            sequence = 0;

            try {
                //Asks user how long sequence should be
                String sequenceInput = JOptionPane.showInputDialog(null, "Between 3-10, how long should the sequence be?");
                sequence = Integer.parseInt(sequenceInput);
                //Ensures sequence is within allowed range
                if (sequence < 3){
                    JOptionPane.showMessageDialog(null, "Too low! Automatic sequence length is 3.");
                    sequence = 3;
                }
                else if (sequence>10) {
                    JOptionPane.showMessageDialog(null, "Too high! Automatic sequence length is 3.");
                    sequence = 3;
                }
            }
            catch (NumberFormatException e){
                //Ensures invalid inputs are handled appropriately
                JOptionPane.showMessageDialog(null, "Misinput! Automatic sequence length is 3.");
                sequence = 3;
            }


            
            //Develops GUI that will facilitate gameplay
            gui = new MemoryGameGUI();
            gui.createBoard(buttons, choice);
            rounds = 0;
            score = 0;
    }

    /**
     * The play method runs the MemoryGame project and actually facilitates
     * gameplay between user and GUI. It generates the random sequence
     * of letters, takes user input for guesses, and tracks score/rounds.
     * When game ends, final numbers are displayed.
     */
    public void play (){

        boolean isActive = true;

        do {
            
            String [] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

            int [] randomizer = RandomPermutation.next(26);
            memoryStrings = new String [sequence];
            //Generates random sequence for the gameplay
            for (int i = 0; i< sequence;i++){
                memoryStrings[i] = alphabet[randomizer[i]-1];
            }
            //Shuffles the sequence
            int [] shuffle = RandomPermutation.next(memoryStrings.length);
            String [] shuffledSequence = new String [memoryStrings.length];
            for (int i = 0; i<memoryStrings.length; i++){

                shuffledSequence [i] = memoryStrings [shuffle[i]-1];
            }   
            

            String answer = "";
            for (int i = 0; i<shuffledSequence.length;i++)
            {
                answer += shuffledSequence[i];
            }
            //Gets user input in GUI
            String guess = gui.playSequence(shuffledSequence, 0.67);
            if (guess == null){
                isActive = false;
                break;
            }
            //Cleans guess input from GUI
            String clearGuess = "";

            for (int i = 0; i<guess.length();i++){
                String currentCharacter = guess.substring(i,i+1);

                if (!(currentCharacter.equals(" ") || currentCharacter.equals(","))){
                    clearGuess += currentCharacter;
                }
            }

            if (clearGuess.equals ("")){
                JOptionPane.showMessageDialog(null, "Misinput or no input maybe?");
                rounds++;
                gui.tryAgain();

            }
            else if (clearGuess.length()>answer.length()){
                JOptionPane.showMessageDialog(null, "Input exceeds character count of answer!");
                rounds++;
                gui.tryAgain();
            }
            else if (clearGuess.equalsIgnoreCase(answer)){
                score++;
                rounds++;
                gui.matched();
            }
            else{
                gui.tryAgain();
                rounds++;
            }
        //Prompts if user wants to play again
        isActive = gui.playAgain();

        } while (isActive);

        gui.showScore(score, rounds);
        gui.quit();



    }


}
