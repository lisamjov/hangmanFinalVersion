package hangman;

import java.util.Random;
import java.util.Scanner;

//it's so hard to write comments in other people's code, but we tried our best.

/**
*@auther ...Raluca? and us(modifiers - Lisa, Agustina and Gagan)
*version 08.03.2016
*/
public class Game {
    private static final String[] wordForGuesing = {"computer", "programmer",
            "software", "debugger", "compiler", "developer", "algorithm",
            "array", "method", "variable"};

    private String guesWord;

    private StringBuffer dashedWord;
    private FileReadWriter filerw;

    /**
    *Constructor
    *@param autostart a boolean value??
    */
    public Game(boolean autoStart) {
        guesWord = getRandWord();
    
        dashedWord = getW(guesWord);
        filerw = new FileReadWriter();
        if (autoStart) {
            displayMenu();
        }
    }

    /**
    *@return method returns a random strin/word
    */
    private String getRandWord() {
        Random rand = new Random();
        String randWord = wordForGuesing[rand.nextInt(9)];
        return randWord;
    }

    /**
    *This method prints out the menu and calls the method findLetter() to find the letter
    */
    public void displayMenu() {
        System.out
                .println("Welcome to �Hangman� game. Please, try to guess my secret word.\n"
                        + "Use 'TOP' to view the top scoreboard, 'RESTART' to start a new game,"
                        + "'HELP' to cheat and 'EXIT' to quit the game.");

        findLetter();
    }
    /**
    *Finds the secret word. Checks if enterd value is valid. Counts how many times the help was used.
    *it aslo calls the relevant method to print the secret word and the winner
    */
    private void findLetter() {
        boolean isHelpUsed = false;
        String letter = "";
        StringBuffer dashBuff = new StringBuffer(dashedWord);
        int mistakes = 0;


        do {
            System.out.println("The secret word is: " + printDashes(dashBuff));
            System.out.println("Enter your gues(1 letter alowed): ");
            Scanner input = new Scanner(System.in);
            letter = input.next(); // letter guessed by the user/player

            if (letter.equals(Command.help.toString())) {
                isHelpUsed = true;
                int i = 0, j = 0;
                while (j < 1) {

                    if (dashBuff.charAt(i) == '_') {
                        dashBuff.setCharAt(i, guesWord.charAt(i));
                        ++j;
                    }
                    ++i;
                }
                System.out.println("The secret word is: "
                        + printDashes(dashBuff));
            }// end if
            menu(letter);

            while (!letter.matches("[a-z]")) ;

            int counter = 0;
            for (int i = 0; i < guesWord.length(); i++) {
                String currentLetter = Character.toString(guesWord.charAt(i));
                if (letter.equals(currentLetter)) {

                    {
                        ++counter;
                    }
                    dashBuff.setCharAt(i, letter.charAt(0));
                }
            }

            if (counter == 0) {
                ++mistakes; //mistakes incresed
                {
                    System.out.printf(
                            "Sorry! There are no unrevealed letters \'%s\'. \n",
                            letter);
                }
            } else {

                System.out.printf("Good job! You revealed %d letter(s).\n",
                        counter);
            }

        }

        while (!dashBuff.toString().

                equals(guesWord)

                );

        //if help was not used, it prints the name og the winner with total mistakes used
        if (isHelpUsed == false) {

            String playerName = print(mistakes, dashBuff);

         {
            //process for writing to file and update scoreboard
             filerw.openFileToWite();
             filerw.addRecords(mistakes, playerName);
             filerw.closeFileFromWriting();
             filerw.openFiletoRead();
             filerw.readRecords();
             filerw.closeFileFromReading();
             filerw.printAndSortScoreBoard();
         }

         // if help was used; reveals the letter but user will not be asked for her/his name for scoreboard
        } else {

            printHelpUsed(mistakes, dashBuff);

        }

        // restart the game
        new

                Game(true);

    }// end method

    /**
    *method prints the secret word
    *@param i= total mistakes, sb = secret word
    */
    /*this method is used if the player DID use help. */
    private void printHelpUsed(int i, StringBuffer sb) {

        System.out
                .println("You won with "
                        + i
                        + " mistake(s). but you have cheated. You are not allowed to enter into the scoreboard.");
        System.out.println("The secret word is: " + printDashes(sb));
    }//end method

    /**
    *method prints the correct word and name of the winner given by the user/player
    *@param i = tatal mistakes, sb = the secret word
    *@return it returs the winner(playerName)
    */

    /*this method is used to revel the print 
    the secret word is the user did NOT use the help*/
    private String print(int i, StringBuffer sb) {

        System.out.println("You won with " + i + " mistake(s).");
        System.out.println("The secret word is: " + printDashes(sb));

        System.out.println("Please enter your name for the top scoreboard:");
        Scanner input = new Scanner(System.in);
        String playerName = input.next();

        return playerName; //returns the playerName given by the user

    }

    /**
    *@param letter, the letter enterd by the user/player
    */
    
    private void menu(String letter) {
        if (letter.equals(Command.restart.toString())) {
            new Game(true); //new game wil be started if the word was found
        } else {
            if (letter.equals(Command.top.toString())) {
                filerw.openFiletoRead();
                filerw.readRecords();
                filerw.closeFileFromReading();
                filerw.printAndSortScoreBoard();
                new Game(true);
            } else {
                if (letter.equals(Command.exit.toString())) {
                    System.exit(1);
                }
            }
        }
    }

    /**
    *@param word, the secret word
    *@return it returns a stringbuffer - dashes
    *method prints retuns dashes(total=word.length)
    */
    private StringBuffer getW(String word) {
        StringBuffer dashes = new StringBuffer("");

        for (int i = 0; i < word.length(); i++) {
            dashes.append("_");
        }
        return dashes; 
    }

    /**
    *@param word, the secret word
    *@return return a string with dashes
    */

    //this method prints the dashes with entered letters by the user/player
    private String printDashes(StringBuffer word) {
        String toDashes = "";

        for (int i = 0; i < word.length(); i++) {

             //the stringbuffer wil be modified here
            toDashes += (" " + word.charAt(i));
        }
        return toDashes;

    }
}
