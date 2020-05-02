/** 
 * Created By: Christian Saltarelli
 * Date: 05-2-2020
 * Project: Grepy
 * File: TransitionObj.java
 * 
 * TransitionObj is used for recording
 * all delta transitions for a current state
 * and current character.
 * 
 * e.g  State q1
 *      Delta [[q1, 1, q1], [q1, 1, q2]] 
 */

 public class TransitionObj {
     String state;
     ArrayList<String[]> delta = new ArrayList<String[]>();

     TransitionObj() {}

     public void setState(String state) {
         this.state = state;
     }

     public void addDelta(String on, String ch, String nxt) {
        String[] transition = new String[] {"q" + on, ch, "q" + nxt};
        this.delta.add(transition); 
     }
 }