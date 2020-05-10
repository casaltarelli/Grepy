import java.util.ArrayList;
import java.util.Arrays;

/** 
 * Created By: Christian Saltarelli
 * Date: 05-2-2020
 * Project: Grepy
 * File: DFABuild.java
 * 
 * Extension of Builder, used to generate
 * our DFA Definition from our NFA
 * Definition
 */

 public class DFABuild extends Builder {
     FiveTuple nfa;

     String curState;
     String errorState;
     String curChar;


    DFABuild(FiveTuple dfa, FiveTuple nfa) {
        super(dfa);
        this.nfa = nfa;
    }

    /**
    * define() 
    * - Used to fully define our FiveTuple object.
    *   For each Char in our Alphabet search our 
    *   NFA Definition to Create and Find all 
    *   needed States & Delta Transitions
    */
    @Override public void define() {
        int index = 0;

        while (index <= this.tuple.getStates().size() - 1) {
            curState = this.tuple.getStates().get(index);                               // Assign CurState

            for (int i = 0; i < this.tuple.getAlphabet().size(); i++) {                 // Iterate over Alphabet
                curChar = this.tuple.getAlphabet().get(i);
                defineHelper();
            }

            index++;
        }

        addState(errorState);                                                           // Fully Define Error State in Tuple
        createError();                                                                  

        addAccepting();                                                                 // Add Accepting States

        System.out.println("DFA Computation Complete");
    }

    /**
     * defineHelper() 
     * - Helper Function to define(), handles finding all
     *   Delta transitions and Creating any needed States
     *   for our DFA Definition
     */
    public void defineHelper() {
        ArrayList<TransitionObj> transitions = getTransitions(); 

        String nxtState = findNext(transitions);

        if (nxtState.equals("")) {                                                      // Error State Transition
            addDelta(curState, curChar, errorState);                                    

        } else {
            if (this.tuple.getStates().contains(nxtState)) {                            // Create Transition
                addDelta(curState, curChar, nxtState);

            } else {                                                                    // Create New State + Transition
                addState(nxtState);                                
                addDelta(curState, curChar, nxtState);
            } 
        }  
    }

    /**
     * getTransitions() : ArrayList<TransitionObj>
     * - Finds all NFA State transitions based on our
     *   current DFA State (curState) + Current Character
     *   from our alphabet (curChar)
     */
    public ArrayList<TransitionObj> getTransitions() {
        ArrayList<TransitionObj> transitions = new ArrayList<TransitionObj>();          // Record all Matched Transitions

        // Breakdown CurState to find NFA States
        ArrayList<String> curStates = getStates();

        for (int i = 0; i < curStates.size(); i++) {
            TransitionObj trans = new TransitionObj(curStates.get(i));                  // Current NFA State

            // Find Delta Transitions matching our Current NFA State and CurChar
            for (int j = 0; j < this.nfa.getDelta().size(); j++) {
                String[] temp = this.nfa.getDelta().get(j);
                
                if (temp[0].equals(curStates.get(i)) && temp[1].equals(curChar)) {
                    trans.addDelta(temp);                                               // Record Transition
                }
            }

            // Add Trans Object to our Transitions
            transitions.add(trans);
        }

        return transitions;
    }


    /**
     * getStates() : ArrayList<String>
     * - Gets all NFA States from our CurState,
     *   and returns a List of each state.
     *
     * e.g "q1q2q3" = ["q1", "q2", "q3"]
     */
    public ArrayList<String> getStates() {
        ArrayList<String> curStates = new ArrayList<String>();                          // List for States
        String states = curState;

        while (states.length() > 1) {
            out: 
            for (int i = 1; i < states.length(); i++) {
                if (String.valueOf(curState.charAt(i)) == "q") {                        //Marks Next State
                    curStates.add(states.substring(0, i - 1));
                    states = states.substring(i); 

                    break out;
                } else if (i == states.length() - 1) {                                  // Last State
                    curStates.add(states.substring(0));
                    states = " ";

                    break out;
                }
            }
        }

        return curStates;
    }

    /**
     * findNext(ArrayList<TransitionObj>) : String
     * - From our matched NFA transitions, record all NFA 
     *   Next States and combine them to create our Next 
     *   DFA State
     */
    public String findNext(ArrayList<TransitionObj> transitions) {
        String nxt = "";

        for (int i = 0; i < transitions.size(); i++) {
            for (int j = 0; j < transitions.get(i).delta.size(); j++) {
                if (nxt.contains(transitions.get(i).delta.get(j)[2])) {
                    continue;
                } else {
                    nxt = nxt + transitions.get(i).delta.get(j)[2];                     // Add New Accept State
                }
            }
        }

        if (nxt.equals("") && errorState == null) {   
            errorState = "q" + (this.nfa.getStates().size() + 1);                       // Create Error State                                                           
        }

        return nxt;
    }

    /**
     * createError()
     * - Creates an Error State with
     *   Delta Transitions for the
     *   current DFA Definition
     */
    public void createError() {
        // Create Delta Definitions
        for (int i = 0; i < this.tuple.getAlphabet().size(); i++) {   
            String temp = this.tuple.getAlphabet().get(i);
            addDelta(errorState, temp, errorState);                                     // Create Loop per Char
        }
    }

    /**
     * addState(String)
     * - Adds our next State to our FiveTuple
     *   definition
     */
    public void addState(String state) {
        this.tuple.getStates().add(state);
    }

    /**
     * addDelta() 
     * - Creates a new delta transition 
     *   for our FiveTuple definition
     */
    public void addDelta(String on, String ch, String nxt) {
        String[] transition = new String[] {on, ch, nxt};
        this.tuple.getDelta().add(transition);
    }

    /**
     * addAccepting()
     * - Find all occurances of our NFA
     *   accepting State in our DFA States
     *   and update Accepting for our
     *   FiveTuple definition
     */
    public void addAccepting() {
        ArrayList<String> accepting = this.nfa.getAccept();

        for (String state : this.tuple.getStates()) {
            out:
            for (String accept : accepting) {
                if (this.tuple.getAccept().contains(state)) {
                    break out;                                          
                } else {
                    if (state.contains(accept)) {
                        this.tuple.addAccepting(this.tuple.getStates().indexOf(state) + 1); // Add State
                    }
                }
            }
        }
    }
 }