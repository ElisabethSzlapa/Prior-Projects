package Hint;

import boggle.*;

import java.util.*;
import java.util.Set;

public class Hint {
    private Hint_function hint_type;

    public Hint(Hint_function hint_type) {
        this.hint_type = hint_type;
    }

    public String hint() {
        return this.hint_type.hint();
    }
}

