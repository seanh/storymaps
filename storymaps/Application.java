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
import java.io.IOException;
import java.util.TimerTask;
import java.util.Timer;
import javax.swing.*;

/**
 * This is the main class of the StoryMaps application. It constructs the GUI
 * and the other main parts of the application. It is a Singleton.
 * 
 * @author seanh
 */
public class Application implements Receiver {

    /**
     * The Swing frame that represents the application window.
     */
    private JFrame frame;
    
    /**
     * The Swing frame's contentPane.
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
     * A subnode of home to which the story map and Write Story button are
     * attached.
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
     * The PNode that the PCamera is currently focused on.
     */
    private PNode target;

    /**
     * Swing file chooser dialog used for saving and restoring to and from file.
     */
    final JFileChooser fc = new JFileChooser();
    
    /**
     * Icon for the toolbar.
     */
    private ImageIcon aboutIcon;

    /**
     * Icon for the toolbar.
     */
    private ImageIcon helpIcon;

    /**
     * The directory that autosave files will be saved to.
     */
    private File autosavedir;
    
    /**
     * The 'Write Story' button that collapses and uncollapses the StoryEditor.
     */
    private WriteStoryButton writeStory;
    
    /**
     * The singleton instance of this class.
     */
    private static final Application INSTANCE = new Application();
    
    /**
     * Get the singleton instance of this class.
     */
    public static Application getInstance() {
        return INSTANCE;
    }

    /**
     * Return this application's single StoryEditor instance.
     */
    StoryEditor getStoryEditor() {
        return editor;
    }

    /**
     * Return the Application to the state recorded in an ApplicationMemento.
     */
    void restoreFromMemento(Object o) {
        ApplicationMemento memento = null;
        try {
            memento = (ApplicationMemento) o;
        } catch (ClassCastException e) {
            // FIXME: display an error message that the file could not be opened.
        }
        
        // FIXME: is this enough to really dispose of cards?
        cards.getNode().removeFromParent();
        cards = memento.getStoryCards();
        home.addChild(cards.getNode());
        
        // FIXME: is this enough to really dispose of map?
        map.getNode().removeFromParent();
        map = memento.getStoryMap();
        second_home.addChild(map.getNode());
        target = home;
    }    
    
    /**
     * Start the application.
     */
    public static void main(String[] args) {
        // Nothing to do here, the single Application instance was already
        // constructed when it was declared above.
    }
    
    /**
     * Construct and start the application.
     */
    private Application() {                
        makeFrame();
                
        // Subscribe to the messages sent by StoryEditor when it is collapsed
        // and uncollapsed.
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
     * Report an exception to the user.
     */
    private void reportException(String message) {
        // TODO.
    }
    
    private JButton createButton(String path, String text) {
        JButton button;
        try {
            ImageIcon icon = Util.readImageIconFromFile(path);
            button = new JButton(text, icon);
        } catch (IOException e) {
            // If we can't read an icon file we don't report it to the user,
            // just create a button with text and no icon.
            button = new JButton(text);
        }        
        button.setVerticalTextPosition(AbstractButton.BOTTOM);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        return button;
    }
           
    /**
     * Construct the application's toolbar, with buttons for controlling the
     * application.
     */
    private void makeToolBar() {
        JToolBar toolBar = new JToolBar("StoryMaps");
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
                
        JButton openButton = createButton("/storymaps/icons/open.png","Open");
        toolBar.add(openButton);
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                open();
            }
        });

        JButton saveButton = createButton("/storymaps/icons/save.png","Save");        
        toolBar.add(saveButton);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });

        JButton saveAsHtmlButton = createButton("/storymaps/icons/save_as_html.png","Save as HTML");        
        toolBar.add(saveAsHtmlButton);
        saveAsHtmlButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveAsHTML();
            }
        });        
                
        contentPane.add(toolBar, BorderLayout.NORTH);
    }

    /**
     * Initialise the Piccolo canvas.
     */
    private void initializePCanvas() {

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
            String filename = fc.getSelectedFile().getAbsolutePath();
            try {                
                Object m = XMLHandler.getInstance().readXMLAbsolute(filename);                
                restoreFromMemento(m);
            } catch (IOException e) {
                // FIXME: display a friendly message to the user via the GUI,
                // print the exception itself to stderr and log it to an errors
                // log file.
                String message = "Application.open(): IOException when trying to open story file at path: "+filename;
                System.out.println(message);
                System.out.println(e);                
            }            
        } else {
            // Open command cancelled by user.
        }
    }

    /**
     * Return a new ApplicationMemento object recording a snapshot of the
     * current state of the Application.
     */
    private ApplicationMemento createMemento() {
        return new ApplicationMemento(map,cards);
    }    
    
    /**
     * Save the current state of the application in the autosave dir with a
     * filename constructed from the current system time.
     */
    private void autosave() {
        String now = Util.nowStr();
        File save = new File(autosavedir,now);
        String filename = save.getAbsolutePath();
        ApplicationMemento memento = createMemento();
        try {
            XMLHandler.getInstance().writeXML(memento,filename);
        } catch (IOException e) {
            // FIXME: display a friendly message to the user via the GUI, print
            // the exception itself to stderr and append it to an errors log
            // file.
            System.out.println("Application.autosave(): IOError when writing to path: "+filename);
            System.out.println(e);
        }
    }    
    
    /**
     * This method is called when the Save button is pressed.
     */
    private void save() {
        int returnVal = fc.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                XMLHandler.getInstance().writeXML(createMemento(), fc.getSelectedFile().getAbsolutePath());
            } catch (IOException e) {
                // FIXME: display a more friendly message to the user via the
                // GUI, print the exception itself to stderr and append it to an
                // errors log file.
                System.out.println("IOException when saving story.");
                System.out.println(e);
            }
        } else {
            // Open command cancelled by user.
        }
    }
    
    /**
     * This method is called when the "Save as HTML" button is pressed.
     */
    private void saveAsHTML() {
        int returnVal = fc.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                String html = TemplateHandler.getInstance().renderStoryMap(map);
                Util.writeTextToFile(html, fc.getSelectedFile().getAbsolutePath());
            } catch (IOException e) {
                // FIXME: display a more friendly message to the user via the
                // GUI, print the exception itself to stderr and append it to an
                // errors log file.
                System.out.println("IOException when saving story as HTML");
                System.out.println(e);
            } catch (TemplateHandlerException e) {
                // FIXME: display a more friendly message to the user via the
                // GUI, print the exception itself to stderr and append it to an
                // errors log file.
                System.out.println("TemplateHandlerException when saving story as HTML");
                System.out.println(e);
            }
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
        /*JOptionPane.showMessageDialog(frame,
                Util.readTextFromFileRelative("/HELP"),
                "Help",
                JOptionPane.INFORMATION_MESSAGE,
                helpIcon);
        */
    }

    /**
     * This method is called when the About button is pressed. Shows the About
     * dialog.
     */
    private void about() {
        /*JOptionPane.showMessageDialog(frame,
                Util.readTextFromFileRelative("/README"),
                "About StoryMaps",
                JOptionPane.INFORMATION_MESSAGE,
                aboutIcon);*/
    }
}