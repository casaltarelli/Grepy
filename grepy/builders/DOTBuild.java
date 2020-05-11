package builders;

import java.io.File;
import java.io.IOException;
import java.lang.InterruptedException;
import java.io.FileWriter;
import java.lang.Process;
import java.lang.Runtime;
import structures.FiveTuple;

/** 
 * Created By: Christian Saltarelli
 * Date: 05-10-2020
 * Project: Grepy
 * File: DOTBuild.java
 * 
 * Extension of Builder, used to generate
 * DOT files representing FiveTuple
 * Definitions
 */

public class DOTBuild extends Builder {
    String file;
    String type;

    public DOTBuild(FiveTuple tuple, String file, String type) {
        super(tuple);
        this.file = file;
        this.type = type;
    }

    /**
     * define()
     * - Used to create a .dot file from 
     *   a FiveTuple Definition and given File Name
     */
    @Override public void define() {
        defineHelper();                                                                 // Validate User Input

        try {
            File f = new File(this.file);

            if (f.createNewFile()) {
                System.out.println("File " + this.file + " Created");
            } else {
                System.out.println("File already exists. Will be overwritten");
            }

            FileWriter writer = new FileWriter(f);

            writer.write("digraph "                                                     // DOT Definition Start
                + this.type + " {" 
                + System.getProperty( "line.separator" )
                + "\trankdir=LR"
                + System.getProperty( "line.separator"));

            for (String accept : this.tuple.getAccept()) {                              // Set Style for all Accept States
                writer.write("\t" + accept + " [color=blue]" + System.getProperty( "line.separator" ));
            }

            for (String[] trans : this.tuple.getDelta()) {
                writer.write("\t" + trans[0] + " -> " + trans[2] + " [label=" + trans[1] + "]");
                writer.write(System.getProperty( "line.separator" ));
            }

            writer.write("}");                                                          // DOT Definition End

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * defineHelper()
     * - Helper function to define(). Checks if 
     *   user had given a filename and validates
     *   the proper extension for the file
     */
    public void defineHelper() {
        this.type = this.type.toUpperCase();

        if (this.file == null || this.file.equals("")) {                                // Default File Name
            this.file = this.type + "Graph.dot";
        } else {                                                                        // Check for Extension
            int lastIndex = this.file.lastIndexOf(".");

            if (lastIndex == -1) {
                this.file = this.file + ".dot";   
            } else {
                String sub = this.file.substring(lastIndex);

                if (!(sub.equals(".dot"))) {
                    this.file = this.file.substring(0, lastIndex) + "dot";
                } 
            } 
        }
    }

    /**
     * toPNG()
     * - Used to take a created 
     *   .dot file and creates 
     *   a new .png Image
     */
    public void toPNG() {
        try {
            File f = new File(this.file); 
            String PNGFile = this.file.substring(0, this.file.length() - 4) + ".png";   // Change Extension

            String[] e = {"dot", "-Tpng", f.getAbsolutePath(), "-o", PNGFile};          // dot Command
            Process p = Runtime.getRuntime().exec(e); 
            int err = p.waitFor(); 

        } catch(IOException e1) {
            e1.printStackTrace();
        } catch(InterruptedException e2) {
            e2.printStackTrace();
        }

        System.out.println("PNG Created");
    }
}