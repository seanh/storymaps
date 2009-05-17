package storymaps;

import java.awt.*;
import javax.swing.*;
import storymaps.ui.Fonts;

/** 
 * @author seanh
 */
class FunctionEditor {

    private Function function;

    /**
     * The root JPanel of this function editor.
     */
    private JPanel root = new JPanel();

    /**
     * The name of the Propp function.
     */
    private JLabel name;

    /**
     * The image of the Propp function.
     */
    private JLabel image;

    /**
     * The description of the Propp function.
     */
    private JEditorPane description;

    /**
     * The textarea where the user enters her text for this function.
     */
    private JTextArea editor;

    private JEditorPane instructions;


    public FunctionEditor(Function function) {
        this(function,"");
    }
      
    public FunctionEditor(Function function, String text) {
        this.function = function;

        root.setLayout(new BorderLayout());

        JPanel cardAndEditor = new JPanel();
        cardAndEditor.setLayout(new BoxLayout(cardAndEditor,BoxLayout.Y_AXIS));

        instructions = new JEditorPane("text/html","<html>"+function.getInstructions()+"</html>");
        instructions.setEditable(false);
        instructions.setBackground(root.getBackground());
        instructions.setFont(Fonts.NORMAL);
        instructions.setPreferredSize(new Dimension(400,150));
        JScrollPane scrollPane = new JScrollPane(instructions);
        root.add(scrollPane,BorderLayout.WEST);

        JPanel card = new JPanel();        
        card.setLayout(new BoxLayout(card,BoxLayout.X_AXIS));
        card.setBackground(root.getBackground());
        ImageIcon imageIcon = new ImageIcon(function.getImage(), "Illustration for function.");
        image = new JLabel(imageIcon);
        image.setAlignmentY(Component.TOP_ALIGNMENT);
        card.add(image);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        name = new JLabel(function.getName());
        name.setFont(Fonts.LARGE);
        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(name);
        description = new JEditorPane("text/html","<html>"+function.getDescription()+"</html>");
        description.setEditable(false);
        description.setBackground(root.getBackground());
        description.setPreferredSize(new Dimension(100,100));
        description.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(description);
        card.add(panel);
        
        cardAndEditor.add(card);

        editor = new JTextArea(6,30);
        editor.setLineWrap(true);
        editor.setWrapStyleWord(true);
        editor.setText(text);
        editor.setFont(Fonts.LARGE);
        editor.setBorder(BorderFactory.createLineBorder(Color.black));
        JScrollPane editorScrollPane = new JScrollPane(editor);
        cardAndEditor.add(editorScrollPane);

        root.add(cardAndEditor,BorderLayout.CENTER);
    }
        
    public JComponent getComponent() {
        return root;
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