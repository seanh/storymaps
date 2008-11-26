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
        this.function = function;
        panel = new JPanel();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        panel.setLayout(flowLayout);
        ImageIcon icon = new ImageIcon(function.image, "Illustation for function.");
        label = new JLabel(icon);
        panel.add(label);
        textArea = new JTextArea(7,66);
        JScrollPane scrollPane = new JScrollPane(textArea);                    
        panel.add(scrollPane);
    }
    
    public JComponent getComponent() {
        return panel;
    }
}