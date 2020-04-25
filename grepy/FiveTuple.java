import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/** 
 * Created By: Christian Saltarelli
 * Date: 04-25-2020
 * Project: Grepy
 * 
 * FiveTuple is used to represent our NFA and DFA machines.
 * Holds all data used to define the automatas.
 */

public class FiveTuple {
    // Five Tuple Attributes
    private ArrayList<String> states = new ArrayList<String>();
    private ArrayList<String> alphabet = new ArrayList<String>();
    private ArrayList<String[]> delta = new ArrayList<String[]>();
    private String start = new String();
    private ArrayList<String> acceptingStates = new ArrayList<String>();

    FiveTuple(String inputFile) {
        this.states.add("q1"); // Create Start State
        this.start = states.get(0);

        // Define Alphabet
        readAlphabet(inputFile);
    }

    /**
     * readAlphabet(String)
     * - Reads all lines from input File to Learn
     * the expected Alphabet + Populate alp
     * 
     */
    public void readAlphabet(String file) {
        try {
            Scanner input = new Scanner(new File(file));

            while (input.hasNextLine()) {
                // Iterate Through Each Line
                String line = input.nextLine();
                for (int i = 0; i < line.length(); i++) {
                    String temp = Character.toString(line.charAt(i));
                    
                    if (this.getAlphabet().contains(temp)) {
                        continue; // Check if Char has Been Recorded
                    } else {
                        this.getAlphabet().add(temp); // Append Otherwise
                        //printAlphabet();
                    }
                }
            }
            input.close(); // Close Scanner

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter & Adder Functions
     * - Allows for Reading of Five Tuple
     * Definition + Adding to Definition
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

    public void AddState() {
        // Find Next Proper State Number
        int number = this.getStates.size() + 1;

        // Add State
        this.getStates.add("q" + number);
    }

    public void addDelta(int on, String ch, int next) {
        // Create New Transitition
        String[] transititon = new String[] {"q" + on, ch, "q" + nxt};

        // Add Delta Definition
        this.getDelta().add(transition);
    }

    public void addAccpeting(int index) {
        String accepting = this.getStates().get(index);
        
        // Add State
        addAccpet(accepting);
    }

    public void printAlphabet() {
        for (String temp : this.getAlphabet()) {
            System.out.print(temp);
        }
        System.out.println();
    }
}