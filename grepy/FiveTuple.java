
/** 
 * Created By: Christian Saltarelli
 * Date: 04-18-2020
 * Project: Grepy
 * 
 * FiveTuple is used to represent our NFA and DFA machines.
 * Holds all data used to define these automatas. As well as ablity to 
 * create a NFA defintion based on a Regular Expression.
 */

public class FiveTuple {
    // Five Tuple Attributes
    ArrayList<String> states = new ArrayList<String>();
    ArrayList<String> alphabet = new ArrayList<String>();
    ArrayList<String[]> delta = new ArrayList<String[]>();
    String start = new String();
    ArrayList<String> acceptingStates = new ArrayList<String>();
}