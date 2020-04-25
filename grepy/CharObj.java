

/** 
 * Created By: Christian Saltarelli
 * Date: 04-25-2020
 * Project: Grepy
 * 
 * CharObj is used for quick reference
 * of value and character type for
 * our limited syntax.
 * 
 * e.g Char [a-z, 0-9] or
 * Special [(), *, +]
 */

public class CharObj {
    String value;
    String type;

    CharObj (String val, String ty) {
        this.value = val;
        this.type = ty;
    }
}