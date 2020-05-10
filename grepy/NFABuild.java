
/** 
 * Created By: Christian Saltarelli
 * Date: 04-25-2020
 * Project: Grepy
 * File: NFABuild.java
 * 
 * Extension of Builder, used to generate
 * our NFA Definition from the users
 * Regular Expression
 */

public class NFABuild extends Builder {
    String expression;                                                                  // Regular Expression

    int curState;                                                                       // Current State
    int prevState;                                                                      // Previous State
    int befState;                                                                       // Before State (Special Case)
    
    int loopIndex; 
    boolean loopFlag = false;
    String prevChar;

    NFABuild(FiveTuple def, String exp) {
        super(def);
        this.expression = exp;
        this.curState = 1; 
    }

    /**
     * define() 
     * - Used to fully define our FiveTuple object
     *   reads the users regular expression 
     *   character-by-character
     */
    @Override public void define() {
        CharObj cur = new CharObj(this.expression.charAt(0)); // Current Character
        CharObj nxt = new CharObj(this.expression.charAt(1)); // Next Character

        switch(cur.type) {
            case "Character":
                handleChar(cur, nxt);
                break;
            
            case "Special":
                handleSpec(cur, nxt);
                break;

            default:
                System.out.println("Error: Invalid Character Type");
                break;
        } 
    }

    /**
     * defineHelper()
     * - Helper function to define()
     *   moves our current Character marker,
     *   depending on case
     */
    public void defineHelper(String updated, String prev) {
        this.expression = updated;

        if (this.expression.length() >= 2) {                                            // General Case
            define();
        } else if (this.expression.length() == 1) {                                     // Base Case
            CharObj cur = new CharObj(this.expression.charAt(0));

            switch(cur.type) {
                case "Character":
                    addState();
                    addDelta(this.prevState, cur.value, this.curState);
                    break;
            
                case "Special":
                    handleSpec(cur, null);
                    break;

                default:
                    System.out.println("Error: Invalid Character Type");
                    break;
            }

            //Update Accepting Set
            addAccepting(this.curState);
            System.out.println("NFA Computation Complete");

        } else {
            //Update Accepting Set
            addAccepting(this.curState);
            System.out.println("NFA Computation Complete");
        }
    }

    /**
     * handleChar(CharObj, CharObj)
     * - Handles all cases when current
     *   Character is type Character
     *   e.g a-z, 0-9
     */
    public void handleChar(CharObj cur, CharObj nxt) {
        boolean paranFlag = false;
        int index = 1;

        switch(nxt.type) {
            case "Character":
                addState();
                addDelta(this.prevState, cur.value, this.curState);
                break;
            
            case "Special":
                out: 
                if (nxt.value.equals("(")) {
                    paranFlag = true;                                                   // Update Flag

                    addState();
                    addDelta(this.prevState, cur.value, this.curState);

                    this.befState = this.curState;
                    break out;

                } else if (nxt.value.equals("+")) {
                    this.prevChar = cur.value;
                    break out;

                } else if (nxt.value.equals("*") && this.expression.indexOf("*") == this.loopIndex) {
                    addDelta(this.curState, cur.value, this.befState);
                    this.curState = this.befState;

                    // Reset Flags + Increase Index
                    index = index + 1;                                                  // Skip Next Char
                    this.loopFlag = false; 
                    paranFlag = false; 

                    break out;  

                } else { // nxt.val.equals("*") && this.loopFlag == false
                    addDelta(this.curState, cur.value, this.curState);
                    index = index + 1;                                                  // Skip Next Char
                    break out;
                }
                break;

            default:
                System.out.println("Error: Invalid Character Type");
                break;
        }

        
        if (this.loopFlag) {                                                            // Update Index for Expected Loop 
            this.loopIndex = this.loopIndex - index;                                    
        }  

        // Update Expression
        if (paranFlag) {
            String newExp = getSubstring();
            defineHelper(newExp, cur.value);
        } else {
            String sub = this.expression.substring(index, this.expression.length());
            defineHelper(sub, cur.value);
        }
    }

    /**
     * handleSpec(CharObj, CharObj)
     * - Handles all cases when current character
     *   is a special character
     *   e.g (, *, +
     */
    public void handleSpec(CharObj cur, CharObj nxt) {
        boolean paranFlag = false;
        int index = 1;

        switch(cur.value) {
            case "+":
                addState();
                addDelta(this.prevState, this.prevChar, this.curState);
                addDelta(this.prevState, nxt.value, this.curState);
                index = index + 1;                                                      // Skip Next Char
                break;

            case "(":
                paranFlag = true;
                this.befState = this.curState;
                break;

            default:
                System.out.println("Error: Invalid Character Type");
                break;
        }

        if (this.loopFlag) {
            this.loopIndex = this.loopIndex - index;                                    // Update Index for Expected Loop 
        }                                      

        // Update Expression
        if (paranFlag) {
            String newExp = getSubstring();
            defineHelper(newExp, cur.value);
        } else {
            String sub = this.expression.substring(index, this.expression.length());
            defineHelper(sub, cur.value);
        }
    }

    /**
     * getSubstring() : String
     * - Used to find the first left-most
     *   occurance of "(" and ")" while checking
     *   if "*" is the following character.
     *   Returns new substring with removed
     *   parans
     */
    public String getSubstring() {
        String sub = "";
        out:
        for (int i = 0; i < this.expression.length(); i++) {
            if (this.expression.charAt(i) == '(') {
                for (int j = i; j < this.expression.length(); j++) {
                    if (this.expression.charAt(j) == ')') {  
                        if (j != this.expression.length() - 1) {                        // Check if Last Char
                            sub = this.expression.substring(i + 1, j) + this.expression.substring(j + 1);

                            if (this.expression.charAt(j+1) == '*') {
                                this.loopIndex = j - 2;
                                this.loopFlag = true;
                            }
                            break out;

                        } else {
                            sub = this.expression.substring(i + 1, j);
                            break out;
                        }          
                        
                    }
                }
            }
        }

        return sub;
    }

    /**
     * addState()
     * - Updates our PrevState and CurState
     *   markers, also creates new state 
     *   for FiveTuple definition
     */
    public void addState() {
        // Update Previous + Cur
        this.prevState = this.curState;
        this.curState = this.tuple.addState();
    }

    /**
     * addDelta() 
     * - Creates new delta transition for 
     *   FiveTuple definition
     */
    public void addDelta(int on, String ch, int nxt) {
        this.tuple.addDelta(on, ch, nxt);
    }

    /**
     * addAccepting()
     * - Adds accepting state/s to our 
     *   FiveTuple definition
     */
    public void addAccepting(int state) {
        this.tuple.addAccepting(state);
    }
}