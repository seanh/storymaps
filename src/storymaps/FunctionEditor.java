package storymaps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import storymaps.ui.Fonts;

/** 
 * @author seanh
 */
class FunctionEditor {

    private Function function;
    private JPanel panel = new JPanel();
    private JPanel subpanel = new JPanel();
    private JLabel name;
    private JLabel image;
    private JEditorPane instructions;
    private JTextArea editor;
            
    public FunctionEditor(Function function) {
        this(function,"");
    }
      
    public FunctionEditor(Function function, String text) {
        this.function = function;
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        panel.setLayout(flowLayout);
        //panel.setBackground(Color.WHITE);
        BorderLayout borderLayout = new BorderLayout();
        subpanel.setLayout(borderLayout);
        subpanel.setBackground(Color.WHITE);
        subpanel.setBounds(0, 0, 10, 10);
        
        name = new JLabel(function.getName());
        name.setFont(Fonts.LARGE);
        subpanel.add(name,BorderLayout.NORTH);
        
        ImageIcon imageIcon = new ImageIcon(function.getImage(), "Illustration for function.");
        image = new JLabel(imageIcon);
        subpanel.add(image,BorderLayout.CENTER);
        
        instructions = new JEditorPane("text/html","<html>"+function.getDescription()+"<br/>"+function.getInstructions()+"</html>");        
        instructions.setEditable(false);
        instructions.setPreferredSize(new Dimension(350,350));
        instructions.setFont(Fonts.NORMAL);
        subpanel.add(instructions,BorderLayout.SOUTH);

        panel.add(subpanel);
        
        editor = new JTextArea(6,45);
        editor.setLineWrap(true);
        editor.setWrapStyleWord(true);
        editor.setText(text);
        editor.setFont(Fonts.LARGE);
        JScrollPane scrollPane = new JScrollPane(editor);
        panel.add(scrollPane);
    }
        
    public JComponent getComponent() {
        return panel;
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
        // FIXME: this closes a paragraph at points where it should just put a
        // <br/> (when the user has manually gone onto the next line, but has
        // not left an empty line).
        String plainText = editor.getText();
        String html = "";
        int mode = 0;
        for (String part : plainText.split("\n")) {
            if (mode == 0) {
                html = html + "<p>";
                mode = 1;
            } else if (mode == 1) {
                html = html + "</p> <p>";
                mode = 0;
            }
            html = html + part;
        }
        html = html + "</p>";
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