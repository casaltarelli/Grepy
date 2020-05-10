import java.io.File;
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
    File input;

    Processor(FiveTuple def, String file) {
        this.tuple = def;
        this.input = new File(file);
    }

    public void process() {
        
    }
}