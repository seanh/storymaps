package storymaps;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author seanh
 */
public class Swing implements Receiver {
    
    private JFrame frame;
    private Container contentPane;
    
    private PCanvas canvas;

    /**
     * The home node, to which all other nodes are attached.
     */
    private VerticalLayoutNode home = new VerticalLayoutNode(50);
    
    /**
     * The collection of story cards that the user chooses from.
     */
    private StoryCards cards;
    
    /**
     * The story map where the user places and arranges her chosen cards.
     */
    private StoryMap map;    
    
    private StoryEditor editor;
    
    private HelpText help_text = new HelpText();
        
    private Object storymap_memento = null;
    
    public Swing() {
        makeFrame();
    }
        
    private void makeFrame() {
        frame = new JFrame("StoryMaps");        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
        contentPane = frame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        
        makeToolBar();

        editor = new StoryEditor();
        editor.getComponent().setPreferredSize(new Dimension(1024,200));
                
        initializePCanvas();
        contentPane.add(canvas);
                
        contentPane.add(editor.getComponent());
                                
        frame.pack();
        frame.setVisible(true);
    }

    
    public void initializePCanvas() {

        canvas = new PCanvas();
        canvas.setPreferredSize(new Dimension(1024,568));
        //canvas.setMinimumSize(new Dimension(800,600));
        canvas.setBackground(Color.DARK_GRAY);
                
        canvas.getLayer().addChild(home);

        cards = new StoryCards("Choose the Story Cards you want from here...");
        home.addChild(cards.getNode());
        
        map = new StoryMap("... and arrange them into your own Story Map here.",editor);
        home.addChild(map.getNode());        
                                
        // Remove the default event handler that enables panning with the mouse.    
        canvas.removeInputEventListener(canvas.getPanEventHandler());
        
        final PCamera cam = canvas.getCamera();
        cam.animateViewToCenterBounds(home.getGlobalFullBounds(), true, 750);
        
        // Make middle mouse button return camera to home position.
        cam.addInputEventListener(new PBasicInputEventHandler() { 		                    
            @Override
            public void mousePressed(PInputEvent event) {
                if (event.getButton() == 3) {
                    cam.animateViewToCenterBounds(home.getGlobalFullBounds(), true, 750);
                }
            }
        });
        
        // Listen for 'clicked' messages from story cards (the receive method
        // will be called), this is how we make RMB zoom in on cards.
        Messager m = Messager.getMessager();
        m.accept("StoryCard clicked", this, null);
                        
        cam.addChild(help_text.getNode());
        help_text.getNode().setOffset(1024/2f,768/2f);
        help_text.show("Welcome to the Story Maps application!\nLeft-click to drag,\ndouble-click to zoom in,\nright-click to zoom out.");        
    }

     public void receive(String name, Object receiver_arg, Object sender_arg) {
         if (name.equals("StoryCard clicked")) {
            PCamera cam = canvas.getCamera();
            StoryCard card = (StoryCard) sender_arg;
            PNode node = card.getNode();
            cam.animateViewToCenterBounds(node.getGlobalBounds(), true, 750);
         }
     }    
     
    private void makeToolBar() {
        JToolBar toolBar = new JToolBar("StoryMaps");
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        
        ImageIcon newIcon = new ImageIcon("src/storymaps/data/document-new.png");
        JButton newButton = new JButton("New",newIcon);
        newButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        newButton.setHorizontalTextPosition(AbstractButton.CENTER);
        toolBar.add(newButton);
        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newStory();
            }
        });
        
        ImageIcon openIcon = new ImageIcon("src/storymaps/data/document-open.png");
        JButton openButton = new JButton("Open",openIcon);
        openButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        openButton.setHorizontalTextPosition(AbstractButton.CENTER);
        toolBar.add(openButton);
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                open();
            }
        });
        
        ImageIcon saveIcon = new ImageIcon("src/storymaps/data/document-save.png");
        JButton saveButton = new JButton("Save",saveIcon);
        saveButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        saveButton.setHorizontalTextPosition(AbstractButton.CENTER);
        toolBar.add(saveButton);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        
        toolBar.addSeparator();
        
        ImageIcon printIcon = new ImageIcon("src/storymaps/data/document-print.png");
        JButton printButton = new JButton("Print",printIcon);
        printButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        printButton.setHorizontalTextPosition(AbstractButton.CENTER);
        toolBar.add(printButton);
        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                print();
            }
        });
        
        contentPane.add(toolBar);    
    }
    
    private void newStory() {
        
    }
    
    private void open() {
        System.out.println("Restoring");
        map.restoreFromMemento(storymap_memento);
    }

    private void save() {
        System.out.println("Saving");
        storymap_memento = map.saveToMemento();
    }

    private void print() {
        
    }
    
    private void showAbout() {
        JOptionPane.showMessageDialog(frame,
                "StoryMaps",
                "About StoryMaps",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        new Swing();
    }   
}