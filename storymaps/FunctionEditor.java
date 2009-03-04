package storymaps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import storymaps.ui.Fonts;

/** 
 * @author seanh
 */
public class FunctionEditor {

    private Function function;
    private JPanel panel = new JPanel();
    private JPanel subpanel = new JPanel();
    private JLabel title;
    private JLabel icon;
    private JLabel desc;
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
        
        title = new JLabel(function.getFriendlyName());
        title.setFont(Fonts.LARGE);
        subpanel.add(title,BorderLayout.NORTH);        
        ImageIcon imageIcon = new ImageIcon(function.getImage(), "Illustation for function.");
        icon = new JLabel(imageIcon);
        subpanel.add(icon,BorderLayout.CENTER);
        desc = new JLabel("<html>"+function.getFriendlyDescription()+"</html>");
        desc.setPreferredSize(new Dimension(350,200));
        desc.setFont(Fonts.NORMAL);
        subpanel.add(desc,BorderLayout.SOUTH);
        
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