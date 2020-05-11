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
 * Format with the option for PNG representation of those DOT Files
 */

public class Grep {
    // Fomatted User Data: [Regular Expression, Input File, NFA File, DFA File, PNG Option]
    static String[] userData = new String[5];

    /**
     * checkPNG(String)
     * - Handles PNG Option, only will check
     *   for yes otherwise defaulted to no.
     *   This can only be checked once as the
     *   first argument
     */
    public static boolean checkPNG(String arg) {
        if (arg.contains("-y")) {
            userData[4] = "yes"; 
            
            return true;
        } else {
            userData[4] = "no"; 

            return false;
        } 
    }

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
             throw new IllegalArgumentException("Fatel Error: Please enter needed arguments e.g [PNG Option, NFA Option, DFA Option, REGEX, File]");
         }

        // Iterate Through User Input - Collect User Data
        for (int i = 0; i < args.length; i++) {     
            if (userData[4] == null) {                                                  // Check for PNG Option
                if (checkPNG(args[i])) {
                    continue;
                }
            }  

            if (userData[2] == null || userData[3] == null) {                           // Check for NFA/DFA Option
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
        NFABuild nfaBuild = new NFABuild(new FiveTuple(userData[1]), userData[0]);
        nfaBuild.define();

        // Create DFA FiveTuple + Compute Definition
        DFABuild dfaBuild = new DFABuild(new FiveTuple(userData[1]), nfaBuild.tuple);
        dfaBuild.define();

        // Process Given .txt file against defined DFA
        Processor processor = new Processor(dfaBuild.tuple);

        try {
            processor.process(userData[1]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Create DOT Files for our FiveTuple Definitions
        DOTBuild nfaDOT = new DOTBuild(nfaBuild.tuple, userData[2], "NFA");
        nfaDOT.define();


        DOTBuild dfaDOT = new DOTBuild(dfaBuild.tuple, userData[3], "DFA");
        dfaDOT.define();

        // Check PNG Option
        if (userData[4].equals("yes")) {
            nfaDOT.toPNG();
            dfaDOT.toPNG();
        }
    }
}