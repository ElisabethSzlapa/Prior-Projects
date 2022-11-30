package Hint;

import org.junit.Test;

import javax.swing.plaf.PanelUI;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HintTest {

    @Test
    public void loacation(){
        Hint hint_location = new Hint(new Hint_location());
        String lo = "here is location";
        assertEquals(lo,hint_location.hint());
    }

    @Test
    public void character(){
        Hint hint_character = new Hint(new Hint_character());
        String ch = "here is character";
        assertEquals(ch, hint_character.hint());
    }

    @Test
    public void word(){
        Hint hint_word = new Hint(new Hint_word());
        String wo = "here is word";
        assertEquals(wo,hint_word.hint());
    }
}
