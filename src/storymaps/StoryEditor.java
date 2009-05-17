package storymaps;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author seanh
 */
class StoryEditor implements Receiver {    
    // The root panel of the StoryEditor, to which everything else is added.
    private JPanel rootPanel;

    /**
     * The toolbar that contains the 'Write your Story!' and 'Sort' buttons.
     */
    private JToolBar topToolBar;

    private JButton writeButton = new JButton();
    private String writeText = "Write your Story!";
    private ImageIcon writeIcon;
    private String planText = "Go back to planning";
    private ImageIcon planIcon;

    /**
     * Whether or not the story editor is collapsed.
     */
    private boolean collapsed = true;
    
    /**
     * The panel that collapses/uncollapses when the 'Write your Story!' button
     * is pressed. Contains the documentPanel and bottomToolBar.
     */
    private JPanel collapsiblePanel;

    // The panel that contains the FunctionEditors for each function in the
    // story. FunctionEditors are dynamically added and removed as the story map
    // is changed.
    private JPanel cardPanel = new JPanel();
    private CardLayout cardLayout = new CardLayout();

    /**
     * The title of the story. Goes at the top of the documentPanel.
     */
    private JTextField title = new JTextField("Enter your story's title here.");
    
    // The panel containing the next and previous buttons.
    private JPanel buttons = new JPanel();
    private JButton next = new JButton("Next");
    private JButton prev = new JButton("Previous");

    /**
     * The toolbar that contains the Cut, Copy, Paste, Save etc. buttons.
     */
    private JToolBar bottomToolBar;
        
    /**
     * The JFrame in which this StoryEditor will be used.
     */
    private JFrame parent;
    
    private final Application app;
    
    public StoryEditor(JFrame parent, final Application app) {
        this.parent = parent;
        this.app = app;
        
        rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());

        topToolBar = new JToolBar();
        topToolBar.setFloatable(false);
        topToolBar.setRollover(true);
        rootPanel.add(topToolBar,BorderLayout.NORTH);

        writeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                collapse();
            }
        });
        try {
            writeIcon = Util.readImageIconFromClassPath("/data/icons/write.png");
            planIcon = Util.readImageIconFromClassPath("/data/icons/arrow_up.png");
        } catch (IOException e) {
            Application.getInstance().logWarning("StoryEditor: IOException when reading in icons." + e.toString());
        }
        writeButton.setText(writeText);
        writeButton.setIcon(writeIcon);
        writeButton.setVerticalTextPosition(AbstractButton.CENTER);
        writeButton.setHorizontalTextPosition(AbstractButton.LEADING);
        topToolBar.add(writeButton);

        topToolBar.add(new JSeparator(SwingConstants.VERTICAL));

        JButton sortButton = new JButton();
        sortButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Messager.getMessager().send("sort", null);
            }
        });
        configureButton("Sort", "/data/icons/sort.png", sortButton);
        sortButton.setVerticalTextPosition(AbstractButton.CENTER);
        sortButton.setHorizontalTextPosition(AbstractButton.LEADING);
        topToolBar.add(sortButton);

        collapsiblePanel = new JPanel();
        collapsiblePanel.setLayout(new BorderLayout());
        rootPanel.add(collapsiblePanel,BorderLayout.CENTER);

        cardPanel.setLayout(cardLayout);
        collapsiblePanel.add(cardPanel,BorderLayout.CENTER);

        buttons.setLayout(new BoxLayout(buttons,BoxLayout.Y_AXIS));
        prev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.previous(cardPanel);
            }
        });
        prev.setAlignmentX(Component.RIGHT_ALIGNMENT);
        buttons.add(prev);
        buttons.add(Box.createVerticalGlue());
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.next(cardPanel);
            }
        });
        next.setAlignmentX(Component.RIGHT_ALIGNMENT);
        buttons.add(next);
        collapsiblePanel.add(buttons,BorderLayout.EAST);

        //title.setVisible(false);
        //title.setFont(Fonts.HUGE);
        //title.setHorizontalAlignment(JTextField.CENTER);
        //documentPanel.add(title);
        //scrollPane = new JScrollPane(documentPanel);        
        collapsiblePanel.setVisible(false); // Starts off collapsed.
        
        bottomToolBar = new JToolBar();
        bottomToolBar.setFloatable(false);
        bottomToolBar.setRollover(true);
        collapsiblePanel.add(bottomToolBar,BorderLayout.SOUTH);

        addButton("Cut","/data/icons/cut.png",new DefaultEditorKit.CutAction(),bottomToolBar);
        addButton("Copy","/data/icons/copy.png",new DefaultEditorKit.CopyAction(),bottomToolBar);
        addButton("Paste","/data/icons/paste.png",new DefaultEditorKit.PasteAction(),bottomToolBar);
        
        bottomToolBar.add(new JSeparator(SwingConstants.VERTICAL));

        JButton exportButton = addButton("Export Story as HTML", "/data/icons/save_as_html.png",bottomToolBar);
        exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                app.saveAsHTML();
            }
        });        
        
        JButton saveButton = addButton("Save Story","/data/icons/save.png",bottomToolBar);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                app.save();
            }
        });
                
        //root_panel.setPreferredSize(new Dimension(parent.getWidth(),24));
        Messager.getMessager().accept("button clicked",this,null);
    }
        
    private void configureButton(String text, String imagePath, JButton button) {
        button.setText(text);
        try {
            ImageIcon icon = Util.readImageIconFromClassPath(imagePath);
            button.setIcon(icon);
        } catch (IOException e) {
            // If we can't read an icon file we don't report it to the user,
            // just create a button with text and no icon.
        }        
        button.setVerticalTextPosition(AbstractButton.BOTTOM);
        button.setHorizontalTextPosition(AbstractButton.CENTER);        
    }
    
    private JButton addButton(String text, String imagePath, JToolBar toolBar) {
        JButton button = new JButton();
        configureButton(text, imagePath, button);
        toolBar.add(button);
        return button;
    }
    
    private JButton addButton(String text, String imagePath, Action action, JToolBar toolBar) {
        JButton button = new JButton(action);
        configureButton(text, imagePath, button);
        toolBar.add(button);
        return button;
    }
            
    private void collapse() {
        if (collapsed) {
            collapsed = false;
            collapsiblePanel.setPreferredSize(new Dimension(parent.getWidth(),parent.getHeight()/2));
            title.setVisible(true);
            collapsiblePanel.setVisible(true);
            collapsiblePanel.getParent().validate();
            collapsiblePanel.getParent().repaint();
            writeButton.setText(planText);
            writeButton.setIcon(planIcon);
            Messager.getMessager().send("Editor uncollapsed", this);
        } else {
            collapsed = true;
            collapsiblePanel.setPreferredSize(new Dimension(parent.getWidth(),0));
            title.setVisible(false);
            collapsiblePanel.setVisible(false);
            collapsiblePanel.getParent().validate();
            collapsiblePanel.getParent().repaint();
            writeButton.setText(writeText);
            writeButton.setIcon(writeIcon);
            Messager.getMessager().send("Editor collapsed", this);
        }
    }
    
    /**
     * Update the list of FunctionEditors in this StoryEditor.
     */
    public void update(ArrayList<StoryCard> new_cards) {
        cardPanel.removeAll();
        for (StoryCard s : new_cards) {
            FunctionEditor e = s.getEditor();
            // FIXME: this might cause a problem if we can have two story cards
            // with the same name.
            cardPanel.add(e.getComponent(),e.getFunction().getName());
        }
    }
        
    /**
     * Return the root component of this StoryEditor, i.e. the component that
     * should be added to a contentPane to add this StoryEditor to a JFrame.
     */
    public JComponent getComponent() {
        return rootPanel;
    }
            
    public boolean isCollapsed() {
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