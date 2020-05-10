import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;

/** 
 * Created By: Christian Saltarelli
 * Date: 05-10-2020
 * Project: Grepy
 * File: Processor.java
 * 
 * Processor is used to test lines of input
 * read from a File against a given 
 * FiveTuple Definition
 */

public class Processor {
    FiveTuple tuple;

    Processor(FiveTuple def) {
        this.tuple = def;
    }

    /**
     * process(String)
     * - Reads the given file line by
     *   line and outputs all accepted Strings
     *   from FiveTuple Definition
     */
    public void process(String file) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        Scanner in = new Scanner(fis);

        while (in.hasNext()) {
            String state = this.tuple.getStart();                                       // Set Start State

            String line = in.nextLine();                                                // Grab Next Line

            for (int i = 0; i < line.length(); i++) {
                out:
                for (int j = 0; j < this.tuple.getDelta().size(); j++) {
                    String[] temp = this.tuple.getDelta().get(j);
                    
                    if (temp[0].equals(state) && temp[1].equals(Character.toString(line.charAt(i)))) {
                        state = temp[2];                                                // Update State
                        break out;
                    }
                }
            }

            for (int i = 0; i < this.tuple.getAccept().size(); i++) {                   // Check Resulting State
                if (state.equals(this.tuple.getAccept().get(i))) {
                    System.out.println("Accepted: " + line);
                }
            }
        }

        in.close();
    }
}