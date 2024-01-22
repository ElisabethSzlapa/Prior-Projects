package Hint;

import boggle.*;

import java.util.*;
import java.util.Set;

public class Hint {
    private Hint_function hint_type;

    /**
     * initialize the hint type
     * @param hint_type Hint_function
     */
    public Hint(Hint_function hint_type) {
        this.hint_type = hint_type;
    }

    /**
     * call hint function according the type of hint
     * @return String
     */
    public String hint() {
        return this.hint_type.hint();
    }
}

