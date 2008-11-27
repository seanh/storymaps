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
        Messager.getMessager().accept("New focus", this, null);        
    }

    private void updateFunctions() {
        HashSet<FunctionEditor> new_editors = new HashSet<FunctionEditor>();        
        panel.removeAll();        
        for (StoryCard s : storyMap.getStoryCards()) {
            FunctionEditor e = s.getEditor();
            new_editors.add(e);
            panel.add(e.getComponent());
        }
        panel.invalidate();
        panel.doLayout();
        for (FunctionEditor e : new_editors) {
            if (!editors.contains(e)) {
                focus(e);
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
    
    private void focus(FunctionEditor f) {
        JComponent jc = f.getComponent();
        Rectangle r = jc.getBounds();
        panel.scrollRectToVisible(r);
        f.focus();        
    }

    public void receive(String name, Object receiver_arg, Object sender_arg) {
        if (name.equals("StoryMap changed")) {
            updateFunctions();
        } else if (name.equals("New focus")) {
            StoryCard s = (StoryCard) sender_arg;
            FunctionEditor f = s.getEditor();
            focus(f);
        }
    }       
}