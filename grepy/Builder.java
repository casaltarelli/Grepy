
/** 
 * Created By: Christian Saltarelli
 * Date: 04-25-2020
 * Project: Grepy
 * File: Builder.java
 * 
 * Builder is used as a model for our NFABuild 
 * and DFABuild files. Enforces Structure with 
 * Children being used for Building NFA and 
 * DFA Definitions
 */

public abstract class Builder {
    public FiveTuple tuple; 

    public Builder(FiveTuple def) {
        this.tuple = def;
    }

    public abstract void define();
}