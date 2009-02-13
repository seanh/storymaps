package storymaps;
import storymaps.ui.Fonts;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author seanh
 */
public class StoryEditor {

    /**
     * The root panel of the StoryEditor, to which the button and document
     * panels are added.
     */
    private JPanel root_panel;
    
    /**
     * Button that collapses/uncollapses the story editor.
     */
    private JButton collapse_button;
    
    /**
     * Whether or not the story editor is collapsed.
     */
    private boolean collapsed = true;
    
    /**
     * The document panel contains the FunctionEditors for each function in the
     * story. FunctionEditors are dynamically added and removed as the story map
     * is changed.
     */
    private JPanel document_panel;
    
    /**
     * ScrollPane that contains the document panel.
     */
    private JScrollPane scrollPane;
    
    /**
     * The title of the story.
     */
    private JTextField title = new JTextField("Enter your story's title here.");
    
    /**
     * The FunctionEditors currently on the document panel.
     */
    private ArrayList<FunctionEditor> editors = new ArrayList<FunctionEditor>();
        
    public StoryEditor() {    
        root_panel = new JPanel();
        root_panel.setLayout(new BorderLayout());
                
        document_panel = new JPanel();
        document_panel.setLayout(new BoxLayout(document_panel, BoxLayout.PAGE_AXIS));                        

        title.setVisible(false);
        title.setFont(Fonts.HUGE);
        root_panel.add(title,BorderLayout.NORTH);
        
        scrollPane = new JScrollPane(document_panel);        
        root_panel.add(scrollPane,BorderLayout.CENTER);
        scrollPane.setVisible(false);// The document panel starts off collapsed.
        
        collapse_button = new JButton("Click here to write your story...");
        collapse_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                collapse();
            }
        });
        root_panel.add(collapse_button,BorderLayout.SOUTH);        
        root_panel.setPreferredSize(new Dimension(1024,25));
    }
    
    private void collapse() {
        if (collapsed) {
            collapsed = false;
            root_panel.setPreferredSize(new Dimension(1024,384));
            title.setVisible(true);
            scrollPane.setVisible(true);            
            collapse_button.setText("Hide story");
            root_panel.getParent().validate();
            root_panel.getParent().repaint();
            Messager.getMessager().send("Editor uncollapsed", this);
        } else {
            collapsed = true;
            root_panel.setPreferredSize(new Dimension(1024,25));
            title.setVisible(false);
            scrollPane.setVisible(false);
            collapse_button.setText("Click here to write your story...");
            root_panel.getParent().validate();
            root_panel.getParent().repaint();
            Messager.getMessager().send("Editor collapsed", this);
        }
    }

    /**
     * Update the list of FunctionEditors in this StoryEditor.
     */
    public void update(ArrayList<StoryCard> new_cards) {
        // Remove all the FunctionEditors from the panel, then add the new ones.
        ArrayList<FunctionEditor> new_editors = new ArrayList<FunctionEditor>();        
        document_panel.removeAll();        
        for (StoryCard s : new_cards) {
            FunctionEditor e = s.getEditor();
            new_editors.add(e);
            document_panel.add(e.getComponent());
        }
        document_panel.validate();
        document_panel.doLayout();
        
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
        return root_panel;
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
        document_panel.scrollRectToVisible(r);
        f.focus();        
    }
    
    public boolean getCollapsed() {
        return collapsed;
    }
    
    public String getTitle() {
        return title.getText();
    }
    
    public void setTitle(String title) {
        this.title.setText(title);
    }
}