package storymaps;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A JTextField that automatically selects all text when focused IF the text
 * it currently contains is the initial text that it was constructed with.
 * The text field also deselects the text when it loses focus.
 *
 * @author seanh
 */
class AutoSelectingTextField extends JTextField implements FocusListener {

    private String initialText;

    AutoSelectingTextField(String text) {
        super(text);
        this.initialText = text;
        addFocusListener(this);
    }

    public void focusLost(FocusEvent fe) {
        select(0,0);
    }

    public void focusGained(FocusEvent fe) {
        if (getText().equals(initialText)) {
            selectAll();
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = f.getContentPane();
        contentPane.setLayout(new FlowLayout());
        contentPane.add(new JLabel("Click on one text field then the other to focus and autoselect:"));
        contentPane.add(new AutoSelectingTextField("Enter your text here"));
        contentPane.add(new AutoSelectingTextField("Enter your text here"));
        f.pack();
        f.setVisible(true);
    }
}