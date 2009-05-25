package storymaps;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import storymaps.ui.Fonts;

import java.io.IOException;
import java.util.logging.Logger;

/** 
 * 
 * @author seanh
 */
class FunctionEditor {

    /**
     * Message that FunctionEditor sends via Messager when helpButton is
     * pressed.
     */
    static final String HELP_MESSAGE = "FunctionEditor.HELP_MESSAGE";

    /**
     * The Propp function that this FunctionEditor represents.
     */
    private Function function;
       
    /**
     * The root JPanel of this function editor.
     */
    private JPanel editorPanel;
    
    /**
     * The text editor where the user enters her text for this Propp function.
     */
    private JTextArea editor;
    
    FunctionEditor(Function function) {
        this(function,"");
    }
      
    FunctionEditor(Function function, String text) {
        this.function = function;
        editorPanel = makeEditorPanel(text);
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
        JTextArea editor = new JTextArea(6,30); // Hiding a field.
        editor.setLineWrap(true);
        editor.setWrapStyleWord(true);
        editor.setText(text);
        editor.setFont(Fonts.LARGE);
        editor.setBorder(BorderFactory.createLineBorder(Color.black));
        return editor;
    }
    
    private JEditorPane makeInstructions(Color background) {
        JEditorPane instructions = new JEditorPane("text/html",
                "<html>"+function.getInstructions()+"</html>");
        instructions.setEditable(false);
        instructions.setBackground(background);
        instructions.setFont(Fonts.NORMAL);
        //instructions.setPreferredSize(new Dimension(400,0));
        return instructions;
    }
        
    private JPanel makeEditorPanel(String text) {
        JPanel editorPanel = new JPanel(); // Hiding a field.
        editorPanel.setLayout(new BoxLayout(editorPanel,BoxLayout.Y_AXIS));
        
        JLabel name = makeName();
        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        editorPanel.add(name);                
        
        JPanel panel = new JPanel();        
        panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBackground(editorPanel.getBackground());
        JLabel image = makeImage();
        image.setAlignmentY(Component.TOP_ALIGNMENT);
        panel.add(image);

        JPanel anotherPanel = new JPanel();
        anotherPanel.setLayout(new BoxLayout(anotherPanel,BoxLayout.Y_AXIS));
        anotherPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        
        JEditorPane description = makeDescription(editorPanel.getBackground());
        description.setAlignmentX(Component.LEFT_ALIGNMENT);
        anotherPanel.add(description);
        
        JButton helpButton = new JButton("Help");
        helpButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        try {
            helpButton.setIcon(Util.readImageIconFromClassPath("/data/icons/help.png"));
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).warning("Couldn't read icon for help button. "+e.toString());
        }
        helpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // Show the help dialog.
                Messager.getMessager().send(HELP_MESSAGE,this);
            }
        });
        helpButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        anotherPanel.add(helpButton);

        
        panel.add(anotherPanel);
        editorPanel.add(panel);

        editor = makeEditor(text);
        JScrollPane editorScrollPane = new JScrollPane(editor);
        editorScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        editorPanel.add(editorScrollPane);
        
        return editorPanel;
    }
    
    JPanel makeHelpPanel() {                                        
        JPanel helpPanel = new JPanel(new BorderLayout());
        helpPanel.add(makeName(),BorderLayout.PAGE_START);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel,BoxLayout.Y_AXIS));
        centerPanel.add(makeImage());
        JEditorPane instructions = makeInstructions(centerPanel.getBackground());
        JScrollPane scrollPane = new JScrollPane(instructions);
        scrollPane.setBorder(null);
        centerPanel.add(scrollPane);
        helpPanel.add(centerPanel,BorderLayout.CENTER);
        helpPanel.setOpaque(true);
        return helpPanel;        
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