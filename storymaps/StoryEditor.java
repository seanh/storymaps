package storymaps;
import storymaps.ui.Fonts;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.text.DefaultEditorKit;

/**
 *
 * @author seanh
 */
public class StoryEditor implements Receiver {

    /**
     * The root panel of the StoryEditor, to which the document panel is added.
     */
    private JPanel root_panel;
        
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
     * The toolbar that contains the Cut, Copy, Paste, Save etc. buttons.
     */
    private JToolBar toolBar;
    
    /**
     * The title of the story.
     */
    private JTextField title = new JTextField("Enter your story's title here.");
    
    /**
     * The FunctionEditors currently on the document panel.
     */
    private ArrayList<FunctionEditor> editors = new ArrayList<FunctionEditor>();
    
    /**
     * The JFrame in which this StoryEditor will be used.
     */
    private JFrame parent;
    
    public StoryEditor(JFrame parent) {
        this.parent = parent;
        
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
        
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);        
        root_panel.add(toolBar,BorderLayout.SOUTH);

        addButton("Cut","/storymaps/icons/cut.png",new DefaultEditorKit.CutAction());
        addButton("Copy","/storymaps/icons/copy.png",new DefaultEditorKit.CopyAction());
        addButton("Paste","/storymaps/icons/paste.png",new DefaultEditorKit.PasteAction());
                       
        root_panel.setPreferredSize(new Dimension(parent.getWidth(),0));
        
        Messager.getMessager().accept("button clicked",this,null);
    }
    
    private void addButton(String text, String imagePath, Action action) {
        JButton button = new JButton(action);
        button.setText(text);
        try {
            ImageIcon icon = Util.readImageIconFromFile(imagePath);
            button.setIcon(icon);
        } catch (IOException e) {
            // If we can't read an icon file we don't report it to the user,
            // just create a button with text and no icon.
        }        
        button.setVerticalTextPosition(AbstractButton.BOTTOM);
        button.setHorizontalTextPosition(AbstractButton.CENTER);        
        toolBar.add(button);        
    }
            
    private void collapse() {
        if (collapsed) {
            collapsed = false;
            root_panel.setPreferredSize(new Dimension(parent.getWidth(),parent.getHeight()/2));
            title.setVisible(true);
            scrollPane.setVisible(true);            
            root_panel.getParent().validate();
            root_panel.getParent().repaint();
            Messager.getMessager().send("Editor uncollapsed", this);
        } else {
            collapsed = true;
            root_panel.setPreferredSize(new Dimension(parent.getWidth(),0));
            title.setVisible(false);
            scrollPane.setVisible(false);
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

    public void receive(String name, Object receiver_arg, Object sender_arg) {
        if (name.equals("button clicked")) {
            if ( ((String)sender_arg).equals("Write Story") ) {
                collapse();
            }
        }
    }
}