package structures;

/** 
 * Created By: Christian Saltarelli
 * Date: 04-25-2020
 * Project: Grepy
 * File: CharObj.java
 * 
 * CharObj is used for quick reference
 * of value and character type for
 * our limited syntax
 * 
 * e.g Char [a-z, 0-9] or
 * Special [(, ), *, +]
 */

public class CharObj {
    public String value;
    public String type;

    public CharObj (char val) {
        this.value = Character.toString(val);
        getType();
    }

    public void getType() {
        // Cast Value to Char for Check
        char ch = this.value.charAt(0);

        // Character Check
        if (ch >= 'a' && ch <= 'z' || ch >= '0' && ch <= '9') {
            this.type = "Character";
        } else if (ch == ')' || ch == '(' || ch == '*' || ch == '+') {
            this.type = "Special";
        } else {
            this.type = "null";
        }
    }
}