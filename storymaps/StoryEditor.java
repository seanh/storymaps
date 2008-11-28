package storymaps;

import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author seanh
 */
public class StoryEditor {

    private JPanel panel;
    private JScrollPane scrollPane;
    private ArrayList<FunctionEditor> editors = new ArrayList<FunctionEditor>();
    
    public StoryEditor() {    
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        scrollPane = new JScrollPane(panel);
    }

    /**
     * Update the list of FunctionEditors in this StoryEditor.
     */
    public void update(ArrayList<StoryCard> new_cards) {
        // Remove all the FunctionEditors from the panel, then add the new ones.
        ArrayList<FunctionEditor> new_editors = new ArrayList<FunctionEditor>();        
        panel.removeAll();        
        for (StoryCard s : new_cards) {
            FunctionEditor e = s.getEditor();
            new_editors.add(e);
            panel.add(e.getComponent());
        }
        panel.invalidate();
        panel.doLayout();
        
        // Find the first new FunctionEditor that has just been added, and focus
        // it.
        for (FunctionEditor e : new_editors) {
            if (!editors.contains(e)) {
                focus(e);
                break;
            }
        }
        
        // Update the list of FunctionEditors.
        editors = new_editors;        
    }
        
    /**
     * Return the root component of this StoryEditor, i.e. the component that
     * should be added to a contentPane to add this StoryEditor to a JFrame.
     */
    public JComponent getComponent() {
        return scrollPane;
    }
    
    /**
     * Scroll so that the FunctionEditor belonging to StoryCard s is in view,
     * and give it the keyboard focus.
     */
    public void focus(StoryCard s) {
        FunctionEditor f = s.getEditor();
        assert editors.contains(f) : "The FunctionEditor must be one that the StoryEditor contains";
        focus(f);
    }
    
    /**
     * Scroll so that FunctionEditor f is in view, and give f the keyboard
     * focus.
     */
    private void focus(FunctionEditor f) {
        JComponent jc = f.getComponent();
        Rectangle r = jc.getBounds();
        panel.scrollRectToVisible(r);
        f.focus();        
    }
}