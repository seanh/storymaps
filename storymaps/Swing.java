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
    
    private HelpText help_text = new HelpText();
    
    
    public Swing() {
        makeFrame();
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path, String description)
    {
        return new ImageIcon(path, description);
    }
        
    private void makeFrame() {
        frame = new JFrame("StoryMaps");        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
                            
        initializePCanvas();
        contentPane.add(canvas);
                
        JPanel documentPanel = new JPanel();
        documentPanel.setLayout(new BoxLayout(
                                    documentPanel, BoxLayout.PAGE_AXIS));
        JScrollPane scrollPane = new JScrollPane(documentPanel);
        contentPane.add(scrollPane);
                
        for (Function f : Functions.getFunctions()) {
            documentPanel.add(makeFunctionEditor(f));
        }        
        
        makeMenu();
        
        frame.pack();
        frame.setVisible(true);
    }

    
    public void initializePCanvas() {

        canvas = new PCanvas();
        canvas.setPreferredSize(new Dimension(800,600));
        canvas.setMinimumSize(new Dimension(800,600));
        canvas.setBackground(Color.DARK_GRAY);
                
        canvas.getLayer().addChild(home);

        cards = new StoryCards("Choose the Story Cards you want from here...");
        home.addChild(cards.getNode());
        
        map = new StoryMap("... and arrange them into your own Story Map here.");        
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
              
    private JPanel makeFunctionEditor(Function f) {
        JPanel container = new JPanel();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        container.setLayout(flowLayout);
        ImageIcon icon = createImageIcon(f.image,"Illustation for function.");
        JLabel label = new JLabel(icon);
        container.add(label);
        JTextArea textArea = new JTextArea(7,66);
        JScrollPane scrollPane = new JScrollPane(textArea);                    
        container.add(scrollPane);
        return container;
    }

    private void makeMenu() {
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);
        
        JMenu fileMenu = new JMenu("File");
        menubar.add(fileMenu);
        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
        fileMenu.add(openItem);
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });
        fileMenu.add(quitItem);
        
        JMenu helpMenu = new JMenu("Help");
        menubar.add(helpMenu);
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAbout();
            }
        });
        helpMenu.add(aboutItem);        
    }
    
    private void openFile() {
        
    }
    
    private void quit() {
        
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
