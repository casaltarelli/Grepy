package structures;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/** 
 * Created By: Christian Saltarelli
 * Date: 04-25-2020
 * Project: Grepy
 * File: FiveTuple.java
 * 
 * FiveTuple is used to represent our NFA and DFA machines.
 * Holds all data used to define the automatas
 */

public class FiveTuple {
    // Five Tuple Attributes
    private ArrayList<String> states = new ArrayList<String>();
    private ArrayList<String> alphabet = new ArrayList<String>();
    private ArrayList<String[]> delta = new ArrayList<String[]>();
    private String start = new String();
    private ArrayList<String> acceptingStates = new ArrayList<String>();

    public FiveTuple(String inputFile) {
        this.states.add("q1");                                                          // Create Start State
        this.start = states.get(0);

        readAlphabet(inputFile);                                                        // Define Alphabet
    }

    /**
     * readAlphabet(String)
     * - Reads all lines from input File to Learn
     *   the expected Alphabet + Populates alphabet
     */
    public void readAlphabet(String file) {
        try {
            Scanner input = new Scanner(new File(file));

            while (input.hasNextLine()) {
                String line = input.nextLine();

                for (int i = 0; i < line.length(); i++) {
                    String temp = Character.toString(line.charAt(i));
                    
                    if (this.getAlphabet().contains(temp)) {                            // Check if Char has Been Recorded
                        continue;                                                       
                    } else {                                                            // Append Otherwise
                        this.getAlphabet().add(temp);                                   
                    }
                }
            }
            input.close();                                                              // Close Scanner

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter & Adder Functions
     * - Allows for Reading of Five Tuple
     *   Definition + Adding to Definition
     */
    public ArrayList<String> getStates() {
        return this.states;
    }

    public ArrayList<String> getAlphabet() {
        return this.alphabet;
    }

    public ArrayList<String[]> getDelta() {
        return this.delta;
    }

    public String getStart() {
        return this.start;
    }

    public ArrayList<String> getAccept() {
        return this.acceptingStates;
    }

    public int addState() {
        int number = this.getStates().size() + 1;
        this.getStates().add("q" + number);

        return number;
    }

    public void addDelta(int on, String ch, int nxt) {
        String[] transition = new String[] {"q" + on, ch, "q" + nxt};
        this.getDelta().add(transition);
    }

    public void addAccepting(int index) {
        String accepting = this.getStates().get(index - 1);
        this.acceptingStates.add(accepting);
    }

    /**
     * toString() : String
     * - Creates String of full 
     *   Five Tuple definition
     */
    public String toString() {
        String output = "{";

        output = output 
            + this.getStates().toString() + ", " + System.getProperty("line.separator")
            + this.getAlphabet().toString() + ", " + System.getProperty("line.separator") + "{";

        for (int i = 0; i < this.getDelta().size(); i++) {
            output = output + Arrays.toString(this.getDelta().get(i));

            if (i < this.getDelta().size() - 1) {
                output = output + ", ";
            }
        }

        output = output + "},"  + System.getProperty("line.separator") 
            + "[" + start + "]," + System.getProperty("line.separator") 
            + "{" + this.getAccept().toString() + "}}";
        
        return output;
    }
}