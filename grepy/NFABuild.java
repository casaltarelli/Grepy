
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
    public String expression; // Regular Expression

    public int curState;    // Current State
    public int prevState;   // Previous State
    
    public int befState;    // Before State (Special Case)
    public boolean loopFlag = false;
    public String prevChar;

    public int acceptState; 

    NFABuild(FiveTuple def, String exp) {
        super(def); // Record Five Tuple Definition
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
     * - Helper function to develop()
     *   moves our current Character marker,
     *   depending on case
     */
    public void defineHelper(String updated, String prev) {
        this.expression = updated;

        if (this.expression.length() >= 2) {                        // General Case
            define();
        } else if (this.expression.length() == 1) {                 // Base Case
            CharObj cur = new CharObj(this.expression.charAt(0));   // Last Character

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
            addAccepting();
            System.out.println("NFA Computation Complete");

        } else {
            //Update Accepting Set
            addAccepting();
            System.out.println("NFA Computation Complete");
        }
    }

    /**
     * handleChar(CharObj, CharObj)
     * - Handles all cases that could happen 
     *   from when current Char is a 
     *   Character type. 
     * 
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
                    paranFlag = true; 

                    addState();
                    addDelta(this.prevState, cur.value, this.curState);
                    this.befState = this.curState;
                    break out;

                } else if (nxt.value.equals("+")) {
                    this.prevChar = cur.value;
                    break out;

                } else if (nxt.value.equals("*") && this.loopFlag) {
                    addDelta(this.curState, cur.value, this.befState);
                    this.curState = this.befState; // Update Cur State

                    // Reset Flags + Increase Index
                    index = index + 1; // Skip Next Char
                    this.loopFlag = false; 
                    paranFlag = false; 

                    break out;  

                } else { // nxt.val.equals("*") && this.loopFlag == false
                    addDelta(this.curState, cur.value, this.curState);
                    index = index + 1; // Skip Next Char
                    break out;
                }
                break;

            default:
                System.out.println("Error: Invalid Character Type");
                break;
        }

        // After Processing
        if (paranFlag) {
            String newExp = getSubstring(); // Remove Parans
            defineHelper(newExp, cur.value);
        } else {
            String sub = this.expression.substring(index, this.expression.length()); // i + 1
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
                index = index + 1; // Skip Next Char
                break;

            case "(":
                paranFlag = true;
                this.befState = this.curState;
                break;

            default:
                System.out.println("Error: Invalid Character Type");
                break;
        }

        // After Processing
        if (paranFlag) {
            String newExp = getSubstring(); // Remove Parans
            defineHelper(newExp, cur.value);
        } else {
            String sub = this.expression.substring(index, this.expression.length()); // i + 1
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
                        if (j != this.expression.length() - 1) { // Check if Last Char
                            sub = this.expression.substring(i + 1, j) + this.expression.substring(j + 1);

                            if (this.expression.charAt(j+1) == '*') {
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
     * AddState()
     * - Updates our PrevState and CurState
     *   markers, also creates new state 
     *   for tuple definition
     */
    public void addState() {
        // Update Previous + Cur
        this.prevState = this.curState;
        this.curState = this.tuple.addState();
    }

    /**
     * addDelta() 
     * - Creates new delta transition for 
     *   tuple definition
     */
    public void addDelta(int on, String ch, int nxt) {
        this.tuple.addDelta(on, ch, nxt);
        this.acceptState = nxt; // Update Accept Value
    }

    /**
     * addAccepting()
     * - Adds accepting state/s to our 
     *   tuple definition
     */
    public void addAccepting() {
        this.tuple.addAccepting(this.acceptState);
    }
}