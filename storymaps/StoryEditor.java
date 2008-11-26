package storymaps;

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
    
    public StoryEditor(StoryMap storyMap) {    
        this.storyMap = storyMap;
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        scrollPane = new JScrollPane(panel);
        Messager.getMessager().accept("StoryMap changed", this, null);
    }

    private void updateFunctions() {
        panel.removeAll();
        for (StoryCard s : storyMap.getStoryCards()) {
            FunctionEditor e = s.getEditor();
            panel.add(e.getComponent());
        }
        scrollPane.validate();
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
