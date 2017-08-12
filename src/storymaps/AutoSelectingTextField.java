
/* 
    Copyright: (c) 2006-2012 Sean Hammond <seanhammond@seanh.cc>

    This file is part of Storymaps.

    Storymaps is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Storymaps is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Storymaps.  If not, see <http://www.gnu.org/licenses/>.

*/
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