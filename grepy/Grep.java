import java.util.regex.*;
import java.io.File;
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

        // Check + Assign File Names
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
            // Check for start + end symbols
            if (exp.charAt(0) == '^' && exp.charAt(exp.length() - 1) == '$') {
                System.out.println("Expression Formatted Correctly");

                // Remove start + end symbols
                exp = exp.substring(1, exp.length() - 1);
                userData[0] = exp;
                validating = false;

            } else {
                System.out.println("Invalid Regular Expression. Please try again. [e.g ^(01)*1$]");

                exp = input.next();
                System.out.println("You entered: " + exp);
                
            }
        }

        input.reset();
        return true; // Marks Complete Execution
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
            // Check input file exists
            if (userFile.exists()) {
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
        return true; // Marks Complete Execution
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
             throw new IllegalArgumentException("Fatel Error: Please enter needed arguments");
         }

        // Iterate Through User Input - Collect User Data
        for (int i = 0; i < args.length; i++) {
            // Check for NFA/DFA Option
            if (userData[2] == null || userData[3] == null) {
                if (checkFileName(args[i])) {
                    continue;
                }
            }
            
            // Get Regular Expression
            if (userData[0] == null) {
                if (validateRegex(args[i])) {
                    continue;
                } 
            }

            // Get File Name
            if (userData[1] == null) {
                if (validateFile(args[i])) {
                    continue; 
                }
            }     
        }

        // Create FiveTuple + Compute Definition
        FiveTuple nfa = new FiveTuple(userData[1]);
        NFABuild nfaDef = new NFABuild(nfa, userData[0]);
        nfaDef.define();

        // TEST: Output NFA Tuple Definition
        System.out.println(nfaDef.tuple.toString());

        // TEST: Output Collected Data
        for (int i = 0; i < userData.length; i++) {
            System.out.println(userData[i]);
        }
    }
}