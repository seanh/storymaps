package storymaps;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolox.PFrame;

/**
 * The main class that runs the Story Maps application.
 * 
 * @author seanh
 */
public class Main extends PFrame implements Receiver {

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
    
    // Override PFrame's initialize method to run the demo.
    @Override
    public void initialize() {
        setSize(1024, 768);
        
        PCanvas canvas = getCanvas();
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
    }

     public void receive(String name, Object receiver_arg, Object sender_arg) {
         if (name.equals("StoryCard clicked")) {
            PCamera cam = getCanvas().getCamera();
            StoryCard card = (StoryCard) sender_arg;
            PNode node = card.getNode();
            cam.animateViewToCenterBounds(node.getGlobalBounds(), true, 750);
         }
     }    
    
    public static void main(String args[]) {
        new Main();
    }
}