package structures;

import java.util.ArrayList;
import java.util.Arrays;

/** 
 * Created By: Christian Saltarelli
 * Date: 05-2-2020
 * Project: Grepy
 * File: TransitionObj.java
 * 
 * TransitionObj is used for recording
 * all delta transitions for a State
 * and Character
 * 
 * e.g  State q1
 *      Delta [[q1, 1, q1], [q1, 1, q2]] 
 */

 public class TransitionObj {
     String state;
     public ArrayList<String[]> delta = new ArrayList<String[]>();

     public TransitionObj(String state) {
         this.state = state;
     }

    /**
     * Getter & Adder Functions
     * - Allows for Reading of TransitionObj
     *   + Adding to Definition
     */
     public void setState(String state) {
         this.state = state;
     }

     public void addDelta(String[] trans) {
        this.delta.add(trans); 
     }

     public String[] getDelta(int index) {
         return delta.get(index);
     }

     public String toString() {
        String output = this.state + ": [";

        for (String[] arr : this.delta) {
            output = output + (Arrays.toString(arr));
            output = output + ", ";
        }

        output = output + "]";

        return output;
     }
 }