package storymaps;
import storymaps.ui.WriteStoryButton;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.TimerTask;
import java.util.Timer;
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
     * Node to which the story map and Write Story button are attached.
     */
    private VerticalLayoutNode second_home = new VerticalLayoutNode(50);
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
     * The Caretaker object that holds onto saved states of the application.
     */
    private Caretaker caretaker = new Caretaker();

    /**
     * The PNode that the PCamera is currently focused on.
     */
    private PNode target;

    /**
     * Swing file chooser dialog used for saving and restoring to and from file.
     */
    final JFileChooser fc = new JFileChooser();
    ImageIcon aboutIcon;
    ImageIcon helpIcon;

    private File autosavedir;
    
    private WriteStoryButton writeStory;
    
    /**
     * Construct and start the application.
     */
    public Main() {
        makeFrame();
        Messager.getMessager().accept("Editor uncollapsed", this, null);
        Messager.getMessager().accept("Editor collapsed", this, null);

        // Create the autosave directory for this session.
        String now = Util.nowStr();
        autosavedir = new File(now);
        autosavedir.mkdir();
        
        // Start a task that autosaves every 60 seconds.
        TimerTask autoSave = new TimerTask() {
            public void run() {autosave();}};
        Timer timer = new Timer();
        timer.schedule(autoSave, 60000, 60000);        
    }

    private void autosave() {
        String now = Util.nowStr();
        File save = new File(autosavedir,now);
        caretaker.writeMemento(saveToMemento(), save.getAbsolutePath());
    }

    /**
     * Initialise the Swing frame and its various containers and other
     * components.
     */
    private void makeFrame() {
        frame = new JFrame("StoryMaps");
        
        // Add a window listener to the frame that autosaves when the window is
        // closed.
        frame.addWindowListener(new WindowListener() {
            public void windowClosing(WindowEvent arg0) {
                autosave();
                System.exit(0);
            }
            public void windowOpened(WindowEvent arg0) {
            }
            public void windowClosed(WindowEvent arg0) {
            }
            public void windowIconified(WindowEvent arg0) {
            }
            public void windowDeiconified(WindowEvent arg0) {
            }
            public void windowActivated(WindowEvent arg0) {
            }
            public void windowDeactivated(WindowEvent arg0) {
            }
        });

        contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());

        makeToolBar();

        editor = new StoryEditor(frame);

        canvas = new PCanvas();
        canvas.setPreferredSize(new Dimension(640, 480));
        canvas.setBackground(Color.DARK_GRAY);
        canvas.setFocusable(false); // Never get the keyboard focus.                        
        contentPane.add(canvas, BorderLayout.CENTER);

        canvas.addComponentListener(new ComponentListener() {
            // This method is called after the component's size changes
            public void componentResized(ComponentEvent evt) {
                repositionCamera();
            }

            public void componentMoved(ComponentEvent arg0) {
            }

            public void componentShown(ComponentEvent arg0) {
            }

            public void componentHidden(ComponentEvent arg0) {
            }
        });

        contentPane.add(editor.getComponent(), BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        initializePCanvas();
    }
        
    /**
     * Construct the application's toolbar, with buttons for controlling the
     * application.
     */
    private void makeToolBar() {
        JToolBar toolBar = new JToolBar("StoryMaps");
        toolBar.setFloatable(false);
        toolBar.setRollover(true);


        /*ImageIcon newIcon = ResourceLoader.loadImageIcon("/storymaps/data/document-new.png");
        JButton newButton = new JButton("New",newIcon);
        newButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        newButton.setHorizontalTextPosition(AbstractButton.CENTER);
        toolBar.add(newButton);
        newButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        newStory();
        }
        });*/

        ImageIcon openIcon = ResourceLoader.loadImageIcon("/storymaps/data/document-open.png");
        JButton openButton = new JButton("Open", openIcon);
        openButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        openButton.setHorizontalTextPosition(AbstractButton.CENTER);
        toolBar.add(openButton);
        openButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                open();
            }
        });

        ImageIcon saveIcon = ResourceLoader.loadImageIcon("/storymaps/data/document-save.png");
        JButton saveButton = new JButton("Save", saveIcon);
        saveButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        saveButton.setHorizontalTextPosition(AbstractButton.CENTER);
        toolBar.add(saveButton);
        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        
        /*
        ImageIcon printIcon = ResourceLoader.loadImageIcon("/storymaps/data/document-print.png");
        JButton printButton = new JButton("Print", printIcon);
        printButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        printButton.setHorizontalTextPosition(AbstractButton.CENTER);
        toolBar.add(printButton);
        printButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                print();
            }
        });

        helpIcon = ResourceLoader.loadImageIcon("/storymaps/data/help-browser.png");
        JButton helpButton = new JButton("Help", helpIcon);
        helpButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        helpButton.setHorizontalTextPosition(AbstractButton.CENTER);
        toolBar.add(helpButton);
        helpButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                help();
            }
        });

        aboutIcon = ResourceLoader.loadImageIcon("/storymaps/data/emblem-favorite.png");
        JButton aboutButton = new JButton("About", aboutIcon);
        aboutButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        aboutButton.setHorizontalTextPosition(AbstractButton.CENTER);
        toolBar.add(aboutButton);
        aboutButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                about();
            }
        });
        */
        
        contentPane.add(toolBar, BorderLayout.NORTH);
    }

    /**
     * Initialise the Piccolo canvas.
     */
    public void initializePCanvas() {

        canvas.getLayer().addChild(home);

        cards = new StoryCards("Choose the Story Cards you want from here...");
        home.addChild(cards.getNode());

        home.addChild(second_home);
        
        map = new StoryMap("... and arrange them into your own Story Map here.", editor);
        second_home.addChild(map.getNode());

        writeStory = new WriteStoryButton();
        second_home.addChild(writeStory);
                
        // Remove the default event handler that enables panning with the mouse.    
        canvas.removeInputEventListener(canvas.getPanEventHandler());

        target = home;
        repositionCamera();

        canvas.getCamera().addInputEventListener(new PBasicInputEventHandler() {

            @Override
            public void mousePressed(PInputEvent event) {
                if (event.getButton() == 3) {
                    zoomToHome();
                }
            }
        });

        // Listen for 'clicked' messages from story cards (the receive method
        // will be called), this is how we make RMB zoom in on cards.
        Messager m = Messager.getMessager();
        m.accept("StoryCard double-clicked", this, null);
    }

    private void zoomToHome() {
        StoryCardBase prev = (StoryCardBase) target.getAttribute("StoryCardBase");
        if (prev != null) {
            prev.goToLowDetail();
        }
        if (map.getEditor().getCollapsed()) {
            target = home;
        } else {
            target = second_home;
        }
        repositionCamera(750);
    }

    /**
     * Receive messages from the global singleton Messager object.
     */
    public void receive(String name, Object receiver_arg, Object sender_arg) {
        if (name.equals("StoryCard double-clicked")) {
            /*StoryCardBase card = (StoryCardBase) sender_arg;
            PNode node = card.getNode();
            StoryCardBase prev = (StoryCardBase) target.getAttribute("StoryCardBase");
            if (prev != null) {
                prev.goToLowDetail();
            }
            target = node;
            repositionCamera(750);*/
        } else if (name.equals("Editor uncollapsed")) {            
            zoomToHome();
        } else if (name.equals("Editor collapsed")) {            
            zoomToHome();
        }
    }

    /**
     * If the PNode field target is not null, reposition the camera to focus on
     * that node. (Used to correct the focus when the piccolo component is
     * resized).
     */
    private void repositionCamera() {
        if (target != null) {
            repositionCamera(target, 0);
        }
    }

    /**
     * If the PNode field target is not null, reposition the camera to focus on
     * that node, and take duration milliseconds to animate the camera to its
     * new position.
     */
    private void repositionCamera(long duration) {
        if (target != null) {
            repositionCamera(target, duration);
        }
    }

    /**
     * Zoom the Piccolo camera in or out so that it focuses on a given node.
     * @param node The node to focus on.
     * @param duration The amount of time (in milliseconds) to take to animate
     * the camera to its new position.
     */
    private void repositionCamera(PNode node, long duration) {
        final PCamera cam = canvas.getCamera();
        cam.animateViewToCenterBounds(node.getGlobalFullBounds(), true,
                duration);
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
        int returnVal = fc.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            restoreFromMemento(caretaker.readMemento(fc.getSelectedFile().getAbsolutePath()));
        } else {
            // Open command cancelled by user.
        }
    }

    /**
     * This method is called when the Save button is pressed.
     */
    private void save() {
        int returnVal = fc.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            caretaker.writeMemento(saveToMemento(), fc.getSelectedFile().getAbsolutePath());
        } else {
            // Open command cancelled by user.
        }


    }

    /**
     * This method is called when the Print button is pressed.
     */
    private void print() {
    }

    /**
     * This method is called when the Help button is pressed, shows the Help
     * dialog.
     */
    private void help() {
        JOptionPane.showMessageDialog(frame,
                ResourceLoader.loadString("/HELP"),
                "Help",
                JOptionPane.INFORMATION_MESSAGE,
                helpIcon);
    }

    /**
     * This method is called when the About button is pressed. Shows the About
     * dialog.
     */
    private void about() {
        JOptionPane.showMessageDialog(frame,
                ResourceLoader.loadString("/README"),
                "About StoryMaps",
                JOptionPane.INFORMATION_MESSAGE,
                aboutIcon);
    }
    // Implement the Originator interface.
    /**
     * A Memento object holds a saved state of the application.
     */
    public static class Memento {

        public Object storycards_memento;
        public Object storymap_memento;

        public Memento(Object storycards_memento, Object storymap_memento) {
            this.storycards_memento = storycards_memento;
            this.storymap_memento = storymap_memento;
        }

        @Override
        public String toString() {
            String string = this.storymap_memento.toString();
            string += this.storycards_memento.toString();
            return string;
        }
    }

    /** Return a memento object for the current state of the application. */
    public Object saveToMemento() {
        Object storycards_memento = cards.saveToMemento();
        Object storymap_memento = map.saveToMemento();
        return new Memento(storycards_memento, storymap_memento);
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