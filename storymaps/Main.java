package storymaps;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import javax.swing.*;

/**
 * This is the main class of the StoryMaps application. It constructs the GUI
 * and the other main parts of the application.
 * 
 * @author seanh
 */
public class Main implements Receiver, Originator {
    
    /**
     * The Swing frame that represents the application window.
     */
    private JFrame frame;
    
    /**
     * The Swing frame's conentPane.
     */
    private Container contentPane;    
    
    /**
     * The Piccolo canvas, where all the Piccolo action happens.
     */
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
    
    /**
     * The story editor, where the user types in and edits the text of her
     * story.
     */
    private StoryEditor editor;
    
    /**
     * Object used to display transparent overlay text messages to the user.
     */
    private HelpText help_text = new HelpText();

    /**
     * The Caretaker object that holds onto saved states of the application.
     */
    private Caretaker caretaker = new Caretaker();
    
    /**
     * Construct and start the application.
     */
    public Main() {
        makeFrame();
    }
    
    /**
     * Initialise the Swing frame and its various containers and other
     * components.
     */
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

    /**
     * Construct the application's toolbar, with buttons for controlling the
     * application.
     */
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
    
    /**
     * Initialise the Piccolo canvas.
     */
    public void initializePCanvas() {

        canvas = new PCanvas();
        canvas.setPreferredSize(new Dimension(1024,568));
        //canvas.setMinimumSize(new Dimension(800,600));
        canvas.setBackground(Color.DARK_GRAY);
        canvas.setFocusable(false); // Never get the keyboard focus.
                
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
        m.accept("StoryCard double-clicked", this, null);
                        
        cam.addChild(help_text.getNode());
        help_text.getNode().setOffset(1024/2f,768/2f);
        help_text.show("Welcome to the Story Maps application!\nLeft-click to drag,\ndouble-click to zoom in,\nright-click to zoom out.");        
    }
    
    /**
     * Receive messages from the global singleton Messager object.
     */
    public void receive(String name, Object receiver_arg, Object sender_arg) {
        if (name.equals("StoryCard double-clicked")) {
           PCamera cam = canvas.getCamera();
           StoryCardBase card = (StoryCardBase) sender_arg;
           PNode node = card.getNode();
           cam.animateViewToCenterBounds(node.getGlobalBounds(), true, 750);
        }
    }    
         
    /**
     * This method is called when the New button is pressed.
     */
    private void newStory() {
        
    }
    
    /**
     * This method is called when the Open button is pressed.
     */
    private void open() {        
        //Memento m = (Memento) caretaker.getMemento();
        //restoreFromMemento(m);
        restoreFromMemento(caretaker.readMemento());
    }

    /**
     * This method is called when the Save button is pressed.
     */
    private void save() {
        //caretaker.addMemento(saveToMemento());
        caretaker.writeMemento(saveToMemento());
    }

    /**
     * This method is called when the Print button is pressed.
     */    
    private void print() {
        
    }
    /**
     * This method is called when the About button is pressed. Shows the About
     * dialog.
     */    
    private void showAbout() {
        JOptionPane.showMessageDialog(frame,
                "StoryMaps",
                "About StoryMaps",
                JOptionPane.INFORMATION_MESSAGE);
    }

        // Implement the Originator interface.
    
    /**
     * A Memento object holds a saved state of the application.
     */
    private static class Memento implements Serializable {
        public Object storycards_memento;
        public Object storymap_memento;
        public Memento(Object storycards_memento, Object storymap_memento) {
            this.storycards_memento = storycards_memento;
            this.storymap_memento = storymap_memento;
        }
    }
        
    /** Return a memento object for the current state of the application. */
    public Object saveToMemento() {
        Object storycards_memento = cards.saveToMemento();
        Object storymap_memento = map.saveToMemento();        
        return new Memento(storycards_memento,storymap_memento);
    }                          

    /** 
     * Restore the state of the application from a memento object. 
     * 
     * @throws IllegalArgumentException if the argument cannot be cast to the
     * Main.Memento type (i.e. the object m is not an object returned by the
     * saveToMemento method of this class).
     */
    public void restoreFromMemento(Object o) {
        if (!(o instanceof Memento)) {
            throw new IllegalArgumentException("Argument not instanceof Memento.");
        } else {
            Memento m = (Memento) o;
            cards.restoreFromMemento(m.storycards_memento);
            map.restoreFromMemento(m.storymap_memento);            
        }        
    }
    
    /**
     * Start the application.
     */
    public static void main(String[] args) {
        new Main();
    }   
}