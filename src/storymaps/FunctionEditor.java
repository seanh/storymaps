
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
import javax.swing.border.*;
import storymaps.ui.Fonts;

/** 
 * 
 * @author seanh
 */
class FunctionEditor {

    /**
     * The Propp function that this FunctionEditor represents.
     */
    private Function function;
       
    /**
     * The root JPanel of this function editor.
     */
    private JComponent editorPanel;
    
    /**
     * The text editor where the user enters her text for this Propp function.
     */
    private JTextArea editor;
    
    FunctionEditor(StoryCard s) {
        this(s,"");
    }
      
    FunctionEditor(StoryCard s, String text) {
        this.function = s.getFunction();
        editorPanel = makeEditorPanel(s,text);
    }

    private JLabel makeName() {
        JLabel name = new JLabel(function.getName());
        name.setFont(Fonts.LARGE);
        return name;
    }
    
    private JLabel makeImage() {
        ImageIcon imageIcon = new ImageIcon(function.getImage(),function.getName());
        JLabel image = new JLabel(imageIcon);
        return image;        
    }
    
    private JEditorPane makeDescription(Color background) {
        JEditorPane description = new JEditorPane("text/html",
                "<html>"+function.getDescription()+"</html>");
        description.setEditable(false);
        description.setBackground(background);
        //description.setPreferredSize(new Dimension(100,100));
        return description;
    }
    
    private JTextArea makeEditor(String text) {
        JTextArea editor = new JTextArea(); // Hiding a field.
        editor.setRows(8);
        editor.setMaximumSize(new Dimension(650,100));
        editor.setLineWrap(true);
        editor.setWrapStyleWord(true);
        editor.setText(text);
        editor.setFont(Fonts.LARGE);
        editor.setBorder(BorderFactory.createLineBorder(Color.black));
        return editor;
    }
    
    private JTextPane makeInstructions(Color background) {
        JTextPane instructions = new JTextPane();
        instructions.setContentType("text/html");
        instructions.setText("<html>"+function.getInstructions()+"</html>");
        instructions.setEditable(false);
        instructions.setBackground(background);
        instructions.setFont(Fonts.NORMAL);
        instructions.setMaximumSize(new Dimension(650,100));
        return instructions;
    }
        
    private JComponent makeEditorPanel(StoryCard s, String text) {
        JPanel editorPanel = new JPanel(); // Hiding a field.
        editorPanel.setLayout(new BoxLayout(editorPanel,BoxLayout.X_AXIS));

        JLabel storyCard = new JLabel(new ImageIcon(s.getImage()));
        storyCard.setAlignmentY(Component.TOP_ALIGNMENT);
        editorPanel.add(storyCard);

        JPanel innerPanel = new JPanel();
        innerPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        innerPanel.setLayout(new BoxLayout(innerPanel,BoxLayout.Y_AXIS));
        innerPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JEditorPane description = makeDescription(innerPanel.getBackground());
        description.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPanel.add(description);

        JEditorPane instructions = makeInstructions(innerPanel.getBackground());
        instructions.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPanel.add(instructions);

        editor = makeEditor(text);
        editor.setAlignmentX(Component.LEFT_ALIGNMENT);
        innerPanel.add(editor);

        editorPanel.add(innerPanel);

        JScrollPane scrollPane = new JScrollPane(editorPanel);
        Border title = BorderFactory.createTitledBorder(function.getName());
        Border empty = BorderFactory.createEmptyBorder(10,10,10,10);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(title,empty));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        return scrollPane;
    }
                    
    public JComponent getComponent() {
        return editorPanel;
    }
    
    public Function getFunction() {
        return function;
    }
    
    public String getText() {
        return editor.getText();
    }
    
    /**
     * Return the user's text from the JTextArea decorated with HTML <br/> and
     * <p> </p> tags, so that line-breaks and paragraphs are preserved when the
     * text is written out to HTML.
     * 
     */
    public String getTextAsHTML() {
        String plainText = editor.getText();
        String html = "";
        for (String paragraph : plainText.split("\n\n")) {
            html = html + "<p>" + paragraph.replace("\n", "<br/>") + "</p>";
        }
        return html;
    }
    
    public void focus() {
        editor.requestFocusInWindow();
    }

    /**
     * Return true if obj is equivalent to this function editor, false
     * otherwise.
     */
    public boolean compare(Object obj) {
        if (!(obj instanceof FunctionEditor)) {
            return false;
        } else {
            FunctionEditor f = (FunctionEditor) obj;
            if (!(f.getFunction().compare(getFunction()))) { return false; }
            if (!(f.getText().equals(getText()))) { return false; }
            return true;
        }        
    }
}