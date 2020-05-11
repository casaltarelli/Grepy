package builders;

import structures.FiveTuple;

/** 
 * Created By: Christian Saltarelli
 * Date: 04-25-2020
 * Project: Grepy
 * File: Builder.java
 * 
 * Builder is used as a model for our NFABuild, 
 * DFABuild, and DOTBuild files. Enforces 
 * Structure with Children being used 
 * for Building NFA, DFA Definitions, and
 * created our DOT Files
 */

public abstract class Builder {
    public FiveTuple tuple; 

    Builder(FiveTuple def) {
        this.tuple = def;
    }

    public abstract void define();
}