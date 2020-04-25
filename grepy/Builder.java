
/** 
 * Created By: Christian Saltarelli
 * Date: 04-25-2020
 * Project: Grepy
 * 
 * Builder is used as a model for our NFABuild 
 * and DFABuild files. Enforces Structure with 
 * Children being used for Building NFA and 
 * DFA Definitions
 */

public abstract class Builder {
    public FiveTuple definition; 

    public Builder(FiveTuple def) {
        this.definition = def;
    }
}