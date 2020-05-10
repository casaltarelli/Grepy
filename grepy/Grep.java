import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** 
 * Created By: Christian Saltarelli
 * Date: 04-18-2020
 * Project: Grepy
 * File: Grep.java
 * 
 * A Java application to search a given file and compare its
 * data against a given regular expression. This file allows for 
 * the user to use all functionality. While delivering an output
 * of accepted strings and written NFA & DFA files in DOT Language
 * Format
 */

public class Grep {
    // Fomatted User Data: [Regular Expression, Input File, NFA File, DFA File]
    static String[] userData = new String[4];

    /**
     * checkFileName(String) : Boolean
     * - Handles NFA + DFA File name option and allocation
     */
    public static boolean checkFileName(String arg) {
        boolean fileStatus = false;

        if (arg.contains("-n")) {
            userData[2] = arg.substring(2, arg.length()).replaceAll("\\s+","");
            fileStatus = true;
        
        } else if (arg.contains("-d")) {
            userData[3] = arg.substring(2, arg.length()).replaceAll("\\s+","");
            fileStatus = true;
        }   

        return fileStatus; 
   }

   /**
     * validateRegex(String) : Boolean
     * - Handles Regular Expression validation +
     *   invalid input handling
     */
    public static boolean validateRegex(String arg) {
        boolean validating = true;
        Scanner input = new Scanner(System.in);
        input.useDelimiter(System.lineSeparator());

        String exp = arg;

        while (validating) {
            if (exp.charAt(0) == '^' && exp.charAt(exp.length() - 1) == '$') {          // Check for start + end symbols
                System.out.println("Expression Formatted Correctly");

                exp = exp.substring(1, exp.length() - 1);                               // Remove start + end symbols
                userData[0] = exp;
                validating = false;

            } else {
                System.out.println("Invalid Regular Expression. Please try again. [e.g ^(01)*1$]");

                exp = input.next();
                System.out.println("You entered: " + exp);
                
            }
        }

        input.reset();
        return true;                                                                    // Marks Complete Execution
    }  

    /**
     * validateFile(String)
     * - Handles Input File validation +
     *   invalid input handling
     */
    public static boolean validateFile(String arg) {
        boolean validating = true;
        Scanner input = new Scanner(System.in);
        input.useDelimiter(System.lineSeparator());

        String name = arg;
        File userFile = new File(name);
        
        while(validating) {
            if (userFile.exists()) {                                                    // Check input file exists
                System.out.println("File is valid");
                userData[1] = name;
                validating = false;

            } else {
                System.out.println("Invalid File Name. Please try again");
                System.out.println("e.g myfile.txt");

                name = input.next();
                userFile = new File(name);   
            }
        }

        input.close();
        return true;                                                                    // Marks Complete Execution
    }

    public static void main(String[] args) {
        /**
         * Get Terminal Arguments Expected Input: 
         * 0: NFA File Name (Optional) 
         * 1: DFA File Name (Optional) 
         * 2: Regular Expression 
         * 3: Input File Name
         */

         // Check Args
         if (args.length < 2) {
             throw new IllegalArgumentException("Fatel Error: Please enter needed arguments e.g [NFA Option, DFA Option, REGEX, File]");
         }

        // Iterate Through User Input - Collect User Data
        for (int i = 0; i < args.length; i++) {                                         // Check for NFA/DFA Option
            if (userData[2] == null || userData[3] == null) {
                if (checkFileName(args[i])) {
                    continue;
                }
            }
            
            if (userData[0] == null) {                                                  // Get Regular Expression
                if (validateRegex(args[i])) {
                    continue;
                } 
            }

            if (userData[1] == null) {                                                  // Get File Name
                if (validateFile(args[i])) {
                    continue; 
                }
            }     
        }

        // Create NFA FiveTuple + Compute Definition
        //FiveTuple nfa = new FiveTuple(userData[1]);
        NFABuild nondetermDef = new NFABuild(new FiveTuple(userData[1]), userData[0]);
        nondetermDef.define();

        // Create DFA FiveTuple + Compute Definition
        //fiveTuple dfa = new FiveTuple(userDate[1]);
        DFABuild determDef = new DFABuild(new FiveTuple(userData[1]), nondetermDef.tuple);
        determDef.define();

        // // TEST: Output NFA Tuple Definition
        // System.out.println("NFA Definition: ");
        // System.out.println(nondetermDef.tuple.toString());
        // System.out.println("");

        // // TEST: Output NFA Tuple Definition
        // System.out.println("DFA Definition: ");
        // System.out.println(determDef.tuple.toString());
        // System.out.println("");

        Processor processor = new Processor(determDef.tuple);

        try {
            processor.process(userData[1]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // TEST: DOT File Outputs
        DOTBuild dfaDot = new DOTBuild(determDef.tuple, null, "DFA");
        dfaDot.define();
        

        // TEST: Output Collected Data
        for (int i = 0; i < userData.length; i++) {
            System.out.println(userData[i]);
        }
    }
}