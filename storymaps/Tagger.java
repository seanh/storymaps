package storymaps;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Tag editor application for the story maps application.
 * 
 * @author seanh
 */
public class Tagger {
    
    private JFrame frame;
    private Container contentPane;
    private JPanel tagsPanel;
    private JTextField addField;
    private JScrollPane scrollPane;
    
    /**
     * Construct and start the application.
     */
    public Tagger() {
        makeFrame();
    }
    
    /**
     * Initialise the Swing frame and its various containers and other
     * components.
     */
    private void makeFrame() {
        frame = new JFrame("StoryMaps Tagger");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(230,600));  
        
        contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("Tags");
        
        addField = new JTextField("Add a new tag...");
        addField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newTag();
            }
        });
        addField.addFocusListener(new FocusListener() {
           public void focusGained(FocusEvent e) {
               addField.selectAll();
           }
            public void focusLost(FocusEvent arg0) {            
            }
        });        
        
        JPanel north = new JPanel();
        north.setLayout(new BoxLayout(north,BoxLayout.PAGE_AXIS));
        north.add(title);
        north.add(addField);
        contentPane.add(north,BorderLayout.NORTH);
                        
        tagsPanel = new JPanel();
        tagsPanel.setLayout(new BoxLayout(tagsPanel,BoxLayout.PAGE_AXIS));
        scrollPane = new JScrollPane(tagsPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        contentPane.add(scrollPane,BorderLayout.CENTER);
        
        frame.pack();
        frame.setVisible(true);                        
    }
    
    private void newTag() {
        Tag tag = new Tag(addField.getText(),this);
        tagsPanel.add(tag.getComponent());
        contentPane.validate();
        addField.setText("Add a new tag...");
        addField.selectAll();
    }
    
    public void remove(Tag tag) {
        int n = JOptionPane.showConfirmDialog(frame,                
                "Deleting the tag will remove the tag from\n" +
                "any story cards that have it. Are you sure?",
                "Delete tag '"+tag.getName()+"'?",
                JOptionPane.YES_NO_OPTION);
        if (n == 0) {
            tagsPanel.remove(tag.getComponent());
            tagsPanel.getParent().invalidate();
            tagsPanel.getParent().repaint();
            tagsPanel.doLayout();
        }
    }
            
    /**
     * Start the application.
     */
    public static void main(String[] args) {
        new Tagger();
    }   
}

class Tag {
    private Tagger tagger;
    private JPanel panel = new JPanel();
    private JTextField text = new JTextField();
    private JButton del = new JButton(ResourceLoader.loadImageIcon("/storymaps/data/process-stop.png"));    
        
    public Tag(String name, Tagger tagger) {
        
        this.tagger = tagger;
        
        panel.setLayout(new FlowLayout());
        
        text.setText(name);        
        text.setEditable(false);
        text.setBorder(new EmptyBorder(0,0,0,0));
        text.setPreferredSize(new Dimension(150,20));

        text.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                text.setBorder(new EmptyBorder(0,0,0,0));
                text.setEditable(false);
            }
        });
        text.addFocusListener(new FocusListener() {
           public void focusGained(FocusEvent e) {
               text.setBorder(LineBorder.createGrayLineBorder());
               text.setEditable(true);
               text.selectAll();
           }
           public void focusLost(FocusEvent arg0) {            
                text.setBorder(new EmptyBorder(0,0,0,0));
                text.setEditable(false);
           }
        });
        panel.add(text);

        del.setBorder(null);
        del.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Tag.this.tagger.remove(Tag.this);
            }
        });        
        panel.add(del);        
        panel.setMaximumSize(new Dimension(200,25));
        //panel.setBorder(LineBorder.createGrayLineBorder());
    }
    public JComponent getComponent() {
        return panel;
    }
    public String getName() {
        return text.getText();
    }
}