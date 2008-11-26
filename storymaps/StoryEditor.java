package storymaps;

import java.awt.Rectangle;
import java.util.HashSet;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author seanh
 */
public class StoryEditor implements Receiver {

    private StoryMap storyMap;
    private JPanel panel;
    private JScrollPane scrollPane;
    private HashSet<FunctionEditor> editors = new HashSet<FunctionEditor>();
    
    public StoryEditor(StoryMap storyMap) {    
        this.storyMap = storyMap;
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        scrollPane = new JScrollPane(panel);
        Messager.getMessager().accept("StoryMap changed", this, null);
    }

    private void updateFunctions() {
        HashSet<FunctionEditor> new_editors = new HashSet<FunctionEditor>();
        
        for (StoryCard s : storyMap.getStoryCards()) {
            FunctionEditor e = s.getEditor();
            new_editors.add(e);
            panel.add(e.getComponent());
        }
        panel.invalidate();
        panel.doLayout();
        for (FunctionEditor e : new_editors) {
            if (!editors.contains(e)) {
                JComponent jc = e.getComponent();
                Rectangle r = jc.getBounds();
                panel.scrollRectToVisible(r);
                e.focus();
                break;
            }
        }        
        editors = new_editors;        
    }
        
    /**
     * Return the root component of this StoryEditor, i.e. the component that
     * should be added to a contentPane to add this StoryEditor to a JFrame.
     */
    public JComponent getComponent() {
        return scrollPane;
    }

    public void receive(String name, Object receiver_arg, Object sender_arg) {
        if (name.equals("StoryMap changed")) {
            updateFunctions();
        }
    }       
}