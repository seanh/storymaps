package storymaps;

import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author seanh
 */
public class FunctionEditor {

    private JPanel panel;
    private JLabel label;
    private JTextArea textArea;
    private Function function;
    
    public FunctionEditor(Function function) {
        this(function,"");
    }
    
    public FunctionEditor(Function function, String text) {
        this.function = function;
        panel = new JPanel();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        panel.setLayout(flowLayout);
        ImageIcon icon = new ImageIcon(function.getImage(), "Illustation for function.");
        label = new JLabel(icon);
        panel.add(label);
        textArea = new JTextArea(6,45);
        textArea.setLineWrap(true);
        textArea.setText(text);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane);
    }
        
    public JComponent getComponent() {
        return panel;
    }
    
    public Function getFunction() {
        return function;
    }
    
    public String getText() {
        return textArea.getText();
    }
    
    public void focus() {
        textArea.requestFocusInWindow();
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

    public static class Memento {
        public Object function_memento;
        public String text;
        public Memento(Object function_memento, String text) {
            this.function_memento = function_memento;
            this.text = text;
        }
        @Override
        public String toString() {
            String string = "<div class='FunctionEditor'>\n";
            string += this.function_memento.toString();
            string += "<div class='user_text'>" + this.text + "</div><!--user_text-->\n";
            string += "</div><!--FunctionEditor-->\n";
            return string;
        }
    }    
    
    public Object saveToMemento() {
        return new Memento(this.function.saveToMemento(),
                this.textArea.getText());
    }
    
    /** 
     * Return a new FunctionEditor constructed from a memento object.
     */
    public static FunctionEditor newFromMemento(Object o) {
        if (!(o instanceof Memento)) {
            throw new IllegalArgumentException("Argument not instanceof Memento.");
        }
        else {
            Memento m = (Memento) o;
            Function f = Function.newFromMemento(m.function_memento);
            return new FunctionEditor(f,m.text);
        }
    }        
}