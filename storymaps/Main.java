/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storymaps;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

/**
 * The main class is a Piccolo PFrame contain the major elements.
 * 
 * @author seanh
 */
public class Main {

    private static void createAndShowGui() {

        JFrame frame = new JFrame("Story Maps");

        JMenuBar menu_bar = new JMenuBar();
        menu_bar.setOpaque(true);
        menu_bar.setBackground(new Color(154, 165, 127));
        frame.setJMenuBar(menu_bar);

        JMenu file_menu = new JMenu("File");
        menu_bar.add(file_menu);

        JMenuItem open_item = new JMenuItem("Open");
        open_item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
        file_menu.add(open_item);

        JMenuItem quit_item = new JMenuItem("Quit");
        quit_item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });
        file_menu.add(quit_item);

        PCanvas pcanvas = new PCanvas();
        pcanvas.setBackground(new Color(0, 255, 255, 255));

        Container story_editor_container = new Container();
        story_editor_container.setLayout(new BoxLayout(story_editor_container,BoxLayout.LINE_AXIS));
        story_editor_container.add(new JLabel("Function"));
        story_editor_container.add(new JEditorPane());
                
        /*JEditorPane editorPane = new JEditorPane();
        JScrollPane editorScrollPane = new JScrollPane(editorPane);
        editorScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        editorScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        editorScrollPane.setPreferredSize(new Dimension(250, 145));
        editorScrollPane.setMinimumSize(new Dimension(10, 10));*/

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                pcanvas, story_editor_container);
        splitPane.setOneTouchExpandable(true);
       
        Dimension minimumSize = new Dimension(1024, 768);
        frame.setMinimumSize(minimumSize);
        minimumSize = new Dimension(1024, 600);
        pcanvas.setMinimumSize(minimumSize);
        minimumSize = new Dimension(1024, 168);
        story_editor_container.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(600);
        
        story_editor_container.addComponentListener(new ComponentListener() {

            public void componentHidden(ComponentEvent e) {
                // Not interested.
                ;
            }

            public void componentMoved(ComponentEvent e) {
                // Not interested.
                ;
            }

            public void componentResized(ComponentEvent e) {
                // Here we would tell our Piccolo app to zoom in to the story
                // map only, or out to the story map and the story bank.
                System.out.println("Editor pane resized.");
            }

            public void componentShown(ComponentEvent e) {
                // Not interested.
                ;
            }
        });

        frame.setContentPane(splitPane);

        final PCamera cam = pcanvas.getCamera();

        // Now the Piccolo stuff. (This should probably go in another method.)
        BankOfStoryCards bank = new BankOfStoryCards();
        for (int i = 0; i < 30; i++) {
            StoryCard card = new StoryCard(cam);
            bank.addChild(card);
        }
        pcanvas.getLayer().addChild(bank);


        frame.pack();
        frame.setVisible(true);

        // And back to Piccolo again. For some reason this has to happen after
        // the final Swing stuff: zoom the camera to fit everything in view.
        cam.animateViewToCenterBounds(cam.getGlobalFullBounds(), true, 0);

        // Event listener for zooming out when right mouse button is clicked.
        pcanvas.addInputEventListener(new PBasicInputEventHandler() {

            @Override
            public void mouseClicked(PInputEvent event) {
                if (event.getButton() == 3) {
                    cam.animateViewToCenterBounds(cam.getGlobalFullBounds(), true, 1000);
                    event.setHandled(true);
                } else {
                    event.setHandled(false);
                }
            }
        });

        //Toolkit.getDefaultToolkit().getSystemEventQueue().push(new FilteredEventQueue());
    }

    private static void openFile() {
        System.out.println("openFile");
    }

    private static void quit() {
        System.out.println("quit");
    }

    /**
     * @param args There are no command line arguments yet.
     */
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGui();
            }
        });
    }
}
