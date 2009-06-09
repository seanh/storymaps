package storymaps;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.List;
import java.util.ArrayList;

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
     * The root panel of this function editor.
     */
    private JPanel panel;

    /**
     * The editor component where the user types.
     */
    private JTextArea editor;

    FunctionEditor(StoryCard s) {
        this(s,"");
    }
      
    FunctionEditor(StoryCard s, String text) {
        this.function = s.getFunction();
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        Border title = BorderFactory.createTitledBorder(function.getName());
        Border empty = BorderFactory.createEmptyBorder(10,10,10,10);
        panel.setBorder(BorderFactory.createCompoundBorder(title,empty));

        JPanel west = new JPanel();
        JLabel image = makeImage();
        west.add(image);
        panel.add(west,BorderLayout.WEST);
        JPanel center = new JPanel();
        center.setLayout(new BorderLayout());
        center.add(makeInstructions(center.getBackground()),BorderLayout.NORTH);
        center.add(makeEditor(text),BorderLayout.CENTER);
        panel.add(center,BorderLayout.CENTER);
    }
    
    private JLabel makeImage() {
        ImageIcon imageIcon = new ImageIcon(function.getImage(),function.getName());
        JLabel image = new JLabel(imageIcon);
        return image;        
    }
        
    private JTextArea makeEditor(String text) {
        JTextArea _editor = new JTextArea();
        _editor.setLineWrap(true);
        _editor.setWrapStyleWord(true);
        _editor.setText(text);
        _editor.setFont(Fonts.LARGE);
        _editor.setBorder(BorderFactory.createLineBorder(Color.black));
        return _editor;
    }
    
    private JTextPane makeInstructions(Color background) {
        JTextPane instructions = new JTextPane();
        instructions.setContentType("text/html");
        String text = "<h1>"+function.getName()+"</h1>";
        text = text + function.getDescription();
        text = text + function.getInstructions();
        instructions.setText(text);
        instructions.setEditable(false);
        instructions.setBackground(background);
        return instructions;
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

    static class FunctionEditorTest {
        private final JFrame frame = new JFrame(getClass().getName());
        private final Container contentPane = frame.getContentPane();
        private final List<FunctionEditor> functionEditors = new ArrayList<FunctionEditor>();
        private JComponent currentComponent;
        private int i = 0;

        FunctionEditorTest() {
            frame.setPreferredSize(new Dimension(600,400));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            for (Function f : Function.getFunctions()) {
                FunctionEditor fe = new FunctionEditor(new StoryCard(f));
                functionEditors.add(fe);
            }

            currentComponent = functionEditors.get(0).getComponent();
            contentPane.add(currentComponent,BorderLayout.CENTER);

            JButton nextButton = new JButton("Next");
            nextButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    i++;
                    if (i>=functionEditors.size()) { i=0; }
                    JComponent nextComponent = functionEditors.get(i).getComponent();
                    contentPane.remove(currentComponent);
                    contentPane.add(nextComponent,BorderLayout.CENTER);
                    contentPane.validate();
                    contentPane.repaint();
                    currentComponent = nextComponent;
                }
            });
            contentPane.add(nextButton,BorderLayout.SOUTH);

            frame.pack();
            frame.setVisible(true);
        }
    }

    public static void main(String[] args) {
        new FunctionEditorTest();
    }
}