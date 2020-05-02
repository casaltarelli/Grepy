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
     public fiveTuple nfa;
     public String curState;
     public String curChar;

     DFABuild(FiveTuple dfa, FiveTuple nfa) {
         super(dfa); // Record Five Tuple Definition
         this.nfa = nfa;
     }


     @override public void define() {
        int index = 0;

        while (index <= this.tuple.getStates().size()) {
            curState = this.tuple.getStates().get(index);

            for (int i = 0; i < this.tuple.getAlphabet().size(); i++) {
                curChar = this.tuple.getAlphabet().get(i);
                defineHelper();
            }

            index++;
        }
     }

     public void defineHelper() {
        
     }


 }